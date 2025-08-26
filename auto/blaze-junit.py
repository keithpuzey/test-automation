import re
import json
import sys
import xml.etree.ElementTree as ET
import html  # for escaping special chars

def parse_jenkins_log(log_path, junit_out):
    test_name = "UnknownTest"
    public_report = "N/A"
    agg_report = {}
    status = "PASSED"
    failure_message = None
    build_status = "UNKNOWN"

    with open(log_path, "r") as f:
        log = f.read()

    # Extract test name
    m = re.search(r"Start test id.*name\s*:\s*(.+)", log)
    if m:
        test_name = m.group(1).strip()

    # Extract public report URL
    m = re.search(r"Public test report will be available at (.+)", log)
    if m:
        public_report = m.group(1).strip()

    # Extract aggregated report JSON
    m = re.search(r"Got aggregated report from server\s*\n.*({.*})", log)
    if m:
        try:
            agg_report = json.loads(m.group(1))
        except Exception as e:
            print("Could not parse aggregate report JSON:", e)

    # Extract failure reason
    m = re.search(r"Having failures (.+)", log)
    if m:
        status = "FAILED"
        failure_message = m.group(1).strip()
        failure_message = html.escape(failure_message)

    # Extract Jenkins build result
    m = re.search(r"Build step .* changed build result to (\w+)", log)
    if m:
        build_status = m.group(1).strip()

    # Build attributes for <testcase> (Helix-compatible)
    testcase_attrs = {
        "classname": "BlazeMeterTest",
        "name": test_name,
        "time": "0.000",
        "Blazemeter_Report_URL": public_report,
        "Jenkins_Build_Status": build_status
    }

    # Add aggregated metrics as attributes
    for key, value in agg_report.items():
        testcase_attrs[key] = str(value)

    # Build JUnit XML
    testsuite = ET.Element("testsuite", {
        "errors": "0",
        "failures": "1" if status == "FAILED" else "0",
        "name": "BlazeMeter Test Suite",
        "skipped": "0",
        "tests": "1",
        "time": "0.000"
    })

    testcase = ET.SubElement(testsuite, "testcase", testcase_attrs)

    # Add failure if exists
    if failure_message:
        ET.SubElement(testcase, "failure", {
            "message": f"Test failed. Reason: {failure_message}"
        })

    # Add system-out for reference
    sysout = ET.SubElement(testcase, "system-out")
    sysout.text = f"Full Report: {public_report}\nAggregate Report: {json.dumps(agg_report)}\nBuild Status: {build_status}"

    # Write XML
    tree = ET.ElementTree(testsuite)
    tree.write(junit_out, encoding="utf-8", xml_declaration=True)
    print(f"✅ JUnit report written to {junit_out}")


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python parse_jenkins.py <jenkins_log.txt> <output_junit.xml>")
        sys.exit(1)

    parse_jenkins_log(sys.argv[1], sys.argv[2])