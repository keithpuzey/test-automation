package com.quantum.tests;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class StepLoggingListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // Not needed here
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // If the method is a test method, flush Reporter logs to JUnit XML
        if (method.isTestMethod()) {
            for (String log : Reporter.getOutput(testResult)) {
                testResult.setAttribute("step-log", log); // store as attribute
            }
        }
    }
}