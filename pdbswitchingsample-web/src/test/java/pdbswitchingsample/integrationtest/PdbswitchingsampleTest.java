package pdbswitchingsample.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.io.Files;

public class PdbswitchingsampleTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private String contextroot;
	
	@Before
	public void setUp() throws Exception {

		String PROXY = System.getProperty("proxy");
		if (PROXY != null) {
			org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
			proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability(CapabilityType.PROXY, proxy);
			driver = new FirefoxDriver(cap);
		} else {
			driver = new FirefoxDriver();
		}

		baseUrl = System.getProperty("baseurl");
		contextroot = System.getProperty("contextroot");
		if (baseUrl == null) {
			throw new Exception(
					"You need to set the property 'baseurl'. Example : https://xxxxxx/'");
		}
		if (contextroot == null) {
			throw new Exception(
					"You need to set the property 'contextroot'.'");
		}
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testPdbswitchingsample() throws Exception {
		driver.get(baseUrl + contextroot + "/index.html");
		driver.findElement(By.xpath("(//input[@name='operation'])[3]")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.linkText("Go back to index page")).click();
		driver.findElement(By.name("operation")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.linkText("Go back to index page")).click();
		driver.findElement(By.xpath("(//input[@name='operation'])[2]")).click();
		driver.findElement(By.id("data")).clear();
		driver.findElement(By.id("data")).sendKeys("helloworld");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.linkText("Go back to index page")).click();
		driver.findElement(By.xpath("(//input[@name='operation'])[4]")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		// Warning: verifyTextPresent may require manual changes
		try {
			assertEquals("helloworld", driver.findElement(By.id("records"))
					.getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File("testPdbswitchingsample.png");
       
        try {
            Files.move(srcFile, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
