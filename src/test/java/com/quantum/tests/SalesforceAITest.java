package com.quantum.tests;

import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import java.util.Map;

public class SalesforceAITest implements ITest {

    private QAFWebDriver driver;
    private String currentTestName;

    @BeforeMethod
    public void initDriver() {
        driver = new WebDriverTestBase().getDriver();
    }

    @Test
    public void TC56_loginSearchValidateCompany() {
        String accountName = "Pyramid Construction Inc.";
        currentTestName = "TC56 - Search and Validate [" + accountName + "]";

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "use a browser to go to url https://perforce-dev-ed.develop.my.salesforce.com/ ,login as the user salesexec@perforce.com with the password P4Demo123, click login"));
  
    }

    @Override
    public String getTestName() {
        return currentTestName;
    }
}