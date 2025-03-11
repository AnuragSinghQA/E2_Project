package generic_utility;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import object_repository.LoginPage;

public class BaseClass {

	public WebDriver driver;
	public static WebDriver sdriver;
	public FileUtility fUtil = new FileUtility();
	public WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	public ExtentReports reports;

	@BeforeSuite
	public void bsuite() {
		Reporter.log("DB connection, Report generation", true);
		ExtentSparkReporter spark = new ExtentSparkReporter("./Advance_reporting/report.html");
		spark.config().setDocumentTitle("Document title");
		spark.config().setReportName("Report Name");
		spark.config().setTheme(Theme.DARK);

		reports = new ExtentReports();
		reports.attachReporter(spark);
		reports.setSystemInfo("O.S.", "Windows 11");
		reports.setSystemInfo("Browser", "Chrome");

	}

	@BeforeTest
	public void btest() {
		Reporter.log("Pre condition", true);
	}

	@BeforeClass
	public void bclass() throws IOException {
		Reporter.log("Opening browser", true);
		String BROWSER = fUtil.getDataFromProp("bro");
		String URL = fUtil.getDataFromProp("url");

		if (BROWSER.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (BROWSER.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (BROWSER.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (BROWSER.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else {
			driver = new ChromeDriver();
		}
		sdriver = driver;

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		driver.get(URL);
	}

	@BeforeMethod
	public void bmethod() throws IOException {
		Reporter.log("Login", true);
		LoginPage lp = new LoginPage(driver);
		lp.login();
	}

	@AfterMethod
	public void amethod() throws InterruptedException {
		Reporter.log("Logout", true);
		WebElement profile = driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"));
		Actions act = new Actions(driver);
		act.moveToElement(profile).build().perform();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Sign Out")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign Out"))).click();
	}

	@AfterClass
	public void aclass() {
		Reporter.log("close browser", true);
		driver.quit();
	}

	@AfterTest
	public void atest() {
		Reporter.log("Post condition", true);
	}

	@AfterSuite
	public void asuite() {
		Reporter.log("DB close, Report backup", true);
		reports.flush();
	}
}
