package fr.eseo.backendalphaplan.selenium;

import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This test class verifies the functionality of the BonusMalusTeamMembers component.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BMSSTest {

    private static WebDriver webdriver;
    private static final String frontendURL = SeleniumProperties.FRONTEND_URL;

    @BeforeAll
    public static void beforeTests() {
        ChromeOptions options = new ChromeOptions();
        BMSSTest.webdriver = new ChromeDriver(options);
        BMSSTest.webdriver.get(frontendURL + "/login");
        WebDriverWait wait = new WebDriverWait(BMSSTest.webdriver, Duration.ofSeconds(20));
        WebElement paramAvance = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("details-button")));
        paramAvance.click();
        WebElement continueLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proceed-link")));
        continueLink.click();
        // enter email
        WebElement mail = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mail")));
        mail.sendKeys("sophie.rousseau@mail.com");

        // enter password
        WebElement pass = BMSSTest.webdriver.findElement(By.id("password"));
        pass.sendKeys("RouSop");

        // click login
        WebElement button = BMSSTest.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe(frontendURL + "/homepage"));
    }
    @Order(1)
    @Test
    void testBonusMalusSS() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(BMSSTest.webdriver, Duration.ofSeconds(20));
//        WebElement bonusMalusSS = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BmSsSideBarButton")));
//        bonusMalusSS.click();
        BMSSTest.webdriver.get(frontendURL + "/bmSS"); //replace with upper lines
        wait.until(ExpectedConditions.urlToBe(frontendURL + "/bmSS"));
        Assertions.assertEquals(frontendURL + "/bmSS", BMSSTest.webdriver.getCurrentUrl(), "The redirection is not correct.");
        //WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("TitreBMSS")));
    }

//    @Test
//    void testChooser() throws InterruptedException {
//        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
//        WebElement chooser = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("chooser-id"))); // replace with actual id or selector
//        chooser.click();
//        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("option-id"))); // replace with actual id or selector
//        Assertions.assertNotNull(option, "Expected option is not present in the chooser");
//    }

//    @Test
//    void testDropdownSprint() throws InterruptedException {
//        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
//        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sprint-chooser"))); // replace with actual id or selector
//        dropdown.click();
//        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Sprint 1')]"))); // replace with actual id or selector
//        Assertions.assertNotNull(option, "Expected option is not present in the dropdown");
//    }

    @Order(2)
    @Test
    void testTotalMalusAndBonus() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
        WebElement openDialogButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("OpenDialogButton")));
        openDialogButton.click();
        WebElement dialog = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dialogBm")));
        WebElement input = dialog.findElement(By.tagName("input"));
        input.sendKeys("10"); // replace with the value you want to test
        WebElement textarea = dialog.findElement(By.tagName("textarea"));
        textarea.sendKeys("Test comment");
        WebElement createBMButton = dialog.findElement(By.cssSelector(".flex.justify-end.items-center.mt-3")).findElement(By.tagName("button"));
        createBMButton.click();
        WebElement totalBonus = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".uppercase.font-CASlalomBold.text-custom-lightblueAP.text-xs.flex.bg-white.rounded-md.border.border-gray-200.p-2")));
        WebElement totalMalus = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".uppercase.font-CASlalomBold.text-custom-red.text-xs.flex.bg-white.rounded-md.border.border-gray-200.p-2")));
        Assertions.assertEquals("MALUS DONNÉS : 0", totalMalus.getText(), "Total malus is not updated correctly");
        Assertions.assertEquals("BONUS DONNÉS : 10", totalBonus.getText(), "Total bonus is not updated correctly");
    }

    @Order(3)
    @Test
    void testSuppressionMalusAndBonus() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
        WebElement openDialogButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("OpenDialogButton")));
        openDialogButton.click();
        WebElement dialog = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dialogBm")));
        WebElement SupBmSSButton = dialog.findElement(By.id("supBmSS"));
        SupBmSSButton.click();
        WebElement totalBonus = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".uppercase.font-CASlalomBold.text-custom-lightblueAP.text-xs.flex.bg-white.rounded-md.border.border-gray-200.p-2")));
        WebElement totalMalus = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".uppercase.font-CASlalomBold.text-custom-red.text-xs.flex.bg-white.rounded-md.border.border-gray-200.p-2")));
        Assertions.assertEquals("MALUS DONNÉS : 0", totalMalus.getText(), "Total malus is not updated correctly");
        Assertions.assertEquals("BONUS DONNÉS : 0", totalBonus.getText(), "Total bonus is not updated correctly");
    }

    @AfterAll
    public static void afterTests() {
        if (BMSSTest.webdriver != null) {
            BMSSTest.webdriver.quit();
        }
    }
}
