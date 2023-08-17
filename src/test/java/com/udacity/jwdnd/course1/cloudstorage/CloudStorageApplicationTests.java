package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private String baseURL;
    private static WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        driver = new FirefoxDriver();
    }

    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + this.port;
    }

    @AfterAll
    public static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void getLoginPage() {
        driver.get(this.baseURL + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    private void doLogout() {

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));

        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depending on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    void testAccessHomePageWithoutLogin() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(this.baseURL + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        Assertions.assertEquals(this.baseURL + "/login", driver.getCurrentUrl());
    }

    @Test
    void testSignupLoginAccessHomeLogoutCannotAccessHome() {

        String username = "johndoe";
        String password = "abc@123";

        signup("John", "Doe", username, password);

        login(username, password);

        Assertions.assertEquals(this.baseURL + "/home", driver.getCurrentUrl());

        doLogout();

        driver.get(this.baseURL + "/home");

        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.titleContains("Login"));

        Assertions.assertEquals(this.baseURL + "/login", driver.getCurrentUrl());
    }

    private void login(String username, String password) {
        driver.get(this.baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }

    private void signup(String firstName, String lastName, String username, String password) {
        driver.get(this.baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup(firstName, lastName, username, password);
    }

//    @Test
//    public void testCreateNoteAndVerifyCreatedNote() throws InterruptedException {
//
//        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
//
//        doMockSignUp("rahul", "rahul", "rahulrahul", "123");
//        doLogIn("rahulrahul", "123");
//
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
//        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
//        notesTab.click();
//
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));
//        WebElement newNoteButton = driver.findElement(By.id("add-new-note"));
//        newNoteButton.click();
//
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
//        WebElement noteTitle = driver.findElement(By.id("note-title"));
//        noteTitle.sendKeys("Test note title");
//
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
//        WebElement noteDescription = driver.findElement(By.id("note-description"));
//        noteDescription.sendKeys("Test note description");
//
//        Thread.sleep(10000);
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-button")));
//        WebElement saveNoteButton = driver.findElement((By.id("save-note-button")));
//        saveNoteButton.click();
//
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
//        WebElement notesTab2 = driver.findElement(By.id("nav-notes-tab"));
//        notesTab2.click();
//
//        Thread.sleep(10000);
//
//        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));
//        WebElement notesTable = driver.findElement(By.id("notesTable"));
//        WebElement notesTableBody = notesTable.findElement(By.tagName("tbody"));
//        WebElement lastRow = notesTableBody.findElement(By.cssSelector("tr:last-of-type"));
//        WebElement lastRowNoteTitle = lastRow.findElement(By.tagName("th"));
//        WebElement lastRowNoteDescription = notesTableBody.findElement(By.cssSelector("td:last-of-type"));
//
//        Assertions.assertSame("Test note title", lastRowNoteTitle.getText());
//        Assertions.assertSame("Test note description", lastRowNoteDescription.getText());
//
//    }

    @Test
    void createNoteAndVerifyCreatedNote() {

        String username = "johndoe";
        String password = "abc@123";

        signup("John", "Doe", username, password);
        login(username, password);

        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).clk();
        NotesTabPage notesTabPage = new NotesTabPage(driver);

        String expectedtTitle = "Test Title";
        String expectedDescription ="Test Description";
        notesTabPage.createNote(expectedtTitle, expectedDescription);
        WebElement homePageLink = new WebDriverWait(driver, Duration.ofSeconds(2)).
                until(webDriver -> webDriver.findElement(By.cssSelector(".alert-success a")));
        homePageLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
        Assertions.assertEquals(expectedtTitle, notesTabPage.getNoteTitle());
        Assertions.assertEquals(expectedDescription, notesTabPage.getNoteDescription());

    }

}
