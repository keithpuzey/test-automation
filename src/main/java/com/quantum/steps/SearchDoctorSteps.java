package com.quantum.steps;

import org.openqa.selenium.Keys;

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

    // Step 1: Click the placeholder to reveal the input field
    QAFWebElement placeholder = driver.findElement("css=div.mobile-input-placeholder");
    placeholder.click();

    // Step 2: Wait for the input to appear
    WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']")));

    // Step 3: Type the doctor name and press Enter
    QAFWebElement searchInput = (QAFWebElement) driver.findElement("css=input[type='text']");
    searchInput.clear();
    searchInput.sendKeys(doctorName);
    searchInput.sendKeys(Keys.ENTER);
}

    @QAFTestStep(description = "I wait between searches")
    public void waitBetweenSearches() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}