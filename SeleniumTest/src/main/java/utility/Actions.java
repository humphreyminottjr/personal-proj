package utility;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Actions {

	private WebDriver driver;
	private org.openqa.selenium.interactions.Actions action;

	public Actions(WebDriver driver) {
		this.driver = driver;
		this.action = new org.openqa.selenium.interactions.Actions(driver);
	}

	public void go(String url) {
		System.out.println("\n going to URL location: " + url);
		driver.get(url);
	}

	public void waitForPageToLoad() {
		System.out.println("\n Wait for page to load...");
		new WebDriverWait(driver, Duration.ofSeconds(20)).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));
		System.out.println("\n page fully loaded!");
	}

	public boolean click(By element) {
		if (element == null) {
			return false;
		}

		try {
			System.out.println("\n Click element with locator: " + element.toString());
			WebElement foundElement = driver.findElement(element);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", foundElement);
			action.moveToElement(foundElement).click().build().perform();
			return true;
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return false;
		}
	}
	
	public boolean clickAndWait(By element,int seconds) {
		if (element == null) {
			return false;
		}

		try {
			click(element);
			action.wait(seconds * 1000);
			return true;
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return false;
		}
		catch(Exception e) {
			return true;
		}
	}

	public boolean sendKeysTo(By element, String value) {
		if (value == null || element == null) {
			return false;
		}

		try {
			System.out.println("\n send keys value of '" + value + "' to locator: " + element.toString());
			WebElement foundElement = driver.findElement(element);
			action.moveToElement(foundElement).click().sendKeys(value).build().perform();
			return true;
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return false;
		}
	}

	public boolean sendKeysToAndHitEnter(By element, String value) {
		if (value == null || element == null) {
			return false;
		}

		try {
			System.out.println("\n send keys value of '" + value + "' to locator: " + element.toString());
			WebElement foundElement = driver.findElement(element);
			action.moveToElement(foundElement).click().sendKeys(value).sendKeys(Keys.RETURN).build().perform();
			return true;
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return false;
		}
	}

	public boolean checkIfPageContainsLocator(By by) {
		if (by == null) {
			return false;
		}
		WebElement element = driver.findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		try {
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
		}
		return false;
	}

	public boolean checkIfCurrentPageTitleIs(String title) {
		String tileActual = driver.getTitle();
		return tileActual.equalsIgnoreCase(title);
	}

	public boolean checkIfElementContainsText(By by, String text) {
		if (text == null || by == null) {
			return false;
		}

		try {
			String textActual = getInnerHTMLText(by);
			return textActual.equalsIgnoreCase(text);
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return false;
		}
	}

	public String getInnerHTMLText(By by) {
		if (by == null) {
			return null;
		}

		try {
			WebElement element = driver.findElement(by);
			return element.getAttribute("innerHTML");
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return null;
		}
	}

	public WebElement getElement(By by) {
		if (by == null) {
			return null;
		}

		try {
			return driver.findElement(by);
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return null;
		}
	}

	public List<WebElement> getElements(By by) {
		if (by == null) {
			return null;
		}

		try {
			return driver.findElements(by);
		} catch (NoSuchElementException e) {
			System.out.println("Element not found!");
			return null;
		}
	}

}
