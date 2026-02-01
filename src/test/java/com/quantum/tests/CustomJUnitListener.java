package com.quantum.tests;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.Reporter;

import java.util.List;

public class CustomJUnitListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult tr) {
        logStepDetails(tr);
        super.onTestSuccess(tr);
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        logStepDetails(tr);
        super.onTestFailure(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        logStepDetails(tr);
        super.onTestSkipped(tr);
    }

    private void logStepDetails(ITestResult tr) {
        List<String> logs = Reporter.getOutput(tr);
        if (!logs.isEmpty()) {
            StringBuilder sb = new StringBuilder("\n--- Step Logs ---\n");
            for (String log : logs) {
                sb.append(log).append("\n");
            }
            sb.append("-----------------\n");

            // Attach the step logs so they appear in the JUnit XML <system-out>
            tr.setAttribute("step-logs", sb.toString());

        }
    }
}