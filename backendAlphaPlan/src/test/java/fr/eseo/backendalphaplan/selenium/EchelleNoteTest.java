package fr.eseo.backendalphaplan.selenium;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class EchelleNoteTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  private static final String frontendURL = SeleniumProperties.FRONTEND_URL;

  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void echelleNote() throws InterruptedException {
    driver.get(frontendURL + "/login");
    driver.manage().window().setSize(new Dimension(860, 816));
    ChromeOptions options = new ChromeOptions();
    driver.get(frontendURL + "/login");
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    WebElement paramAvance = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("details-button")));
    paramAvance.click();
    WebElement continueLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proceed-link")));
    continueLink.click();
    // Login
    WebElement mail = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mail")));
    mail.sendKeys("maeva.boudon@mail.com");
    WebElement pass = driver.findElement(By.id("password"));
    pass.sendKeys("BouMae");
    WebElement button = driver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
    button.click();
    wait.until(ExpectedConditions.urlToBe(frontendURL + "/homepage"));
    Assertions.assertEquals(frontendURL + "/homepage", driver.getCurrentUrl(), "The redirection after login is not correct.");
    driver.get("http://localhost:5173/echelleNotes");
    Assertions.assertEquals("http://localhost:5173/echelleNotes", driver.getCurrentUrl(), "The redirection to the echelleNotes page is not correct.");
    WebElement buttonA = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.bg-\\[\\#1A78D1\\]")));
    buttonA.click();
    {
      WebElement element = driver.findElement(By.xpath("//button/div/div"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.id("type")).click();
    {
      WebElement dropdown = driver.findElement(By.id("type"));
      dropdown.findElement(By.xpath("//option[. = 'PRESENTATION']")).click();
    }
    driver.findElement(By.id("description")).click();
    driver.findElement(By.id("description")).click();
    driver.findElement(By.id("description")).sendKeys("TEST");
    driver.findElement(By.id("note1")).click();
    driver.findElement(By.id("note1")).sendKeys("1-5");
    driver.findElement(By.id("note2")).click();
    driver.findElement(By.id("note2")).sendKeys("5-10");
    driver.findElement(By.id("note3")).click();
    driver.findElement(By.id("note3")).sendKeys("10-15");
    driver.findElement(By.id("note4")).click();
    driver.findElement(By.id("note4")).sendKeys("18-20");
    driver.findElement(By.id("note5")).click();
    driver.findElement(By.id("note5")).sendKeys("20+");
    driver.findElement(By.cssSelector(".bg-\\[\\#1A78D1\\]:nth-child(15) > .flex")).click();
    WebElement element = driver.findElement(By.id("pv_id_2_0_header_action"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    element.click();
//    driver.findElement(By.id("pv_id_2_0_header_action")).click();
    driver.findElement(By.cssSelector(".delete-button")).click();
    driver.findElement(By.cssSelector(".confirm-button")).click();
    driver.findElement(By.cssSelector(".lucide-log-out-icon")).click();
    wait.until(ExpectedConditions.urlToBe(frontendURL + "/login"));
    Assertions.assertEquals(frontendURL + "/login", driver.getCurrentUrl(), "The redirection after logout is not correct.");  }
}
