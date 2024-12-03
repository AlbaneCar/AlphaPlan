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
class TestEchellesNotes {

    private static WebDriver webdriver;

    @BeforeAll
    public static void beforeTests(){

        ChromeOptions options = new ChromeOptions();
        TestEchellesNotes.webdriver = new ChromeDriver(options);

    }

    @Test
    void testCreeEchelles() throws InterruptedException {

        // Accéder à la page de connexion
        TestEchellesNotes.webdriver.get("http://localhost:5173/login");
        WebDriverWait wait = new WebDriverWait(TestEchellesNotes.webdriver, Duration.ofSeconds(20));

        // Remplir le champ d'adresse e-mail
        WebElement mail = TestEchellesNotes.webdriver.findElement(By.id("mail"));
        mail.sendKeys("john.doe@reseau.eseo.fr");

        // Remplir le champ de mot de passe
        WebElement pass = TestEchellesNotes.webdriver.findElement(By.id("password"));
        pass.sendKeys("DoeJoh");

        // Cliquer sur le bouton de connexion
        WebElement button = TestEchellesNotes.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();

        // Attendre que la page d'accueil soit chargée
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));

        // Redirection vers la page échelles de notes
        TestEchellesNotes.webdriver.get("http://localhost:5173/echelleNotes");
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/echelleNotes"));

        // Sélectionner le type d'échelle
        TestEchellesNotes.webdriver.findElement(By.id("type")).click();
        {
            WebElement dropdown = TestEchellesNotes.webdriver.findElement(By.id("type"));
            dropdown.findElement(By.xpath("//option[. = 'PRESENTATION']")).click();
        }
        // Remplir la description de l'échelle
        TestEchellesNotes.webdriver.findElement(By.id("description")).click();
        TestEchellesNotes.webdriver.findElement(By.id("description")).sendKeys("La");

        // Remplir les notes de l'échelle
        TestEchellesNotes.webdriver.findElement(By.cssSelector(".items-center:nth-child(1) > .border")).click();
        TestEchellesNotes.webdriver.findElement(By.cssSelector(".items-center:nth-child(1) > .border")).sendKeys("10");
        TestEchellesNotes.webdriver.findElement(By.cssSelector(".items-center:nth-child(2) > .border")).click();
        TestEchellesNotes.webdriver.findElement(By.cssSelector(".items-center:nth-child(2) > .border")).sendKeys("18");

        // Remplir le commentaire de l'échelle
        TestEchellesNotes.webdriver.findElement(By.cssSelector(".w-40")).click();
        TestEchellesNotes.webdriver.findElement(By.cssSelector(".w-40")).sendKeys("bien");

        // Vérifier que le bouton pour ajouter un échelon est présent
        WebElement addButton = TestEchellesNotes.webdriver.findElement(By.cssSelector(".mt-2 > .justify-center"));
        Assertions.assertNotNull(addButton);

        // Cliquer sur le bouton pour ajouter un échelon
        addButton.click();

       // Remplir les notes de l'échelon
        WebElement note1 = TestEchellesNotes.webdriver.findElement(By.cssSelector(".flex:nth-child(6) > .flex:nth-child(1) > .border"));
        note1.clear();
        note1.sendKeys("1");
        Assertions.assertEquals("1", note1.getAttribute("value"));

        note1.click();

        WebElement note2 = TestEchellesNotes.webdriver.findElement(By.cssSelector(".flex:nth-child(6) > .flex:nth-child(1) > .border"));
        note2.clear();
        note2.sendKeys("2");
        Assertions.assertEquals("2", note2.getAttribute("value"));

        note2.click();

        // Remplir le commentaire de l'échelon
        WebElement comment1 = TestEchellesNotes.webdriver.findElement(By.cssSelector(".flex:nth-child(6) > .flex:nth-child(2) > .border"));
        comment1.sendKeys("1");
        Assertions.assertEquals("1", comment1.getAttribute("value"));

        comment1.click();

        WebElement comment2 = TestEchellesNotes.webdriver.findElement(By.cssSelector(".flex:nth-child(6) .w-40"));
        comment2.click();
        comment2.sendKeys("null");
        Assertions.assertEquals("null", comment2.getAttribute("value"));

        // Vérifier que le bouton pour créer l'échelle est présent
        WebElement createButton = TestEchellesNotes.webdriver.findElement(By.cssSelector(".mt-4 .leading-5"));
        Assertions.assertNotNull(createButton);

        // Cliquer sur le bouton pour créer l'échelle
        createButton.click();

        // Vérifier que l'échelle a bien été créée
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/echelleNotes"));
        String currentUrl = TestEchellesNotes.webdriver.getCurrentUrl();
        Assertions.assertEquals("http://localhost:5173/echelleNotes", currentUrl);
    }


    @Test
    void testSupprimerEchelles() throws InterruptedException {

        // Accéder à la page de connexion
        TestEchellesNotes.webdriver.get("http://localhost:5173/login");
        WebDriverWait wait = new WebDriverWait(TestEchellesNotes.webdriver, Duration.ofSeconds(20));

        // Remplir le champ d'adresse e-mail
        WebElement mail = TestEchellesNotes.webdriver.findElement(By.id("mail"));
        mail.sendKeys("john.doe@reseau.eseo.fr");

        // Remplir le champ de mot de passe
        WebElement pass = TestEchellesNotes.webdriver.findElement(By.id("password"));
        pass.sendKeys("DoeJoh");

        // Cliquer sur le bouton de connexion
        WebElement button = TestEchellesNotes.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();

        // Attendre que la page d'accueil soit chargée
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));

        // Redirection vers la page échelles de notes
        TestEchellesNotes.webdriver.get("http://localhost:5173/echelleNotes");
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/echelleNotes"));

        // Vérifier que la redirection a bien été effectuée
        String currentUrl = TestEchellesNotes.webdriver.getCurrentUrl();
        Assertions.assertEquals("http://localhost:5173/echelleNotes", currentUrl);

        // Vérifier que le bouton pour supprimer une échelle est présent
        WebElement deleteButton = TestEchellesNotes.webdriver.findElement(By.cssSelector(".flex .justify-center"));
        Assertions.assertNotNull(deleteButton);

        // Cliquer sur le bouton pour supprimer une échelle
        deleteButton.click();

    }

    @AfterAll
    public static void afterTests(){
        TestEchellesNotes.webdriver.close();
    }
}