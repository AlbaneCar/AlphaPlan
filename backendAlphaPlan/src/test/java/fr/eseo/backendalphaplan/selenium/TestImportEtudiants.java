package fr.eseo.backendalphaplan.selenium;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
class TestImportEtudiants {

	private static WebDriver webdriver;

	@BeforeAll
	public static void beforeTests(){

		ChromeOptions options = new ChromeOptions();
		TestImportEtudiants.webdriver = new ChromeDriver(options);

	}
	
	@Test
    @Order(1)
	void testRedirection() throws InterruptedException {
	    TestImportEtudiants.webdriver.get("http://localhost:5173/etudiants");
	    String currentUrl = TestImportEtudiants.webdriver.getCurrentUrl();
	    Assertions.assertEquals("http://localhost:5173/login", currentUrl, "La redirection n'est pas correcte.");
	}
	
	@Test
    @Order(2)
	void testImportEtudiant() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(TestImportEtudiants.webdriver, Duration.ofSeconds(20));
	    TestImportEtudiants.webdriver.get("http://localhost:5173/login");
        WebElement mail = TestImportEtudiants.webdriver.findElement(By.id("mail"));
	    mail.sendKeys("john.doe@reseau.eseo.fr");
	    WebElement pass = TestImportEtudiants.webdriver.findElement(By.id("password"));
	    pass.sendKeys("DoeJoh");
	    WebElement button = TestImportEtudiants.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
	    button.click();
	    wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));

	    WebElement etudiantsLink = TestImportEtudiants.webdriver.findElement(By.xpath("//a[@href='/etudiants']"));
	    etudiantsLink.click();
	    
	    // chercher le bouton Importer et cliquer dessus
	    WebElement importButton = TestImportEtudiants.webdriver.findElement(By.xpath("//button[contains(., 'Importer')]"));
	    importButton.click();
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("label.flex svg.lucide-cloud-upload")));
	    
	    // Vérifier si le dialog d'importation s'ouvre et que l'on peut cliquer sur le nuage
	    WebElement cloudUploadButton = TestImportEtudiants.webdriver.findElement(By.cssSelector("label.flex svg.lucide-cloud-upload"));
	    cloudUploadButton.click();
	    
	    // Sélectionner le bouton pouur valider le fichier
	    WebElement sendButton = TestImportEtudiants.webdriver.findElement(By.cssSelector("button.upload-button"));
	    sendButton.click();
	    
	    String currentUrl = TestImportEtudiants.webdriver.getCurrentUrl();
	    Assertions.assertEquals("http://localhost:5173/etudiants", currentUrl, "La redirection n'est pas correcte");
	}
	
	@AfterAll
	public static void afterTests() {
	    if (TestImportEtudiants.webdriver != null) {
	        TestImportEtudiants.webdriver.quit();
	    }
	}

}