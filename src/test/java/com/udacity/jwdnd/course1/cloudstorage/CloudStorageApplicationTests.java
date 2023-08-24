package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

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

//    @AfterEach
//    public void afterEach() {
//
//    }

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
        Assertions.assertEquals(this.baseURL + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {

        driver.get(this.baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        try {
            signupPage.signup(firstName, lastName, userName, password);
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.

        driver.get(this.baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        try {
            loginPage.login(userName, password);
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        }

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
        Assertions.assertEquals(this.baseURL + "/login", driver.getCurrentUrl());
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
        driver.get(this.baseURL + "/some-random-page");
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

        String username = "SLLT";
        String password = "123";

        doMockSignUp("SignUp Login Logout", "Test", username, password);

        doLogIn(username, password);

        Assertions.assertEquals(this.baseURL + "/home", driver.getCurrentUrl());

        doLogout();

        driver.get(this.baseURL + "/home");

        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.titleContains("Login"));

        Assertions.assertEquals(this.baseURL + "/login", driver.getCurrentUrl());
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
    void testCreateNote() {

        String username = "CNT";
        String password = "123";

        doMockSignUp("Create Note", "Test", username, password);
        doLogIn(username, password);

        NotesTabPage notesTabPage = new NotesTabPage(driver);
        String expectedTitle = "Test Title";
        String expectedDescription = "Test Description";
        createMockNote(expectedTitle, expectedDescription, notesTabPage);
        Assertions.assertEquals(expectedTitle, notesTabPage.getNoteTitle());
        Assertions.assertEquals(expectedDescription, notesTabPage.getNoteDescription());
    }

    private void createMockNote(String expectedTitle, String expectedDescription, NotesTabPage notesTabPage) {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
        notesTabPage.createNote(expectedTitle, expectedDescription);
        WebElement homePageLink = new WebDriverWait(driver, Duration.ofSeconds(2)).
                until(webDriver -> webDriver.findElement(By.cssSelector(".alert-success a")));
        homePageLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
    }

    @Test
    void testUpdateNote() {

        String username = "UNT";
        String password = "123";

        doMockSignUp("Update Note", "Test", username, password);
        doLogIn(username, password);

        NotesTabPage notesTabPage = new NotesTabPage(driver);
        String title = "Test Title";
        String description = "Test description";
        createMockNote(title, description, notesTabPage);
        String newTitle = "New Test Title";
        String newDescription = "New test description";
        String noteId = notesTabPage.editNote(newTitle, newDescription);
        WebElement homePageLink = new WebDriverWait(driver, Duration.ofSeconds(2)).
                until(webDriver -> webDriver.findElement(By.cssSelector(".alert-success a")));
        homePageLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();

        List<WebElement> notes = notesTabPage.getNotes();
        Optional<WebElement> editedNote = notes.stream()
                .filter(
                        note ->
                                note.findElement(By.cssSelector("td:first-of-type button"))
                                        .getAttribute("data-id").equals(noteId)
                )
                .findFirst();

        Assertions.assertTrue(editedNote.isPresent());
        Assertions.assertEquals(newTitle, editedNote.get().findElement(By.cssSelector("th:first-of-type")).getText());
        Assertions.assertEquals(newDescription, editedNote.get().findElement(By.cssSelector("td:last-of-type")).getText());
    }

    @Test
    void testDeleteNote() {

        String username = "DNT";
        String password = "123";

        doMockSignUp("Delete Note", "Test", username, password);
        doLogIn(username, password);

        NotesTabPage notesTabPage = new NotesTabPage(driver);
        String title = "Test Title";
        String description = "Test description";
        createMockNote(title, description, notesTabPage);

        WebElement deleteNoteBtn = notesTabPage.getDeleteNoteBtn();
        String[] urlStrings = deleteNoteBtn.getAttribute("href").split("/");
        String noteId = urlStrings[urlStrings.length - 1];
        deleteNoteBtn.click();
        WebElement homePageLink = new WebDriverWait(driver, Duration.ofSeconds(2)).
                until(webDriver -> webDriver.findElement(By.cssSelector(".alert-success a")));
        homePageLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();

        List<WebElement> notes = notesTabPage.getNotes();
        Optional<WebElement> deletedNote = notes.stream()
                .filter(
                        note ->
                                note.findElement(By.cssSelector("td:first-of-type a"))
                                        .getAttribute("href").split("/")[2].equals(noteId)
                )
                .findFirst();

        Assertions.assertFalse(deletedNote.isPresent());
    }

    @Test
    void testSaveCredential() {

        String username = "SCT";
        String password = "123";

        doMockSignUp("Save Credential", "Test", username, password);
        doLogIn(username, password);

        CredentialTabPage credentialTabPage = new CredentialTabPage(driver);
        String credentialUrl = "Test url";
        String credentialUsername = "Test username";
        String credentialPassword = "Test password";
        saveMockCredential(credentialUrl, credentialUsername, credentialPassword, credentialTabPage);
        Assertions.assertEquals(credentialUrl, credentialTabPage.getCredentialUrl());
        Assertions.assertEquals(credentialUsername, credentialTabPage.getCredentialUsername());
        Assertions.assertNotEquals(credentialPassword, credentialTabPage.getCredentialPassword());
    }

    private void saveMockCredential(String url, String username, String password, CredentialTabPage credentialTabPage) {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
        credentialTabPage.storeCredential(url, username, password);
        WebElement homePageLink = new WebDriverWait(driver, Duration.ofSeconds(2)).
                until(webDriver -> webDriver.findElement(By.cssSelector(".alert-success a")));
        homePageLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
    }

    @Test
    void testUpdateCredential() {

        String username = "UCT";
        String password = "123";

        doMockSignUp("Update Credential", "Test", username, password);
        doLogIn(username, password);

        CredentialTabPage credentialTabPage = new CredentialTabPage(driver);
        String credentialUrl = "Test url";
        String credentialUsername = "Test username";
        String credentialPassword = "Test password";
        saveMockCredential(credentialUrl, credentialUsername, credentialPassword, credentialTabPage);
        String newUrl = "New Test url";
        String newUsername = "New test username";
        String newPassword = "New test password";
        String credentialId = credentialTabPage.editCredential(newUrl, newUsername, newPassword);
        WebElement homePageLink = new WebDriverWait(driver, Duration.ofSeconds(2)).
                until(webDriver -> webDriver.findElement(By.cssSelector(".alert-success a")));
        homePageLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();

        List<WebElement> credentials = credentialTabPage.getCredentials();
        Optional<WebElement> editedCredential = credentials.stream()
                .filter(
                        credential ->
                                credential.findElement(By.cssSelector("td:first-of-type button"))
                                        .getAttribute("data-id").equals(credentialId)
                )
                .findFirst();

        Assertions.assertTrue(editedCredential.isPresent());
        Assertions.assertEquals(newUrl, editedCredential.get().findElement(By.cssSelector("th:first-of-type")).getText());
        Assertions.assertEquals(newUsername, editedCredential.get().findElement(By.cssSelector("td:nth-of-type(2)")).getText()); // td:nth-last-child(2)
        Assertions.assertNotEquals(newPassword, editedCredential.get().findElement(By.cssSelector("td:last-of-type")).getText());
        Assertions.assertEquals(newPassword, credentialTabPage.getCredentialPasswordInput()); // split into separate test
    }

    @Test
    void testDeleteCredential() {

        String username = "DCT";
        String password = "123";

        doMockSignUp("Delete Credential", "Test", username, password);
        doLogIn(username, password);

        CredentialTabPage credentialTabPage = new CredentialTabPage(driver);
        String credentialUrl = "Test url";
        String credentialUsername = "Test username";
        String credentialPassword = "Test password";
        saveMockCredential(credentialUrl, credentialUsername, credentialPassword, credentialTabPage);

        WebElement deleteCredentialBtn = credentialTabPage.getDeleteCredentialBtn();
        String[] urlStrings = deleteCredentialBtn.getAttribute("href").split("/");
        String credentialId = urlStrings[urlStrings.length - 1];
        deleteCredentialBtn.click();
        WebElement homePageLink = new WebDriverWait(driver, Duration.ofSeconds(2)).
                until(webDriver -> webDriver.findElement(By.cssSelector(".alert-success a")));
        homePageLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();

        List<WebElement> credentials = credentialTabPage.getCredentials();
        Optional<WebElement> deletedCredential = credentials.stream()
                .filter(
                        credential ->
                                credential.findElement(By.cssSelector("td:first-of-type a"))
                                        .getAttribute("href").split("/")[2].equals(credentialId)
                )
                .findFirst();

        Assertions.assertFalse(deletedCredential.isPresent());
    }
}
