package com.quantum.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import java.util.Map;

public class SalesforceAITest {

    private QAFWebDriver driver;

    @BeforeMethod
    public void initDriver() {
        // Create a WebDriverTestBase and get the driver from it
        driver = new WebDriverTestBase().getDriver();
    }

    @Test
    public void loginSearchValidateCompany() {
        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "use a browser to go to url https://perforce-dev-ed.develop.my.salesforce.com/"));

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "login as the user salesexec@perforce.com with the password P4Demo123"));

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "search for account Pyramid Construction Inc."));

        driver.executeScript("perfecto:ai:validation",
                Map.of("validation", "Screen shows details for company Pyramid Construction Inc."));
    }
}