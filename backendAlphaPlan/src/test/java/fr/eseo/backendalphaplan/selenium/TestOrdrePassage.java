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

/**Cette classe de test vérifie la redirection vers la page de connexion lors de l'accès à la page des équipes,
 *  puis se connecte et vérifie qu'il y a au moins une équipe sur la page et que chaque équipe a un nom et un chef d'équipe.
 **/

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestOrdrePassage {

    private static WebDriver webdriver;
    private static final String frontendURL = SeleniumProperties.FRONTEND_URL;

    @BeforeAll
    public static void beforeTests() {
        ChromeOptions options = new ChromeOptions();
        TestOrdrePassage.webdriver = new ChromeDriver(options);
        TestOrdrePassage.webdriver.get(frontendURL + "/login");
        WebDriverWait wait = new WebDriverWait(TestOrdrePassage.webdriver, Duration.ofSeconds(20));
        WebElement paramAvance = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("details-button")));
        paramAvance.click();
        WebElement continueLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proceed-link")));
        continueLink.click();
        // Login
        WebElement mail = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mail")));
        mail.sendKeys("maeva.boudon@mail.com");
        WebElement pass = TestOrdrePassage.webdriver.findElement(By.id("password"));
        pass.sendKeys("BouMae");
        WebElement button = TestOrdrePassage.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe(frontendURL + "/homepage"));
    }

    @Test
    void testOrdrePassage() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(TestOrdrePassage.webdriver, Duration.ofSeconds(20));
        TestOrdrePassage.webdriver.get(frontendURL + "/equipes/1/presentation"); // replace with the URL of your Vue.js component

        // Check if team members are displayed
        List<WebElement> teamMembers = TestOrdrePassage.webdriver.findElements(By.xpath("//div[contains(@class, 'p-2 bg-gray-200 rounded-md shadow mb-2')]"));
        Assertions.assertTrue(teamMembers.size() > 0, "No team members found");

        // Check if the order of the team members can be changed
        // This part is tricky because Selenium does not support drag and drop for HTML5, which is used by VueDraggableNext
        // You might need to use JavaScript to simulate drag and drop or use other tools like Puppeteer

        // Check if the order can be submitted
        WebElement submitButton = TestOrdrePassage.webdriver.findElement(By.xpath("//button[contains(@class, 'bg-custom-lightblueAP rounded-md text-white border-none px-2 py-1 mb-2 font-CASlalomBold')]"));
        submitButton.click();

        // Check if a success message is displayed
        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'Vue-Toastification__toast')]//div[contains(@class, 'Vue-Toastification__toast-body')]")));
        Assertions.assertNotNull(successMessage, "Success message not displayed");
    }

    @AfterAll
    public static void afterTests() {
        if (TestOrdrePassage.webdriver != null) {
            TestOrdrePassage.webdriver.quit();
        }
    }
}
