package com.quantum.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class SearchDoctorSteps {

    private WebDriver driver;

    public SearchDoctorSteps(Hooks hooks) {
        this.driver = hooks.getDriver();
    }

    @Given("I navigate to the TopDoctors site")
    public void goToTopDoctors() {
        driver.get("https://www.topdoctors.co.uk/doctor/");
    }

    @When("I search for {string}")
    public void searchDoctor(String doctorName) {
        WebElement searchInput = driver.findElement(By.cssSelector("input[aria-label='Specialty / illness / doctor name']"));
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