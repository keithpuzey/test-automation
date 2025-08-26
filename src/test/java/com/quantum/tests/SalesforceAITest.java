package com.quantum.tests;

import org.testng.annotations.Test;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import java.util.Map;

public class SalesforceAITest {

    private QAFWebDriver driver = new WebDriverTestBase().getDriver();

    @Test
    public void loginSearchValidateCompany() {
        // Step 1: Open Salesforce login page using AI
        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "use a browser to go to url https://perforce-dev-ed.develop.my.salesforce.com/"));

        // Step 2: Login using AI
        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "login as the user salesexec@perforce.com with the password P4Demo123"));

        // Step 3: Search for a company using AI
        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "search for account Pyramid Construction Inc."));

        // Step 4: Validate the correct screen is displayed using AI validation
        driver.executeScript("perfecto:ai:validation",
                Map.of("validation", "Screen shows details for company Pyramid Construction Inc."));
    }
}