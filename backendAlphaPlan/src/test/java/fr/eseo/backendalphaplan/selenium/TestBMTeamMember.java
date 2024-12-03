package fr.eseo.backendalphaplan.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

// Pour ces tests, il faut s'assurer d'avoir au préalable dans la bdd :
// - L'utilisateur Cyril ABADIE avec son bon mdp, et dans l'équipe 1
// - Au moins 2/3 utilisateurs dans l'équipe 1

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestBMTeamMember {

    private static WebDriver webdriver;

    @BeforeAll
    public static void beforeTests() {
        ChromeOptions options = new ChromeOptions();
        webdriver = new ChromeDriver(options);
    }

    @BeforeEach
    public void beforeEachTest() {
    }

    @Test
    @Order(1)
    void testBMTM_Advancment1() throws InterruptedException {
        deleteNotesS1();
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
        webdriver.get("http://localhost:5173/login");
        WebElement mail = webdriver.findElement(By.id("mail"));
        mail.sendKeys("cyril.abadie@reseau.eseo.fr");
        WebElement pass = webdriver.findElement(By.id("password"));
        pass.sendKeys("ABADIE");
        WebElement button = webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));
        webdriver.get("http://localhost:5173/team/1/bonusmalusTM");
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/team/1/bonusmalusTM"));


        // Clic +
        WebElement plusElement = webdriver.findElement(By.xpath("(//span[@class='flex h-6 justify-start items-start font-CASlalomBold text-lg leading-6 text-black absolute top-1 right-3 text-left'])[1]"));
        plusElement.click();
  
        // Affichage du toast d'erreur
        WebElement toast = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast = toast.getText();

        assertEquals("Les bonus/malus ne sont pas encore disponibles.", messageToast);
        assertTrue(toast.isDisplayed());
    }


    
    @Test
    @Order(2)
    void testBMTM_Advancment2() throws InterruptedException {
    	addNotesS1();
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
        webdriver.get("http://localhost:5173/login");
        WebElement mail = webdriver.findElement(By.id("mail"));
        mail.sendKeys("cyril.abadie@reseau.eseo.fr");
        WebElement pass = webdriver.findElement(By.id("password"));
        pass.sendKeys("ABADIE");
        WebElement button = webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));
        webdriver.get("http://localhost:5173/team/1/bonusmalusTM");

        // Vérification de la valeur avant le clic sur +
        WebElement valueElementBeforeClick = webdriver.findElement(By.xpath("(//span[@class='text-black h-[30.5px] w-[103px] font-CASlalomBold text-lg px-12 py-1 flex justify-center items-center'])[1]"));
        String valueBeforeClick = valueElementBeforeClick.getText();
        assertEquals("0", valueBeforeClick);

        // Clic +
        WebElement plusElement = webdriver.findElement(By.xpath("(//span[@class='flex h-6 justify-start items-start font-CASlalomBold text-lg leading-6 text-black absolute top-1 right-3 text-left'])[1]"));
        plusElement.click();
        plusElement.click();
        plusElement.click();
        plusElement.click();
        plusElement.click();


        // Il n'est pas possible d'ajouter plus que 4 ou -4 pts
        WebElement toast = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast = toast.getText();

        assertEquals("Le bonus ne peut pas excéder 4 points !", messageToast);
        assertTrue(toast.isDisplayed());
        
        
        // Vérification de la valeur avant le clic sur -
        WebElement valueElementBeforeClick2 = webdriver.findElement(By.xpath("(//span[@class='text-black h-[30.5px] w-[103px] font-CASlalomBold text-lg px-12 py-1 flex justify-center items-center'])[1]"));
        String valueBeforeClick2 = valueElementBeforeClick2.getText();
        assertEquals("0", valueBeforeClick2);

        // Clic +
        WebElement moinsElement = webdriver.findElement(By.xpath("(//span[@class='flex h-6 justify-start items-start font-CASlalomBold text-lg leading-6 text-black absolute top-1 left-3 text-left'])[2]"));
        moinsElement.click();
        moinsElement.click();
        moinsElement.click();

        assertEquals("Le bonus ne peut pas excéder 4 points !", messageToast);
        assertTrue(toast.isDisplayed());

        
        // Vérification de la valeur après le clic sur +
        String valueAfterClick = valueElementBeforeClick.getText();
        assertEquals("4", valueAfterClick);
        
        // Vérification de la valeur après le clic sur +
        String valueAfterClick2 = valueElementBeforeClick2.getText();
        assertEquals("-3", valueAfterClick2);
        
        //Appuyer sur le bouton Enregistrer si les BM ne sont pas équilibrés
        WebElement buttonSave = webdriver.findElement(By.xpath("//button[@class='bg-custom-lightblueAP hover:bg-custom-darkblueAP text-white font-CASlalomBold rounded-md duration-300 mt-4 py-2 px-4']"));
        buttonSave.click();
        
        WebElement toast2 = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast2 = toast2.getText();

        // Toast affichant un message d'erreur
        assertEquals("Le bonus et malus ne sont pas équilibrés.", messageToast2);
        assertTrue(toast2.isDisplayed());
        
        //équilibrage des BM
        moinsElement.click();

        
        //Appuyer sur le bouton Enregistrer s'il n'y a pas de commentaire associé
        buttonSave.click();


        WebElement toast3 = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast3 = toast3.getText();

        // Toast affichant un message d'erreur
        assertEquals("Veuillez ajouter un commentaire pour justifier ces choix.", messageToast3);
        assertTrue(toast3.isDisplayed());
        
        // Cas où le BM respecte les règles et possède un commentaire
        
        //Ajout d'un commentaire
        WebElement comments = webdriver.findElement(By.xpath("//textarea[@placeholder=\"Si vous faîtes le choix d'accorder des Bonus & Malus, expliquez en quelques lignes les raisons qui ont motivées ces changements.\"]"));
		comments.sendKeys("Ceci est une justification");

        
		// Toutes les condition sont réunies -> confirmer le BM
        buttonSave.click();
        
        WebElement toast4 = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast4 = toast4.getText();

        // Toast affichant la confirmation des BM
        assertEquals("Tous les bonus/malus ont été ajoutés avec succès.", messageToast4);
        assertTrue(toast4.isDisplayed());

    }
    
    @Test
    @Order(3)
    void testBMTM_Advancment3() throws InterruptedException {
    	//addValidation();
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
        webdriver.get("http://localhost:5173/login");
        WebElement mail = webdriver.findElement(By.id("mail"));
        mail.sendKeys("cyril.abadie@reseau.eseo.fr");
        WebElement pass = webdriver.findElement(By.id("password"));
        pass.sendKeys("ABADIE");
        WebElement button = webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));
        webdriver.get("http://localhost:5173/team/1/bonusmalusTM");
        
        // Vérification de la valeur avant le clic sur +
        WebElement valueElementBeforeClick = webdriver.findElement(By.xpath("(//span[@class='text-black h-[30.5px] w-[103px] font-CASlalomBold text-lg px-12 py-1 flex justify-center items-center'])[1]"));
        String valueBeforeClick = valueElementBeforeClick.getText();
        assertEquals("0", valueBeforeClick);

        // Clic +
        WebElement plusElement = webdriver.findElement(By.xpath("(//span[@class='flex h-6 justify-start items-start font-CASlalomBold text-lg leading-6 text-black absolute top-1 right-3 text-left'])[1]"));
        plusElement.click();


        
        WebElement toast = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast = toast.getText();

        // Toast affichant la non possibilité d'appuyer sur +
        assertEquals("Les bonus/malus ne sont pas modifiables.", messageToast);
        assertTrue(toast.isDisplayed());

        
        //Valider les BM
        WebElement btnValidate = webdriver.findElement(By.xpath("//button[contains(., 'Je valide les bonus/malus')]"));
        btnValidate.click();
        
        WebElement toast2 = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast2 = toast2.getText();

        // Toast affichant la confirmation de validation des BM
        assertEquals("Vous venez de valider les bonus malus !", messageToast2);
        assertTrue(toast2.isDisplayed());

    }
    
    @Test
    @Order(4)
    void testBMTM_Advancment3Bis() throws InterruptedException {
    	//addValidation();
        WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(20));
        webdriver.get("http://localhost:5173/login");
        WebElement mail = webdriver.findElement(By.id("mail"));
        mail.sendKeys("cyril.abadie@reseau.eseo.fr");
        WebElement pass = webdriver.findElement(By.id("password"));
        pass.sendKeys("ABADIE");
        WebElement button = webdriver.findElement(By.xpath("//button[contains(text(), 'Se connecter')]"));
        button.click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/homepage"));
        webdriver.get("http://localhost:5173/team/1/bonusmalusTM");

        
        // Clic +
        WebElement plusElement = webdriver.findElement(By.xpath("(//span[@class='flex h-6 justify-start items-start font-CASlalomBold text-lg leading-6 text-black absolute top-1 right-3 text-left'])[1]"));
        plusElement.click();

        
        WebElement toast = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast = toast.getText();

        // Toast affichant la non possibilité d'appuyer sur +
        assertEquals("Les bonus/malus ne sont pas modifiables.", messageToast);
        assertTrue(toast.isDisplayed());

        // Ne pas pouvoir valider à nouveau les BM
        WebElement btnValidate = webdriver.findElement(By.xpath("//button[contains(., 'Je valide les bonus/malus')]"));
        btnValidate.click();
        
        WebElement toast2 = webdriver.findElement(By.xpath("//div[@class='Vue-Toastification__toast-body']"));
        String messageToast2 = toast2.getText();

        // Toast affichant que le BM a déja été validé
        assertEquals("Vous avez déjà validé ce Bonus Malus.", messageToast2);
        assertTrue(toast2.isDisplayed());
    }

    @AfterAll
    public static void afterTests() {
        if (webdriver != null) {
            webdriver.quit();
        }
    }
    
    private void deleteNotesS1() {
        String url = "jdbc:mariadb://localhost:3308/alphaplan";
        String username = "root";
        String password = "network";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

        	String deleteQuery1 = "DELETE FROM bonus_malus WHERE note_id IN ( SELECT id FROM note_eleve WHERE sprint_id = 1 AND eleve_id IN (SELECT id FROM Utilisateur WHERE equipe_id = 1 ) AND type_note_eleve = 'IN_SP')";
            String deleteQuery2 = "DELETE FROM note_eleve WHERE sprint_id = 1 AND eleve_id IN (SELECT id FROM Utilisateur WHERE equipe_id = 1) AND type_note_eleve = 'IN_SP'";
            statement.executeUpdate(deleteQuery1);
            statement.executeUpdate(deleteQuery2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void addNotesS1() {
        String url = "jdbc:mariadb://localhost:3308/alphaplan";
        String username = "root";
        String password = "network";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            // Vérifier si des notes existent déjà pour le sprint 1 et l'équipe 1
            String checkQuery = "SELECT COUNT(*) FROM note_eleve WHERE sprint_id = 1 AND eleve_id IN (SELECT id FROM Utilisateur WHERE equipe_id = 1) AND type_note_eleve = 'IN_SP'";
            ResultSet resultSet = statement.executeQuery(checkQuery);
            resultSet.next();
            int existingNotesCount = resultSet.getInt(1);

            // S'il n'y a pas de notes existantes, insérer de nouvelles notes
            if (existingNotesCount == 0) {
                String insert = "INSERT INTO note_eleve (eleve_id, evaluateur_id, sprint_id, message, note, commentaire, type_note_eleve) " +
                                "SELECT id AS eleve_id, 0 AS evaluateur_id, 1 AS sprint_id, '' AS message, 12.0 AS note, '' AS commentaire, 'IN_SP' AS type_note_eleve " +
                                "FROM Utilisateur WHERE equipe_id = 1";
                statement.executeUpdate(insert);
            } else {
                System.out.println("Des notes existent déjà pour le sprint 1 et l'équipe 1.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
