package fr.eseo.backendalphaplan.selenium;

import java.awt.*;
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

/**Cette classe de test vérifie la redirection vers la page de connexion lors de l'accès à la page des équipes,
 *  puis se connecte et vérifie qu'il y a au moins une équipe sur la page et que chaque équipe a un nom et un chef d'équipe.
 **/

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestAffichageEquipe {

    private static WebDriver webdriver;
    private static final String frontendURL = SeleniumProperties.FRONTEND_URL;
    @BeforeAll
    public static void beforeTests() {
        ChromeOptions options = new ChromeOptions();
        TestAffichageEquipe.webdriver = new ChromeDriver(options);
        TestAffichageEquipe.webdriver.get(frontendURL + "/login");
        WebDriverWait wait = new WebDriverWait(TestAffichageEquipe.webdriver, Duration.ofSeconds(20));
        WebElement paramAvance = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("details-button")));
        paramAvance.click();
        WebElement continueLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proceed-link")));
        continueLink.click();
        // entrée de l'adresse e-mail
        WebElement mail = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mail")));
        mail.sendKeys("admin@mail.com");

        // entrée du mot de passe
        WebElement pass = TestAffichageEquipe.webdriver.findElement(By.id("password"));
        pass.sendKeys("admin");

        // clic sur connexion
        WebElement button = TestAffichageEquipe.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe(frontendURL + "/homepage"));
    }

//    @Test
//    @Order(1)
//    void testRedirection() {
//        TestAffichageEquipe.webdriver.get("http://localhost:5173/equipes");
//        String currentUrl = TestAffichageEquipe.webdriver.getCurrentUrl();
//        Assertions.assertEquals("http://localhost:5173/login", currentUrl, "La redirection n'est pas correcte.");
//    }

    @Test
    void testEquipes() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(TestAffichageEquipe.webdriver, Duration.ofSeconds(20));
//        WebElement equipesLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'flex items-center ml-6 mt-3')]//router-link[contains(@to, '{ name: routeNames.LISTE_EQUIPES_VUE }')]//div[contains(text(), 'Équipes')]")));
//        equipesLink.click();
        TestAffichageEquipe.webdriver.get(frontendURL + "/equipes");
        List<WebElement> equipes = TestAffichageEquipe.webdriver.findElements(By.xpath("//table[contains(@class, 'w-full')]"));

        Assertions.assertTrue(equipes.size() > 0, "No teams found");

       for (WebElement equipe : equipes) {
        WebElement nomEquipe = equipe.findElement(By.xpath(".//th[contains(text(), equipe.nom)]"));
        WebElement nomChefEquipe = equipe.findElement(By.xpath(".//th[contains(text(), equipe.utilisateur.nom)]"));

        Assertions.assertTrue(nomEquipe.getText().length() > 0, "Team name is empty");
        Assertions.assertTrue(nomChefEquipe.getText().length() > 0, "Team leader name is empty");
       }
    }

    @Test
    void testSignalement() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(TestAffichageEquipe.webdriver, Duration.ofSeconds(20));
        TestAffichageEquipe.webdriver.get(frontendURL + "/equipes");
        WebElement signalement = webdriver.findElement(By.cssSelector(".text-right")).findElement(By.tagName("button"));

        // Check if the signalement button is displayed
        Assertions.assertTrue(signalement.isDisplayed(), "Signalement button is not displayed");

        signalement.click();
        WebElement dialog = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dialogSignalementEquipe"))); // replace with actual class name or selector
        WebElement textarea = dialog.findElement(By.tagName("textarea"));

        // Check if the dialog and textarea are displayed
        Assertions.assertTrue(dialog.isDisplayed(), "Dialog is not displayed");
        Assertions.assertTrue(textarea.isDisplayed(), "Textarea is not displayed");

        textarea.sendKeys("Test comment");

        // Check if the textarea contains the correct text
        Assertions.assertEquals("Test comment", textarea.getAttribute("value"), "Textarea does not contain the correct text");

        WebElement createBMButton = dialog.findElement(By.cssSelector(".flex.flex-col.justify-start.items-center.p-2.gap-3")).findElement(By.tagName("button"));
        createBMButton.click();

        // Add more assertions here based on what you want to test after the button click
    }

    @AfterAll
    public static void afterTests() {
        if (TestAffichageEquipe.webdriver != null) {
            TestAffichageEquipe.webdriver.quit();
        }
    }
}
