package com.quantum.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Map;
import java.time.Duration;
import java.util.HashMap;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.core.MessageTypes;

public class UpdateCompany {

	
    @QAFTestStep(description = "I navigate to the Salesforce Site and login as salesexec")
    public void goToSalesForce() {
           QAFWebDriver driver = new WebDriverTestBase().getDriver();

        driver.executeScript("perfecto:ai:user-action",
            Map.of("action", "use a browser to go to url https://perforce-dev-ed.develop.my.salesforce.com/ "));
            Map.of("action", "login as the users salesexec@perforce.com with the password P4Demo123");
    		
    }


    @QAFTestStep(description = "I search for {0}")
    public void searchCompany(String companyName) {
           QAFWebDriver driver = new WebDriverTestBase().getDriver();
        
        driver.executeScript("perfecto:ai:user-action", Map.of("action", "search for account" + companyName ));
    }

    @QAFTestStep(description = "I validate screen contains {0}")
    public void waitBetweenSearches(String companyName ) {
  
               QAFWebDriver driver = new WebDriverTestBase().getDriver();    
        driver.executeScript("perfecto:ai:validation", Map.of("validation", "Screen shows details for cmpany" + companyName   ));
     }

    /**
     * Returns a Perfecto report URL for each step.
     * Replace this logic with your actual URL retrieval.
     */
    private String getReportUrlForStep(String stepName) {
        // TODO: Replace this with real logic, e.g., lookup from config, environment, or test context
        return "https://perfecto.report/step/" + stepName;
    }
}
