package com.quantum.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.core.MessageTypes;

public class SearchDoctorSteps {

    @QAFTestStep(description = "I navigate to the TopDoctors site")
    public void goToTopDoctors() {
        new WebDriverTestBase().getDriver().get("https://www.topdoctors.co.uk/doctor/");

        // Log clickable Perfecto report URL for this step
        String reportUrlForStep = getReportUrlForStep("goToTopDoctors");
        Reporter.log("Perfecto Step Report URL: " + reportUrlForStep, MessageTypes.Info);
    }

    @QAFTestStep(description = "I search for {0}")
    public void searchDoctor(String doctorName) {
        QAFWebDriver driver = new WebDriverTestBase().getDriver();
        WebDriver seleniumDriver = (WebDriver) driver;

        WebDriverWait wait = new WebDriverWait(seleniumDriver, Duration.ofSeconds(10));
        By placeholderLocator = By.xpath("//div[contains(@class,'mobile-input-placeholder')]");
        wait.until(ExpectedConditions.elementToBeClickable(placeholderLocator));
        driver.findElement(placeholderLocator).click();

        // Log clickable Perfecto report URL for this step
        String reportUrlForStep = getReportUrlForStep("searchDoctor");
        Reporter.log("Perfecto Step Report URL: " + reportUrlForStep, MessageTypes.Info);
    }

    @QAFTestStep(description = "I wait between searches")
    public void waitBetweenSearches() {
        try {
            Thread.sleep(5000); // Can also use QAF’s wait mechanism if preferred
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Log clickable Perfecto report URL for this step
        String reportUrlForStep = getReportUrlForStep("waitBetweenSearches");
        Reporter.log("Perfecto Step Report URL: " + reportUrlForStep, MessageTypes.Info);
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