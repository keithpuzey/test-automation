package com.quantum.steps;

import org.openqa.selenium.Keys;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class SearchDoctorSteps {

    @Given("^I navigate to the TopDoctors site$")
    public void goToTopDoctors() {
        // ✅ Check if file is available on the classpath
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/doctorNames.csv")) {
            if (is == null) {
                throw new RuntimeException("❌ CSV file NOT found in classpath: data/doctorNames.csv");
            } else {
                System.out.println("✅ CSV file FOUND in classpath: data/doctorNames.csv");

                // Optional: Print contents of the file
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("CSV Line: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new WebDriverTestBase().getDriver().get("https://www.topdoctors.co.uk/doctor/");
    }

    @When("^I search for \"([^\"]*)\"$")
    public void searchDoctor(String doctorName) {
        QAFWebDriver driver = new WebDriverTestBase().getDriver();

        QAFWebElement searchInput = driver.findElement("css=input[aria-label='Specialty / illness / doctor name']");
        searchInput.clear();
        searchInput.sendKeys(doctorName);
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