package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utility.Actions;

public class CartPage extends Actions {

	// locators - only the important ones for this exercise
	private By cartIcon = By.xpath("//span[@id='cartItemCountSpan']/preceding-sibling::span");
	private By isCartEmptyText = By.xpath("//*[contains(text(),'Your cart is empty.')]");

	public CartPage(WebDriver driver) {
		super(driver);
		System.out.println("\n initialized the Cart Page");
	}

	/**
	 * navigating to the view cart screen
	 * 
	 * @return
	 */
	public boolean navigateToCartPage() {
		System.out.println("\n Navigating to Cart Page");
		return click(cartIcon);
	}

	/**
	 * whilst on the view cart screen, given a product name this method will remove
	 * that item from cart
	 * 
	 * @param name - the name of product that you want to remove from cart
	 * @return
	 */
	public boolean removeProductFromCart(String name) {
		By deleteFromCart = By.xpath("//li//div[contains(@class, 'cartItem')]//span//a[@title='" + name + "'] "
				+ "//ancestor::li//div[contains(@class, 'cartItem')]//div[@class='itemDelete']//button");

		System.out.println("\n Removing product titled - '"+name+" - from cart'");
		return clickAndWait(deleteFromCart, 20);
	}
	
	/**
	 * Just checking to see if the cart is currently empty or not.
	 * @return boolean true if is is empty, false if it is not empty
	 */
	public boolean isCartEmpty() {
		boolean isCartEmpty = checkIfPageContainsLocator(isCartEmptyText);
		System.out.println("\n Check if the cart is empty --> " + isCartEmpty);
		return isCartEmpty;
	}
}
