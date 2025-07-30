import requests
import time
import os
import xml.etree.ElementTree as ET
import json
import sys

# Ensure immediate output flushing for Jenkins
sys.stdout.reconfigure(line_buffering=True)

# Load environment variables
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
    url = (
        f'https://{perfecto_cloud}/services/executions'
        f'?operation=execute&scriptKey={script_key}&securityToken={PerfectoKey}'
        f'&output.visibility=public&param.PERFECTOTESTURL={PERFECTOTESTURL}&param.symptom={symptom}'
    )
    headers = {'Content-Type': 'application/json'}
    print(f"📡 Sending request to start test: {url}")
    response = requests.post(url, headers=headers)

    if response.status_code == 200:
        try:
            response_json = response.json()
            print("✅ Test initiation response:")
            print(response_json)
            return (
                response_json.get('executionId'),
                response_json.get('reportKey'),
                response_json.get('testGridReportUrl'),
                response_json.get('singleTestReportUrl')
            )
        except ValueError:
            print("❌ Could not parse JSON response:")
            print(response.content)
            return None, None, None, None
    else:
        print("❌ Error starting test:")
        print(f"Status Code: {response.status_code}")
        print(response.text)
        return None, None, None, None

# Poll test status
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

# Get device details from Perfecto


# Get device details
def get_device_details(device_id):
    url = f'https://{perfecto_cloud}/api/v1/device-management/devices/{device_id}'
    headers = { 'Perfecto-Authorization': PerfectoKey }
    try:
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        data = response.json()
        handset = data.get("handset", {})

        print("✅ Raw JSON response from Perfecto device API:", flush=True)
        print(json.dumps(data, indent=2), flush=True)

        debug_info = {
            "Device_Tested": f"{handset.get('manufacturer', '')} {handset.get('model', '')}".strip(),
            "OS": handset.get("os", ""),
            "OS_Version": handset.get("osVersion", ""),
            "Resolution": handset.get("resolution", ""),
            "Location": handset.get("location", ""),
            "Network": handset.get("operator", {}).get("name", "")
        }

        print("\n🔍 Extracted fields for XML generation:", flush=True)
        for key, value in debug_info.items():
            print(f"{key}: {value}", flush=True)

        return handset
    except requests.exceptions.RequestException as e:
        print(f"❌ Failed to fetch device details for {device_id}: {e}", flush=True)
        return {}

# Generate JUnit XML result
def generate_junit_xml(test_name, result, test_grid_report_url, device_id, reason=None, duration_seconds=0.0):
    if not os.path.exists(RESULT_DIR):
        os.makedirs(RESULT_DIR)

    device_info = get_device_details(device_id) if device_id else {}
    device_tested = f"{device_info.get('manufacturer', '')} {device_info.get('model', '')}".strip()

    testcase_attrs = {
        "classname": "PerfectoTest",
        "name": test_name,
        "time": f"{duration_seconds:.3f}",
        "Perfecto_Test_URL": test_grid_report_url or "",
        "Device_Tested": device_tested,
        "OS": device_info.get("os", ""),
        "OS_Version": device_info.get("osVersion", ""),
        "Resolution": device_info.get("resolution", ""),
        "Location": device_info.get("location", ""),
        "Network": device_info.get("operator", {}).get("name", "")
    }

    testsuite = ET.Element("testsuite", name="Perfecto Test Suite", tests="1", failures="0" if result == "passed" else "1", errors="0", skipped="0", time=f"{duration_seconds:.3f}")
    testcase = ET.SubElement(testsuite, "testcase", attrib=testcase_attrs)

    if result != "passed":
        failure_message = f"Test failed. Reason: {reason}" if reason else "Test failed."
        ET.SubElement(testcase, "failure", message=failure_message)

    ET.SubElement(testcase, "system-out").text = f"Full Report: {test_grid_report_url or 'Not available'}"

    tree = ET.ElementTree(testsuite)
    tree.write(RESULT_FILE, encoding="utf-8", xml_declaration=True)
    print(f"📄 JUnit result saved to {RESULT_FILE}", flush=True)

# Main

def main():
    start_time = time.time()
    execution_id, report_key, test_grid_report_url, single_test_report_url = start_test()
    if execution_id is None:
        generate_junit_xml(TEST_NAME, "failed", "N/A", None, reason="Failed to start test.", duration_seconds=0.0)
        exit(1)

    print("🕒 Test execution started with ID:", execution_id, flush=True)

    while True:
        status, flow_end_code, report_key, reason, devices = check_test_status(execution_id)
        if status is None:
            duration = time.time() - start_time
            generate_junit_xml(TEST_NAME, "failed", test_grid_report_url, None, reason="Could not fetch status", duration_seconds=duration)
            exit(1)

        print("Current status:", status, flush=True)
        print("Flow End Code:", flow_end_code, flush=True)

        if status.lower() in ['completed', 'failed', 'stopped']:
            duration = time.time() - start_time
            device_id = devices[0].get('deviceId') if devices else None

            if flow_end_code == 'Failed':
                generate_junit_xml(TEST_NAME, "failed", test_grid_report_url, device_id, reason, duration_seconds=duration)
                exit(1)
            else:
                generate_junit_xml(TEST_NAME, "passed", test_grid_report_url, device_id, reason, duration_seconds=duration)
                exit(0)

        time.sleep(10)

if __name__ == "__main__":
    main()