import requests
import time
import os
import sys
import xml.etree.ElementTree as ET

# --- Environment Variables ---
api_token = os.getenv("APICredentials")
api_test = os.getenv("testUUID")
api_environment = os.getenv("APIEnvironment")

for var, name in [(api_token, "APICredentials"),
                  (api_test, "testUUID"),
                  (api_environment, "APIEnvironment")]:
    if not var:
        print(f"❌ Environment variable '{name}' is not set.")
        sys.exit(1)

# --- Prepare Authorization ---
if not api_token.lower().startswith("bearer "):
    AUTH_TOKEN = f"Bearer {api_token}"
else:
    AUTH_TOKEN = api_token

HEADERS = {
    "Authorization": AUTH_TOKEN,
    "Content-Type": "application/json"
}

# --- URLs & Result Paths ---
RUNSCOPE_TRIGGER_URL = f"https://api.runscope.com/radar/{api_test}/trigger?runscope_environment={api_environment}"
RESULT_DIR = "test-results"
RESULT_FILE = os.path.join(RESULT_DIR, "runscope-result.xml")

# --- Trigger API Test ---
def trigger_test():
    print("🔄 Triggering API Monitoring test...")
    try:
        # Runscope trigger often expects an empty JSON body
        response = requests.post(RUNSCOPE_TRIGGER_URL, headers=HEADERS, json={})
        response.raise_for_status()
    except requests.exceptions.HTTPError as e:
        print(f"❌ Failed to trigger test: {e}")
        print("Response content:", response.text)
        sys.exit(1)
    except requests.exceptions.RequestException as e:
        print(f"❌ Request failed: {e}")
        sys.exit(1)

    data = response.json()
    try:
        run_info = data["data"]["runs"][0]
        api_test_run_url = run_info["api_test_run_url"]
        test_name = run_info["test_name"]
        test_run_url = run_info["test_run_url"]
        test_id = run_info["test_id"]
    except (KeyError, IndexError):
        print("❌ Failed to parse Runscope trigger response:")
        print(data)
        sys.exit(1)

    print(f"✅ Test triggered: {test_name} ({test_id})")
    print(f"🔁 Polling test run at: {api_test_run_url}")
    return api_test_run_url, test_name, test_run_url

# --- Poll Test Until Complete ---
def poll_until_complete(api_test_run_url, interval=5, max_attempts=60):
    attempts = 0
    status = "working"
    while status in ("init", "working"):
        if attempts >= max_attempts:
            print("⏱️ Timed out waiting for test to complete.")
            sys.exit(1)

        time.sleep(interval)
        try:
            resp = requests.get(api_test_run_url, headers=HEADERS)
            resp.raise_for_status()
            data = resp.json()
            status = data.get("data", {}).get("result")
            print(f"⏳ Current status: {status}")
        except requests.exceptions.RequestException as e:
            print(f"❌ Failed to fetch test status: {e}")
            sys.exit(1)

        attempts += 1

    return data

# --- Generate JUnit XML ---
def generate_junit_xml(test_name, final_result, test_run_url, duration_seconds):
    os.makedirs(RESULT_DIR, exist_ok=True)

    testsuite = ET.Element(
        "testsuite",
        name="API Monitoring Test Suite",
        tests="1",
        failures="0" if final_result.lower() == "pass" else "1",
        errors="0",
        skipped="0",
        time=f"{duration_seconds:.3f}"
    )

    testcase = ET.SubElement(
        testsuite,
        "testcase",
        classname="Runscope",
        name=test_name,
        time=f"{duration_seconds:.3f}",
        BlazeMeter_API_Test_URL=test_run_url or ""
    )

    if final_result.lower() != "pass":
        ET.SubElement(testcase, "failure", message=f"API Monitoring test result: {final_result}")

    ET.SubElement(testcase, "system-out").text = f"Test Report URL: {test_run_url or 'Not available'}"

    tree = ET.ElementTree(testsuite)
    tree.write(RESULT_FILE, encoding="utf-8", xml_declaration=True)
    print(f"📄 JUnit result saved to {RESULT_FILE}")

# --- Main ---
def main():
    start_time = time.time()

    api_test_run_url, test_name, test_run_url = trigger_test()
    status_response = poll_until_complete(api_test_run_url)

    duration = time.time() - start_time
    final_result = status_response.get("data", {}).get("result", "failed")
    print(f"✅ Final result: {final_result}")
    print(f"🕒 Duration: {duration:.2f} seconds")

    generate_junit_xml(test_name, final_result, test_run_url, duration)

if __name__ == "__main__":
    main()