package com.quantum.tests;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class CustomJUnitListener implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try (FileWriter writer = new FileWriter(outputDirectory + "/custom-junit-report.xml")) {
            writer.write("<testsuites>\n");

            for (ISuite suite : suites) {
                Map<String, ISuiteResult> results = suite.getResults();
                for (ISuiteResult sr : results.values()) {
                    List<ITestResult> passedTests = sr.getTestContext().getPassedTests().getAllResults().stream().toList();
                    List<ITestResult> failedTests = sr.getTestContext().getFailedTests().getAllResults().stream().toList();

                    writeResults(writer, passedTests, "passed");
                    writeResults(writer, failedTests, "failure");
                }
            }

            writer.write("</testsuites>\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeResults(FileWriter writer, List<ITestResult> results, String status) throws Exception {
        for (ITestResult tr : results) {
            Object instance = tr.getInstance();
            String testName = (instance instanceof org.testng.ITest) ?
                    ((org.testng.ITest) instance).getTestName() : tr.getMethod().getMethodName();

            writer.write(String.format(
                    "<testcase classname=\"%s\" name=\"%s\" time=\"%.2f\">\n",
                    tr.getTestClass().getName(),
                    testName,
                    (tr.getEndMillis() - tr.getStartMillis()) / 1000.0));

            if ("failure".equals(status)) {
                Throwable t = tr.getThrowable();
                writer.write(String.format(
                        "<failure message=\"%s\" type=\"%s\"><![CDATA[%s]]></failure>\n",
                        t.getMessage(),
                        t.getClass().getName(),
                        t.toString()));
            }

            writer.write("</testcase>\n");
        }
    }
}