package com.quantum.steps;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qmetry.qaf.automation.step.QAFTestStepProvider;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.quantum.pages.GooglePage;
import com.quantum.utils.DeviceUtils;
import com.quantum.utils.DriverUtils;
import com.quantum.utils.ReportUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Keys;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.ui.WebDriverTestBase;


@QAFTestStepProvider

public class GoogleStepDefs {

	GooglePage googlePage = new GooglePage();

	@Given("^I am on Google Search Page$")
	public void I_am_on_Google_Search_Page() throws Throwable {
		
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("label", "Continue");
			params.put("timeout", "5");
			params.put("threshold", "95");
			DeviceUtils.getQAFDriver().executeScript("mobile:button-text:click", params);
		} catch (Exception e) { }
		
		DeviceUtils.getQAFDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		
		if(!DriverUtils.isAndroid() && !DriverUtils.isIOS()) {
			DeviceUtils.getQAFDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(100));
		}
		
		
		System.out.println(DriverUtils.getDriver().getCapabilities().getCapability("browserName"));
		System.out.println(DriverUtils.getDriver().getCapabilities().getBrowserName());
		
		new WebDriverTestBase().getDriver().get("https://www.google.com/");
	}

	@When("^I search for \"([^\"]*)\"$")
	public void search(String searchKey) {
    WebDriver driver = new WebDriverTestBase().getDriver();
    WebElement searchBox = driver.findElement(By.name("q"));
    searchBox.sendKeys(searchKey + Keys.ENTER);
    }

	@Then("^it should have \"([^\"]*)\" in search results$")
	public void it_should_have_in_search_results(String result) throws Throwable {
		googlePage.verifyResult(result);
	}

	@Then("^it should have following search results:$")
	public void it_should_have_all_in_search_results(List<String> results) {
		googlePage.verifyResult(results);
	}
	
	@Given("^I have the following books in the store:$")
	public void iHaveTheFollowingBooksInTheStore(List<Map<Object,Object>> dataList) {
		for(Map<Object, Object> dataMap : dataList) {
			for (Map.Entry<Object, Object> entry : dataMap.entrySet()) {
				System.out.println(entry.getKey().toString()+"*************"+ entry.getValue().toString());
				ReportUtils.logAssert(entry.getKey().toString()+"*************"+ entry.getValue().toString(), true);
			}
		}
	}
}