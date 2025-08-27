package com.quantum.tests;

import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class SalesforceAITest implements ITest {

    private QAFWebDriver driver;
    private String currentTestName;  // store dynamic test name

    @BeforeMethod
    public void initDriver() {
        driver = new WebDriverTestBase().getDriver();
    }

    @DataProvider(name = "accountProvider")
    public Object[][] getAccounts() throws Exception {
    	Iterator<Object[]> csvData = CSVUtils.readCSV("accounts.csv");
        List<Object[]> list = new ArrayList<>();
        csvData.forEachRemaining(list::add);
        return list.toArray(new Object[0][]);
    }

    @Test(dataProvider = "accountProvider", testName = "TC56 - Search and Validate")
    public void loginSearchValidateCompany(String accountName) {
        // Set the dynamic test name for ITest
        currentTestName = "TC56 - Search and Validate [" + accountName + "]";

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "use a browser to go to url https://perforce-dev-ed.develop.my.salesforce.com/"));

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "login as the user salesexec@perforce.com with the password P4Demo123"));

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "search for account " + accountName));

        driver.executeScript("perfecto:ai:validation",
                Map.of("validation", "Screen shows details for company " + accountName));
    }

    @Override
    public String getTestName() {
        return currentTestName;  // TestNG uses this for JUnit / HTML report
    }
}