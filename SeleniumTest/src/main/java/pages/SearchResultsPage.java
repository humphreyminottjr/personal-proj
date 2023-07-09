package pages;

import java.util.ArrayList; 
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utility.Actions;

public class SearchResultsPage extends Actions{

	CartPage cartPage;
	
	//locators - only the important ones for this exercise
	private By searchTitle = By.xpath("//h1[@class='page-header search--title']//span");
	private By results = By.xpath("//*[@id='ProductBoxContainer']/div[1]/a/span");
	private By paginationLastPage = By.xpath("(//div[@id='paging'])//li[(last() - 1)]//a");
	private By cartIcon = By.xpath("//span[@id='cartItemCountSpan']/preceding-sibling::span");
	private By notificationPopUp = By.xpath("//div[@data-role='notification']");
	private By notificationPopUpViewCart = By.xpath("//div[@data-role='notification']//a[text() = 'View Cart']");
	private By notificationPopUpClose = By.xpath("//div[@data-role='notification']//button");
	
	public SearchResultsPage(WebDriver driver) {
		super(driver);
		cartPage = new CartPage(driver);
		System.out.println("\n initialized the Search Results Page");
	}
	
	/**
	 * This method confirms that we are on the search results page
	 * @param seachValue - the value entered that brought about the search results page
	 * @return
	 */
	public boolean checkIfOnThisPageWithSearchValue(String seachValue) {
		waitForPageToLoad();
		if(checkIfElementContainsText(searchTitle,seachValue) && checkIfCurrentPageTitleIs(seachValue + " - WebstaurantStore")) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return the list of all product names found from search query
	 * @throws Exception
	 */
	public List<String> getAllSearchResultItemNames() throws Exception{
		List<String> items = new ArrayList<>();
		if(isThereAtLeastOneResult()) {
			Integer numOfPages = getTotalPaginationCount();
			
			//loop through each page in search results and gather the names of the products into 'items' List
			for(int i = 0; i < numOfPages; i++) {
				System.out.println("\n On page num "+(i + 1)+" of Search Results page");
				for(WebElement el : getElements(results)) {
					items.add(el.getAttribute("innerHTML"));
				}
				//if not on the last page, then click next page - if the click fails, then throw an exception (unable to proceed)
				if((i != numOfPages - 1) && !click(By.xpath("(//div[@id='paging'])//li//a[text()[contains(., '"+(i+2)+"')]]"))) {
					throw new Exception("unable to click next page in search results page! There is a total of " + numOfPages + "pages!");
				}
			}
			return items;
		}
		return items;
	}
	
	/**
	 * @return the total number of pages in search results
	 */
	public Integer getTotalPaginationCount() {
		String totalPages = getInnerHTMLText(paginationLastPage);
		if(totalPages == null) {
			return -1;
		}
		try {
			return Integer.parseInt(totalPages);
		} catch(Exception e) {
		}
		return -1;
	}
	
	/**
	 * @return checks to see if the search query returned at least 1 item on the results page
	 */
	public boolean isThereAtLeastOneResult() {
		return checkIfPageContainsLocator(results);
	}
	
	/**
	 * This method adds a product from the search results page to cart, given the productName (title)
	 * @param productName
	 * @return
	 */
	public boolean addProductFromResultsToCart(String productName) {
		System.out.println("\n adding product with the title: '"+productName+"' to the cart");
		if(checkIfPageContainsLocator(By.xpath("//*[@id='ProductBoxContainer']/div[1]/a/span[contains(text(),'"+productName+"')]"))) {
			
			//Dynamic Xpath to get the add-cart-bttn based on a given product name in the search results list...
			By addToCartBttn = By.xpath("//*[@id='ProductBoxContainer']/div[1]/a/span[contains(text(),'"+productName+"')]"
					+ "//ancestor::div[1]/following-sibling::div[contains(@class, 'add-to-cart')]//div");
			return click(addToCartBttn);
		}
		return false;
	}
	
	public boolean navigateToShoppingCart() {
		if(checkIfPageContainsLocator(notificationPopUp)) {
			clickAndWait(notificationPopUpClose,3);
		}
		return click(cartIcon);
	}
	
	public boolean navigateToShoppingCartUsingPopUp() {
		return click(notificationPopUpViewCart);
	}
	
	public boolean navigateToShoppingCartAndRemoveProduct(String name) {
		navigateToShoppingCart();
		return cartPage.removeProductFromCart(name);
	}
	

}
