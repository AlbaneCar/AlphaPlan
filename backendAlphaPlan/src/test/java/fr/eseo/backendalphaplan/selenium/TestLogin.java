package fr.eseo.backendalphaplan.selenium;


import java.time.Duration;

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
class TestLogin {

	private static WebDriver webdriver;

	@BeforeAll
	public static void beforeTests(){

		ChromeOptions options = new ChromeOptions();
		TestLogin.webdriver = new ChromeDriver(options);

	}

	@Test
	void testConnexionReussie() throws InterruptedException {

	    TestLogin.webdriver.get("http://localhost:5173/login");
		WebDriverWait wait = new WebDriverWait(TestLogin.webdriver, Duration.ofSeconds(20));

	    // entrée de l'adresse e-mail
	    WebElement mail = TestLogin.webdriver.findElement(By.id("mail"));
	    mail.sendKeys("admin@mail.com");
	    
	    // entrée du mot de passe
	    WebElement pass = TestLogin.webdriver.findElement(By.id("password"));
	    pass.sendKeys("admin");

	    // clic sur connexion
	    WebElement button = TestLogin.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
	    button.click();

	    wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));

	    // Vérifier la redirection
	    String currentUrl = TestLogin.webdriver.getCurrentUrl();
	    Assertions.assertEquals("http://localhost:5173/homepage", currentUrl, "La redirection n'est pas correcte.");
	}

	@Test
	void testConnexionFailMail() throws InterruptedException {
	    TestLogin.webdriver.get("http://localhost:5173/login");

	    // entrée de l'adresse e-mail incorrecte
	    WebElement mail = TestLogin.webdriver.findElement(By.id("mail"));
	    mail.sendKeys("john@reseau.eseo.fr");

	    // entrée du mot de passe
	    WebElement pass = TestLogin.webdriver.findElement(By.id("password"));
	    pass.sendKeys("admin");

	    // clic sur connexion
	    WebElement button = TestLogin.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
	    button.click();

	    // reste sur la page de connexion
	    String currentUrl = TestLogin.webdriver.getCurrentUrl();
	    Assertions.assertEquals("http://localhost:5173/login", currentUrl, "La redirection n'est pas correcte.");

	    // message d'erreur pour le mail invalide
	    WebElement errorMessage = TestLogin.webdriver.findElement(By.xpath("//div[contains(@class, 'text-red-400')]"));
	    Assertions.assertTrue(errorMessage.getText().contains("identifiants"), "Le message d'erreur pour l'adresse e-mail incorrecte n'est pas affiché.");
	}

	@Test
	void testConnexionFailMdp() throws InterruptedException {
	    TestLogin.webdriver.get("http://localhost:5173/login");

	    // entrée de l'adresse e-mail
	    WebElement mail = TestLogin.webdriver.findElement(By.id("mail"));
	    mail.sendKeys("admin@mail.com");

	    // entrée du mot de passe incorrect
	    WebElement pass = TestLogin.webdriver.findElement(By.id("password"));
	    pass.sendKeys("Azerty");

	    // clic sur connexion
	    WebElement button = TestLogin.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
	    button.click();

	    // rester sur la page de connexion
	    String currentUrl = TestLogin.webdriver.getCurrentUrl();
	    Assertions.assertEquals("http://localhost:5173/login", currentUrl, "La redirection n'est pas correcte.");


	    // message d'erreur pour le mot de passe incorrect
	    WebElement errorMessage = TestLogin.webdriver.findElement(By.xpath("//div[contains(@class, 'text-red-400')]"));
	    Assertions.assertTrue(errorMessage.getText().contains("identifiants"), "Le message d'erreur pour le mot de passe incorrect n'est pas affiché.");
	}


	@AfterAll
	public static void afterTests(){
		TestLogin.webdriver.close();
	}
}