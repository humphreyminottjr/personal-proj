package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utility.Actions;

public class StoreHomePage extends Actions {

	// locators - only the important ones for this exercise
	private By searchBox = By.id("searchval");

	public StoreHomePage(WebDriver driver) {
		super(driver);
	}

	public void go() {
		go("https://www.webstaurantstore.com/");
	}

	public boolean checkIfOnThisPage() {
		waitForPageToLoad();
		return checkIfCurrentPageTitleIs("WebstaurantStore: Restaurant Supplies & Foodservice Equipment");
	}

	public boolean searchFor(String searchValue) {
		return sendKeysToAndHitEnter(searchBox, searchValue);
	}

}
