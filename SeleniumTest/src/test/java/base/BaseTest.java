package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {

	public static WebDriver driver;

	/**
	 * I'm only setting up the chromeDriver - In the future I could have a dynamic
	 * setup depending on browserType given in config file, instead of hard coding
	 * chrome driver
	 */
	@BeforeTest
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		// Adding an implicit wait here, my browser is slow sometimes - hopefully yours
		// is fast :)
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

	}

	@AfterTest
	public void teardown() {
		// clean up the webdriver
		driver.close();
	}

}
