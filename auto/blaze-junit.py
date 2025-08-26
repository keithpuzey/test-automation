import re
import json
import sys
import xml.etree.ElementTree as ET


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

    # Extract Jenkins build result
    m = re.search(r"Build step .* changed build result to (\w+)", log)
    if m:
        build_status = m.group(1).strip()

    # Build JUnit XML
    testsuite = ET.Element("testsuite", {
        "errors": "0",
        "failures": "1" if status == "FAILED" else "0",
        "name": "BlazeMeter Test Suite",
        "skipped": "0",
        "tests": "1",
        "time": "0.000"
    })

    testcase = ET.SubElement(testsuite, "testcase", {
        "classname": "BlazeMeterTest",
        "name": test_name,
        "time": "0.000"
    })

    # Add failure if exists
    if failure_message:
        ET.SubElement(testcase, "failure", {
            "message": "Test failed. Reason: " + failure_message
        })

    # Add <properties> with report + metrics + build status
    props = ET.SubElement(testcase, "properties")

    ET.SubElement(props, "property", {
        "name": "Blazemeter_Report_URL",
        "value": public_report
    })

    ET.SubElement(props, "property", {
        "name": "Jenkins_Build_Status",
        "value": build_status
    })

    for key, value in agg_report.items():
        ET.SubElement(props, "property", {
            "name": key,
            "value": str(value)
        })

    # Add system-out (for reference)
    sysout = ET.SubElement(testcase, "system-out")
    sysout.text = f"Full Report: {public_report}\nAggregate Report: {json.dumps(agg_report)}\nBuild Status: {build_status}"

    # Pretty print XML
    tree = ET.ElementTree(testsuite)
    tree.write(junit_out, encoding="utf-8", xml_declaration=True)
    print(f"JUnit report written to {junit_out}")


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python parse_jenkins.py <jenkins_log.txt> <output_junit.xml>")
        sys.exit(1)

    parse_jenkins_log(sys.argv[1], sys.argv[2])