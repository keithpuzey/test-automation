package com.quantum.steps;

import org.openqa.selenium.Keys;

import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class SearchDoctorSteps {

    @Given("I navigate to the TopDoctors site")
    public void goToTopDoctors() {
        new WebDriverTestBase().getDriver().get("https://www.topdoctors.co.uk/doctor/");
    }

    @When("I search for {string}")
    public void searchDoctor(String doctorName) {
        QAFWebDriver driver = new WebDriverTestBase().getDriver();
        QAFWebElement searchInput = driver.findElement("css=input[aria-label='Specialty / illness / doctor name']");
        searchInput.clear();
        searchInput.sendKeys(doctorName);
        searchInput.sendKeys(Keys.ENTER);
    }

    @Then("I wait between searches")
    public void waitBetweenSearches() {
        try {
            Thread.sleep(5000); // wait 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}