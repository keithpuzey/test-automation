import requests
import time
import os
import xml.etree.ElementTree as ET
import json
import sys

# Load environment variables
PerfectoKey = os.getenv("PerfectoToken")
Perfectotest = os.getenv("PerfectoTest")
Perfectotestname = os.getenv("PerfectoTestname")
PERFECTOTESTURL = os.getenv("PERFECTOTESTURL")
symptom = os.getenv("symptom")

for var, name in [(PerfectoKey, "PerfectoToken"), (Perfectotest, "PerfectoTest"),
                  (Perfectotestname, "PerfectoTestname"), (PERFECTOTESTURL, "PERFECTOTESTURL"),
                  (symptom, "symptom")]:
    if not var:
        raise RuntimeError(f"❌ Environment variable '{name}' is not set.")

# Config
perfecto_cloud = 'demo.perfectomobile.com'
perfecto_cloud_app = 'demo.app.perfectomobile.com'
script_key = Perfectotest
RESULT_DIR = "test-results"
RESULT_FILE = os.path.join(RESULT_DIR, "perfecto-result.xml")
TEST_NAME = Perfectotestname

# --- Start Test ---
def start_test():
    url = f"https://{perfecto_cloud}/scriptless/api/executions"
    payload = { "testKey": Perfectotest }
    headers = {'Content-Type': 'application/json', "Perfecto-Authorization": PerfectoKey}

    print(f"📡 Sending request to start test: {url}")
    response = requests.post(url, headers=headers, json=payload)

    if response.status_code in [200, 201]:  # ✅ accept 201 as success
        try:
            r = response.json()
            print("✅ Test initiation response:", r)
            return r.get("executionId"), r.get("testGridReportUrl")
        except Exception as e:
            print("❌ Failed to parse start-test response:", e)
    else:
        print(f"❌ Error starting test: {response.status_code} {response.text}")
    return None, None

# --- Check Test Status ---
def check_test_status(execution_id):
    url = f"https://{perfecto_cloud}/scriptless/api/executions/{execution_id}"
    headers = {'Content-Type': 'application/json', "Perfecto-Authorization": PerfectoKey}
    try:
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        data = response.json()
        return (
            data.get("status"),
            data.get("reportKey"),
            data.get("devices", []),
            data.get("description"),
            data.get("numberOfFailedCommands"),
            data.get("endCode")  # <-- Only present at the end
        )
    except requests.exceptions.RequestException as e:
        print("❌ Error checking test status:", e)
        return None, None, [], None, None, None
        
        
# --- Get Device Details ---
def get_device_details(device_id):
    url = f"https://{perfecto_cloud_app}/api/v1/device-management/devices/{device_id}"
    headers = {'Perfecto-Authorization': PerfectoKey}
    try:
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        handset = response.json().get("handset", {})
        return handset
    except requests.exceptions.RequestException as e:
        print(f"❌ Failed to fetch device details: {e}")
        return {}

# --- Generate JUnit XML ---
def generate_junit_xml(test_name, result, test_grid_report_url, device_name, reason=None, duration_seconds=0.0, end_code=None):
    if not os.path.exists(RESULT_DIR):
        os.makedirs(RESULT_DIR)

    # Device info
    device_tested = device_name or "Unknown Device"

    testcase_attrs = {
        "classname": "PerfectoTest",
        "name": test_name,
        "time": f"{duration_seconds:.3f}",
        "Perfecto_Test_URL": test_grid_report_url or "",
        "Device_Tested": device_tested,
    }

    if end_code:
        testcase_attrs["EndCode"] = end_code   # <-- Add endCode into XML attributes

    testsuite = ET.Element("testsuite", name="Perfecto Test Suite", tests="1",
                           failures="0" if result == "passed" else "1", errors="0", skipped="0",
                           time=f"{duration_seconds:.3f}")
    testcase = ET.SubElement(testsuite, "testcase", attrib=testcase_attrs)

    if result != "passed":
        ET.SubElement(testcase, "failure", message=f"Test failed. Reason: {reason or 'Unknown'}")

    ET.SubElement(testcase, "system-out").text = f"Full Report: {test_grid_report_url or 'Not available'}"

    tree = ET.ElementTree(testsuite)
    tree.write(RESULT_FILE, encoding="utf-8", xml_declaration=True)
    print(f"📄 JUnit result saved to {RESULT_FILE}", flush=True)


# --- Main ---
def main():
    start_time = time.time()
    execution_id, test_grid_report_url = start_test()  # ✅ Only 2 values

    if execution_id is None:
        generate_junit_xml(TEST_NAME, "failed", "N/A", None, reason="Failed to start test", duration_seconds=0.0)
        sys.exit(1)

    print("🕒 Test execution started with ID:", execution_id, flush=True)

    while True:
        status, report_key, devices, description, failed_cmds, end_code = check_test_status(execution_id)
        if status is None:
            duration = time.time() - start_time
            generate_junit_xml(TEST_NAME, "failed", test_grid_report_url, None, reason="Could not fetch status", duration_seconds=duration)
            sys.exit(1)

        print(f"Current status: {status}", flush=True)
        print(f"Description: {description}", flush=True)
        print(f"Failed Commands: {failed_cmds}", flush=True)

        device_name = devices[0].get("deviceName") if devices else None

        if status.lower() in ['completed', 'failed', 'stopped']:
            duration = time.time() - start_time
            result = "failed" if failed_cmds and failed_cmds > 0 else "passed"
            generate_junit_xml(TEST_NAME, result, test_grid_report_url, device_name, reason=description, duration_seconds=duration, end_code=end_code)
            sys.exit(0 if result == "passed" else 1)

        time.sleep(10)

if __name__ == "__main__":
    main()