package fr.eseo.backendalphaplan.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DashBoardTest {

    private static WebDriver webdriver;
    private static final String frontendURL = SeleniumProperties.FRONTEND_URL;

    @BeforeAll
    public static void beforeTests() {
        ChromeOptions options = new ChromeOptions();
        DashBoardTest.webdriver = new ChromeDriver(options);
        DashBoardTest.webdriver.get(frontendURL + "/login");
        WebDriverWait wait = new WebDriverWait(DashBoardTest.webdriver, Duration.ofSeconds(20));
        WebElement paramAvance = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("details-button")));
        paramAvance.click();
        WebElement continueLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proceed-link")));
        continueLink.click();
        // Login
        WebElement mail = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mail")));
        mail.sendKeys("maeva.boudon@mail.com");
        WebElement pass = DashBoardTest.webdriver.findElement(By.id("password"));
        pass.sendKeys("BouMae");
        WebElement button = DashBoardTest.webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe(frontendURL + "/homepage"));
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testDashboard(String locator, String expectedText) {
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
        assertEquals(expectedText, element.getText());
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("//div[contains(@class, 'text-2xl font-CASlalomBold text-custom-darkblueAP')]", "Bienvenue Maéva !"),
                Arguments.of("//div[contains(@class, 'text-custom-lightblueAP text-sm font-CASlalomBold')]", "Sprint n° 1"),
                Arguments.of("//div[contains(@class, 'flex flex-col w-full h-full justify-center items-center gap-3')]//div[contains(@class, 'text-xl text-custom-darkblueAP font-CASlalomBold')]", "Aucune notification !"),
                Arguments.of("//p[contains(@class, 'font-Roboto text-custom-darkblueAP text-sm')]", "Rien à voir pour le moment !")
        );
    }

    @AfterAll
    public static void afterTests() {
        if (webdriver != null) {
            webdriver.quit();
        }
    }
}