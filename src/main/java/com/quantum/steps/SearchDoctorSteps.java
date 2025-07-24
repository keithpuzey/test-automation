package com.quantum.steps;
import org.openqa.selenium.Keys;

import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class SearchDoctorSteps {

    @Given("^I navigate to the TopDoctors site$")
    public void goToTopDoctors() {
        new WebDriverTestBase().getDriver().get("https://www.topdoctors.co.uk/doctor/");
    }

    @When("^I search for \"([^\"]*)\"$")
    public void searchDoctor(String doctorName) {
        QAFWebDriver driver = new WebDriverTestBase().getDriver();

        QAFWebElement searchInput = driver.findElement("css=input[name='name']");
        searchInput.clear();
        searchInput.sendKeys(doctorName);

        // Submit the search (modify if needed to trigger the search)
        searchInput.sendKeys(Keys.ENTER);
    }

    @Then("^I wait between searches$")
    public void waitBetween() {
        try {
            Thread.sleep(5000); // 5 second wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}