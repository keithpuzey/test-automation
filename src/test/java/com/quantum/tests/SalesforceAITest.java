package com.quantum.tests;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import com.qmetry.qaf.automation.core.DriverFactory;
import com.qmetry.qaf.automation.ui.WebDriverTestBase; // Correct import
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import java.net.MalformedURLException;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.Assert;



// Corrected class declaration
public class SalesforceAITest extends WebDriverTestBase implements ITest { 

    private QAFWebDriver driver;
    private String currentTestName;

private void checkStepResult(Object result, String stepName) {
    if (!(result instanceof Boolean && (Boolean) result)) {
        Assert.fail("Step failed: " + stepName + " → Result: " + result);
    }
}

    @BeforeMethod
    public void setupDriver() {
        // The getDriver() method is available because the class extends WebDriverTestBase
        driver = getDriver(); 
    }

@Test
public void TC56_loginSearchValidateCompany() {
    currentTestName = "TC56 - Search and Validate";

    Reporter.log("Launching Browser and Navigate to salesforce.com site.", true);
    Object step1 = driver.executeScript("perfecto:ai:user-action",
            Map.of("action", "go to https://perforce-dev-ed.develop.my.salesforce.com/ and login as the user salesexec@perforce.com with the password P4Demo123 and wait for the salesforce page to be displayed"));
    Reporter.log(" → Result: " + step1, true);
    checkStepResult(step1, "Launching the app");

    Reporter.log("SalesForce Login", true);
    Object step2 = driver.executeScript("perfecto:ai:user-action",
            Map.of("action", "login as the user salesexec@perforce.com with the password P4Demo123 and wait for the salesforce page to be displayed."));
    Reporter.log(" → Result: " + step2, true);
    checkStepResult(step2, "Logging in");

    Reporter.log("Validate dashboard", true);
    Object step3 = driver.executeScript("perfecto:ai:validation",
            Map.of("validation", "Screen shows salesforce dashboard"));
    Reporter.log(" → Result: " + step3, true);
    checkStepResult(step3, "Validate");

    Reporter.log("Step 4: Logout", true);
    Object step4 = driver.executeScript("perfecto:ai:user-action",
            Map.of("action", "logout using avatar and close device"));
    Reporter.log(" → Result: " + step4, true);
    checkStepResult(step4, "Logout");
}

    @Override
    public String getTestName() {
        return currentTestName;
    }
}