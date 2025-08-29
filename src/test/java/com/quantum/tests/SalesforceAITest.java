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
        currentTestName = "TC56 - Search and Validate [" + accountName + "]";

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "go to https://perforce-dev-ed.develop.my.salesforce.com/ "));

        	driver.executeScript("perfecto:ai:user-action",
        	    Map.of("action", "login as the user salesexec@perforce.com with the password P4Demo123"));

        	driver.executeScript("perfecto:ai:validation",
            	    Map.of("validation", "Screen shows salesforce dashboard"));
        	
        	driver.executeScript("perfecto:ai:user-action",
            	    Map.of("action", "logout using avatar"));
        	
    }

    @Override
    public String getTestName() {
        return currentTestName;
    }
}