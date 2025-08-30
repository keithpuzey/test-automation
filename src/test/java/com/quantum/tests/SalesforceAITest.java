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


// Corrected class declaration
public class SalesforceAITest extends WebDriverTestBase implements ITest { 

    private QAFWebDriver driver;
    private String currentTestName;

    @BeforeMethod
    public void setupDriver() {
        // The getDriver() method is available because the class extends WebDriverTestBase
        driver = getDriver(); 
    }

    @Test
    public void TC56_loginSearchValidateCompany() {
        String accountName = "Pyramid Construction Inc.";
        currentTestName = "TC56 - Search and Validate";

        Reporter.log("Step 1: Launching the app", true);
        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "go to https://perforce-dev-ed.develop.my.salesforce.com/and wait for the login page to be displayed"));
        Reporter.log("Step 2: Logging in", true);
        	driver.executeScript("perfecto:ai:user-action",
        	    Map.of("action", "login as the user salesexec@perforce.com with the password P4Demo123 and wait for the salesforce page to be displayed."));
            Reporter.log("Step 3: Validate", true);
        	driver.executeScript("perfecto:ai:validation",
           	    Map.of("validation", "Screen shows salesforce dashboard"));
            Reporter.log("Step 4: Logout", true);
        	driver.executeScript("perfecto:ai:user-action",
            	    Map.of("action", "logout using avatar and close device"));
        	
    }

    @Override
    public String getTestName() {
        return currentTestName;
    }
}