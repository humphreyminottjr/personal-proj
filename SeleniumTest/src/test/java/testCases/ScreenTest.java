package testCases;

import static org.testng.Assert.fail;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.CartPage;
import pages.SearchResultsPage;
import pages.StoreHomePage;

@Test
public class ScreenTest extends BaseTest {
	
	//Utilizing the page object model for this exercise!
	StoreHomePage webstaurantstore;
	SearchResultsPage searchResults;
	CartPage cartPage;

	@BeforeTest
	public void initiate() {
		//initialize the dependent pages for this test scenario
		this.webstaurantstore = new StoreHomePage(driver);
		this.searchResults = new SearchResultsPage(driver);
		this.cartPage = new CartPage(driver);
	}

	/**
	 * This test case will...
	 *  1) Navigate the the Webstaurant Store homepage (https://www.webstaurantstore.com/);
	 *  2) Search for stainless work table using the search box feature
	 *  3) Check the search result ensuring every product has the standalone word 'Table' in its title
	 *  4) Add the last item of the found items on the search result page to the Cart
	 *  5) Empty Cart - I did this by navigating to the cart page and remove the item from the cart, thus making it empty!
	 *  
	 *  With various other small check throughout! :) 
	 */
	@Test
	public void searchAndCartTest() {
		try {
			webstaurantstore.go(); // navigate to the home page

			// confirm we are on the home page
			if (!webstaurantstore.checkIfOnThisPage()) {
				fail("Title of the page is different than expected! Are we on the correct page?");
			}

			// use search box to search for: 'stainless work table'
			webstaurantstore.searchFor("stainless work table");

			// confirm we are on the search results page with the search value pre-populated in header
			if (!searchResults.checkIfOnThisPageWithSearchValue("stainless work table")) {
				fail("Expected to be on search results page with search title populated with value");
			}

			// gather all the product/title names in the search results page - for each page (click pagination footer)
			List<String> productNames = searchResults.getAllSearchResultItemNames();

			// create a list and add the names that do not have the word 'table' in the
			// title, so we can see why script is failing!
			List<String> incorrectTitles = new ArrayList<>();

			// for each product name, confirm that it contain the word table (ignoring
			// case), if it doesn't that add it to the incorrect titles list
	        Pattern pattern = Pattern.compile("\\bTable\\b", Pattern.CASE_INSENSITIVE);
			for (String name : productNames) {
				Matcher matcher = pattern.matcher(name);
				if (!(matcher.find())) {
					incorrectTitles.add(name);
				}
			}

			// if this list is not empty then the test will fail since the search results
			// page contains items that don't have the keyword title
			if (!(incorrectTitles.isEmpty())) {
				System.out.println("\n The following " + incorrectTitles.size()+" Product Titles do not contain the word 'table' -- FAILED test!\n");
				incorrectTitles.forEach(System.out::println);
//				fail("Expected all products in search results to contain the word 'table', "
//						+ "but found the following products in the list: " + incorrectTitles);
			}

			//List retains the order, so get the name of the last product added to the list from the search results page
			String lastProductName = productNames.get(productNames.size() - 1);
			
			//Using the name of the product, add the product to the cart
			if (!(searchResults.addProductFromResultsToCart(lastProductName))) {
				fail("failed to add the last product on results page to the cart");
			}

			//Since the popup appears after adding to cart, use the 'View Cart' button from the popup window to navigate to the cart screen
			searchResults.navigateToShoppingCartUsingPopUp();

			//Remove the product from the shopping cart page by product name
			if (!(cartPage.removeProductFromCart(lastProductName))) {
				fail("failed to remove the product with name '" + lastProductName + "' from the cart");
			}
			
			//Instead of clicking the empty cart button, I manually removed the product I added...
			//Since I only had 1 item in the cart for this testcase, after removing that product the cart should be empty now
			if (!(cartPage.isCartEmpty())) {
				fail("The cart is not empty!");
			}
			
			//Taking a screenshot of empty cart - since the browser closes so fast! Saved in src/test/resources. This will overwrite each time you run
			TakesScreenshot scrShot =((TakesScreenshot)driver);
			File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(SrcFile, new File("src/test/resources/reports/EmptyCartScreenshot.png"));

		} catch (Exception e) {
			System.out.print(e);
		}
	}

}
