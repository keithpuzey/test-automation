import requests
import time
import os
import xml.etree.ElementTree as ET


PerfectoKey = os.getenv("PerfectoToken")
if not PerfectoKey:
    raise RuntimeError("❌ Environment variable 'PerfectoToken' is not set.")

Perfectotest = os.getenv("PerfectoTest")
if not Perfectotest:
    raise RuntimeError("❌ Environment variable 'Perfectotest' is not set.")

Perfectotestname = os.getenv("PerfectoTestname")
if not Perfectotestname:
    raise RuntimeError("❌ Environment variable 'Perfectotestname' is not set.")


PERFECTOTESTURL = os.getenv("PERFECTOTESTURL")
if not PERFECTOTESTURL:
    raise RuntimeError("❌ Environment variable 'PERFECTOTESTURL' is not set.")

symptom = os.getenv("symptom")
if not symptom:
    raise RuntimeError("❌ Environment variable 'symptom' is not set.")



# Config
perfecto_cloud = 'web-demo-fra.perfectomobile.com'
script_key = Perfectotest
RESULT_DIR = "test-results"
RESULT_FILE = os.path.join(RESULT_DIR, "perfecto-result.xml")
TEST_NAME = Perfectotestname

# Start the Perfecto script execution
def start_test():
    url = f'https://{perfecto_cloud}/services/executions?operation=execute&scriptKey={script_key}&securityToken={PerfectoKey}&output.visibility=public&param.PERFECTOTESTURL={PERFECTOTESTURL}&param.symptom={symptom}'
    headers = {'Content-Type': 'application/json'}

    response = requests.post(url, headers=headers)
    if response.status_code == 200:
        try:
            response_json = response.json()
            execution_id = response_json.get('executionId')
            report_key = response_json.get('reportKey')
            test_grid_report_url = response_json.get('testGridReportUrl')
            single_test_report_url = response_json.get('singleTestReportUrl')
            print("Single Test Report:", single_test_report_url)
            return execution_id, report_key, test_grid_report_url, single_test_report_url
        except KeyError:
            print("❌ Error parsing response:")
            print(response.content)
            return None, None, None, None
    else:
        print("❌ Error starting test:", response.text)
        return None, None, None, None

# Poll until the test completes
def check_test_status(execution_id):
    url = f'https://{perfecto_cloud}/services/executions/{execution_id}?operation=status&securityToken={PerfectoKey}'
    try:
        response = requests.get(url)
        response.raise_for_status()
        json_data = response.json()
        return (
            json_data.get('status'),
            json_data.get('flowEndCode'),
            json_data.get('reportKey'),
            json_data.get('reason'),
            json_data.get('devices')
        )
    except requests.exceptions.RequestException as e:
        print("❌ Error checking test status:", e)
        return None, None, None, None, None

# Create a JUnit XML result file with duration
def generate_junit_xml(test_name, result, report_url, reason=None, duration_seconds=0.0):
    if not os.path.exists(RESULT_DIR):
        os.makedirs(RESULT_DIR)

    testsuite = ET.Element(
        "testsuite",
        name="Perfecto Test Suite",
        tests="1",
        failures="0" if result == "passed" else "1",
        errors="0",
        skipped="0",
        time=f"{duration_seconds:.3f}"
    )

    testcase = ET.SubElement(
        testsuite,
        "testcase",
        classname="PerfectoTest",
        name=f"{test_name}",
        time=f"{duration_seconds:.3f}"
    )

    if result != "passed":
        failure_message = f"Test failed. Reason: {reason}" if reason else "Test failed."
        ET.SubElement(testcase, "failure", message=failure_message)

    ET.SubElement(testcase, "system-out").text = f"Full Report: {report_url}"

    tree = ET.ElementTree(testsuite)
    tree.write(RESULT_FILE, encoding="utf-8", xml_declaration=True)
    print(f"📄 JUnit result saved to {RESULT_FILE}")

# Main function
def main():
    start_time = time.time()
    execution_id, report_key, test_grid_report_url, single_test_report_url = start_test()
    if execution_id is None:
        print("❌ Failed to start test.")
        generate_junit_xml(TEST_NAME, "failed", "N/A", reason="Failed to start test.", duration_seconds=0.0)
        exit(1)

    print("🕒 Test execution started with ID:", execution_id)

    while True:
        status, flow_end_code, report_key, reason, devices = check_test_status(execution_id)
        if status is None:
            print("❌ Could not get test status.")
            duration = time.time() - start_time
            generate_junit_xml(TEST_NAME, "failed", single_test_report_url, reason="Could not fetch status", duration_seconds=duration)
            exit(1)

        print("Current status:", status)
        print("Flow End Code:", flow_end_code)

        if status.lower() in ['completed', 'failed', 'stopped']:
            duration = time.time() - start_time
            print("✅ Test execution completed.")

            if flow_end_code == 'Failed':
                print("Test failed.")
                print("Reason:", reason)
                print("Devices:", devices)
                print("Report:", single_test_report_url)
                generate_junit_xml(TEST_NAME, "failed", single_test_report_url, reason, duration_seconds=duration)
                exit(1)
            else:
                print("Test passed.")
                print("Reason:", reason)
                print("Devices:", devices)
                print("Report:", single_test_report_url)
                generate_junit_xml(TEST_NAME, "passed", single_test_report_url, duration_seconds=duration)
                exit(0)

        time.sleep(10)

if __name__ == "__main__":
    main()