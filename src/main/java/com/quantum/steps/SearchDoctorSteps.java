package com.quantum.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Map;
import java.time.Duration;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.core.MessageTypes;

public class SearchDoctorSteps {

    @QAFTestStep(description = "I navigate to the TopDoctors site")
    public void goToTopDoctors() {
           QAFWebDriver driver = new WebDriverTestBase().getDriver();

        driver.executeScript("perfecto:ai:user-action",
                Map.of("action", "Open new order screen!"));
    }


    @QAFTestStep(description = "I search for {0}")
    public void searchDoctor(String doctorName) {
           QAFWebDriver driver = new WebDriverTestBase().getDriver();
        
        driver.executeScript("perfecto:ai:user-action", Map.of("action", "Open new order screen!"));
    }

    @QAFTestStep(description = "I wait between searches")
    public void waitBetweenSearches() {
  
               QAFWebDriver driver = new WebDriverTestBase().getDriver();    
        driver.executeScript("perfecto:ai:user-action", Map.of("action", "Open new order screen!"));
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
