package com.quantum.tests;

import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class SalesforceAITest implements ITest {

    private QAFWebDriver driver;
    private String currentTestName;

    @BeforeMethod
    public void initDriver() {
        driver = new WebDriverTestBase().getDriver();
    }

    @DataProvider(name = "accountProvider")
    public Object[][] getAccounts() throws Exception {
        // Use file system path to avoid classpath issues
        Iterator<Object[]> csvData = CSVUtils.readCSV("src/test/resources/accounts.csv");
        List<Object[]> list = new ArrayList<>();
        csvData.forEachRemaining(list::add);
        return list.toArray(new Object[0][]);
    }

    @Test(dataProvider = "accountProvider")
    public void loginSearchValidateCompany(String accountName) {
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
        return currentTestName;
    }
}