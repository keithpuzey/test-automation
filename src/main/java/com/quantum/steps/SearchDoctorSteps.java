package com.quantum.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;

public class SearchDoctorSteps {

    @QAFTestStep(description = "I navigate to the TopDoctors site")
    public void goToTopDoctors() {
        new WebDriverTestBase().getDriver().get("https://www.topdoctors.co.uk/doctor/");
    }

    @QAFTestStep(description = "I search for {0}")
    public void searchDoctor(String doctorName) {
        QAFWebDriver driver = new WebDriverTestBase().getDriver();
        WebDriver seleniumDriver = (WebDriver) driver.getWrappedDriver(); // safest

        WebDriverWait wait = new WebDriverWait(seleniumDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#input-specialty > div > input")));

        QAFWebElement searchInput = driver.findElement("css=#input-specialty > div > input");
        searchInput.clear();
        searchInput.sendKeys(doctorName);
        searchInput.sendKeys(Keys.ENTER);
    }

    @QAFTestStep(description = "I wait between searches")
    public void waitBetweenSearches() {
        try {
            Thread.sleep(5000); // Can also use QAF’s wait mechanism if preferred
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}