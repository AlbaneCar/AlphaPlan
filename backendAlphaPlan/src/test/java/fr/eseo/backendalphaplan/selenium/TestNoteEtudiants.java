package fr.eseo.backendalphaplan.selenium;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestNoteEtudiants {

	private static WebDriver webdriver;

	@BeforeAll
	public static void beforeTests(){

		ChromeOptions options = new ChromeOptions();
		TestNoteEtudiants.webdriver = new ChromeDriver(options);

	}
	
	@Test
	void testBachelorNote() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(TestNoteEtudiants.webdriver, Duration.ofSeconds(20));
	    TestNoteEtudiants.webdriver.get("http://localhost:5173/login");
        WebElement mail = TestNoteEtudiants.webdriver.findElement(By.id("mail"));
	    mail.sendKeys("john.doe@reseau.eseo.fr");
	    WebElement pass = TestNoteEtudiants.webdriver.findElement(By.id("password"));
	    pass.sendKeys("DoeJoh");
	    WebElement button = TestNoteEtudiants.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
	    button.click();
	    wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));

	    WebElement etudiantsLink = TestNoteEtudiants.webdriver.findElement(By.xpath("//a[@href='/etudiants']"));
	    etudiantsLink.click();

	    
	    WebElement rowBachelor = TestNoteEtudiants.webdriver.findElement(By.xpath("//tr[td/div[contains(., 'Bachelor')]]"));
	    WebElement plusButton = rowBachelor.findElement(By.cssSelector("td.flex.justify-end svg.lucide.lucide-plus"));

	    plusButton.click();


	    WebElement dialog = webdriver.findElement(By.cssSelector("div[role='dialog']"));
	    WebElement row = dialog.findElement(By.cssSelector("tr.bg-white"));
	    List<WebElement> cells = row.findElements(By.tagName("td"));
	    String firstValue = cells.get(0).getText().trim();
	    boolean firstNum = firstValue.matches("\\d+\\.?\\d*");
	    assert(firstNum);
	    for (int i = 1; i < cells.size(); i++) {
	        List<WebElement> svgElements = cells.get(i).findElements(By.tagName("svg"));
	        assert(svgElements.size() > 0);
	    }

	    
	    WebElement closeButton = TestNoteEtudiants.webdriver.findElement(By.xpath("//button[@aria-label='Close']"));
	    closeButton.click();
	    
		String currentUrl = TestNoteEtudiants.webdriver.getCurrentUrl();
	    Assertions.assertEquals("http://localhost:5173/etudiants", currentUrl, "La redirection n'est pas correcte");
	    
	    WebElement rowE3 = TestNoteEtudiants.webdriver.findElement(By.xpath("//tr[td/div[contains(., 'E3e')]]"));
	    WebElement plusButtonE3 = rowE3.findElement(By.cssSelector("td.flex.justify-end svg.lucide.lucide-plus"));

	    plusButtonE3.click();

	    WebElement dialogE3 = webdriver.findElement(By.cssSelector("div[role='dialog']"));
	    WebElement rowBis = dialogE3.findElement(By.cssSelector("tr.bg-white"));
	    List<WebElement> cellsE3 = rowBis.findElements(By.tagName("td"));
	    String firstValueE3 = cellsE3.get(0).getText().trim();
	    boolean isNumE3 = firstValueE3.matches("\\d+\\.?\\d*");
	    assert(isNumE3);
	    for (int i = 1; i < cellsE3.size(); i++) {
	        WebElement cellE3 = cellsE3.get(i);
	        String txtE3 = cellE3.getText().trim();
	        boolean isNoteE3 = txtE3.matches("\\d+\\.?\\d*");
	        assert(isNoteE3);
	    }
	    
	    WebElement closeButtonBis = TestNoteEtudiants.webdriver.findElement(By.xpath("//button[@aria-label='Close']"));
	    closeButtonBis.click();
	    
		String currentUrlBis = TestNoteEtudiants.webdriver.getCurrentUrl();
	    Assertions.assertEquals("http://localhost:5173/etudiants", currentUrlBis, "La redirection n'est pas correcte");
	}
	
	@AfterAll
	public static void afterTests() {
	    if (TestNoteEtudiants.webdriver != null) {
	        TestNoteEtudiants.webdriver.quit();
	    }
	}

}