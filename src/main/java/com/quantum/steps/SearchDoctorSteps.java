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
        URL resource = getClass().getClassLoader().getResource("data/doctorNames.csv");
        System.out.println("CSV Path: " + (resource != null ? resource.getPath() : "Not Found"));
        new WebDriverTestBase().getDriver().get("https://www.topdoctors.co.uk/doctor/");
    }

    @When("^I search for \"([^\"]*)\"$")
    public void searchDoctor(String doctorName) {
        QAFWebDriver driver = new WebDriverTestBase().getDriver();

        // Use aria-label to locate the input
        QAFWebElement searchInput = driver.findElement("css=input[aria-label='Specialty / illness / doctor name']");
        searchInput.clear();
        searchInput.sendKeys(doctorName);

        // If pressing ENTER doesn't work, we can also click a search button or wait
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