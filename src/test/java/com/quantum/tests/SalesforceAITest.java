package com.quantum.tests;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;

import java.net.MalformedURLException;
import java.util.Map;

public class SalesforceAITest implements ITest {

    private QAFWebDriver driver;
    private String currentTestName;

    @BeforeMethod
    public void initDriver() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();

        // Perfecto authentication
        caps.setCapability("perfecto:user", "<your-user>");
        caps.setCapability("perfecto:securityToken", "<your-token>");

        // Device selection
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "14");  // appium: prefix is important in W3C
        caps.setCapability("deviceName", ".*"); // regex for any device

        // Browser
        caps.setCapability("browserName", "Chrome");

        driver = new WebDriverTestBase(caps).getDriver();
    }

    @Test
    public void TC56_loginSearchValidateCompany() {
        String accountName = "Pyramid Construction Inc.";
        currentTestName = "TC56 - Search and Validate [" + accountName + "]";

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "login as the user salesexec@perforce.com with the password P4Demo123 using the url https://perforce-dev-ed.develop.my.salesforce.com/ "));
    }

    @Override
    public String getTestName() {
        return currentTestName;
    }
}