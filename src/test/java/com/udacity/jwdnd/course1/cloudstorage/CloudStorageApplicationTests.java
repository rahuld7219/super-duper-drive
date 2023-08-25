package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    private static String tmpDownloadsDirectory;

    private String baseURL;

    private ResultPage resultPage;

    @BeforeAll
    static void beforeAll() {

        // configure Firefox to download files to the user's current working directory/testDownloads.
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        tmpDownloadsDirectory = System.getProperty("user.dir") + File.separator + "testDownloads";
        // specifying that files should be saved to a specific directory without asking for user confirmation
        firefoxOptions.addPreference("browser.download.folderList", 2);
        //  sets the actual path to the directory where downloaded files should be stored.
        firefoxOptions.addPreference("browser.download.dir", tmpDownloadsDirectory);
        //  ensures that the custom directory specified in browser.download.dir is used for all downloads
        firefoxOptions.addPreference("browser.download.useDownloadDir", true);
        // ensures that files of any MIME type are automatically saved without user prompts.
        firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk", "");

        driver = new FirefoxDriver(firefoxOptions);
    }

    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + this.port;
        resultPage = new ResultPage(driver);
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
            Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
            Assertions.fail(e.getMessage());
        }

    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {

        driver.get(this.baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        try {
            loginPage.login(userName, password);
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
            Assertions.fail(e.getMessage());
        }
        Assertions.assertEquals(this.baseURL + "/home", driver.getCurrentUrl());

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
        doLogout();

        driver.get(this.baseURL + "/home");

        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.titleContains("Login"));

        Assertions.assertEquals(this.baseURL + "/login", driver.getCurrentUrl());
    }

    @Test
    void testFileUpload() {
        String username = "FUT";
        String password = "123";

        doMockSignUp("File Upload", "Test", username, password);
        doLogIn(username, password);

        FilesTabPage filesTabPage = new FilesTabPage(driver);
        String fileName = "upload5m.zip";
        File file = new File(fileName);
        mockUploadFile(filesTabPage, file);

    }

    @Test
    void testEmptyFileUpload() {

        String username = "EFUT";
        String password = "123";

        doMockSignUp("Empty File Upload", "Test", username, password);
        doLogIn(username, password);

        FilesTabPage filesTabPage = new FilesTabPage(driver);
        String fileName = "new_empty_file.txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        boolean fileCreated = false;
        try {
            fileCreated = file.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating empty file: " + e.getMessage());
        }
        Assertions.assertTrue(fileCreated);
        if (fileCreated) {
            mockUploadFile(filesTabPage, file);
            file.delete();
        }
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

        String username = "LFUT";
        String password = "123";

        doMockSignUp("Large File Upload", "Test", username, password);
        doLogIn(username, password);

        FilesTabPage filesTabPage = new FilesTabPage(driver);
        String fileName = "upload5m.zip";
        File file = new File(fileName);
        mockUploadFile(filesTabPage, file);
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    private void mockUploadFile(FilesTabPage filesTabPage, File file) {

        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab"))).click();
            filesTabPage.uploadFile(file.getAbsolutePath());

            // file.length() will return 0 for both empty files and files that do not exist
            if (file.length() == 0) {
                if (this.resultPage.isError()) {
                    Assertions.assertTrue(this.resultPage.getCustomErrorMsg().contains("Cannot upload empty file."));
                } else {
                    Assertions.fail("Empty file upload behaviour was not as expected.");
                }
            } else if (this.resultPage.isSuccess()) {
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("upload successful"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab"))).click();
                Assertions.assertEquals(file.getName(), filesTabPage.getFileTitle());
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }
    }

    @Test
    void testFileDownload() throws InterruptedException {

        String username = "FDT";
        String password = "123";

        doMockSignUp("File Download", "Test", username, password);
        doLogIn(username, password);

        FilesTabPage filesTabPage = new FilesTabPage(driver);
        final String fileName = "upload5m.zip";
        File file = new File(fileName);
        mockUploadFile(filesTabPage, file);

        mockDownloadFile(filesTabPage, fileName);
    }

    private void mockDownloadFile(FilesTabPage filesTabPage, String fileName) {

        Path downloadPath = Paths.get(tmpDownloadsDirectory);
        Path downloadedFilePath = Paths.get(tmpDownloadsDirectory, fileName);

        // check before downloading that the downloadPath not exists
        if (Files.exists(downloadPath)) {
            deleteDownloadPath(downloadPath);
        }

        // download the file
        if (!Files.exists(downloadPath)) {
            try {
                Files.createDirectory(downloadPath);
                filesTabPage.downloadFile();
                // Wait for atmost 60 sec period for the file to appear
                if (!waitForFile(downloadPath, fileName, 60, TimeUnit.SECONDS)) {
                    Assertions.fail("Downloading file was taking too much time.");
                }
            } catch (IOException e) {
                System.out.println("Failed to create directory: " + e.getMessage());
            } catch (TimeoutException timeoutException) {
                System.out.println(timeoutException.getMessage());
                Assertions.fail();
            }
        }

        // check the file downloaded
        if (Files.exists(downloadedFilePath)) {
            long fileSize = 0;
            try {
                fileSize = Files.size(downloadedFilePath);
            } catch (IOException e) {
                System.out.println("Error reading downloaded file size: " + e.getMessage());
            }
            Assertions.assertTrue(fileSize > 0);
        } else {
            Assertions.fail("File not downloaded successfully.");
        }

        deleteDownloadPath(downloadPath);
    }

    @Test
    void testDeleteFile() {
        String username = "DFT";
        String password = "123";

        doMockSignUp("Delete File", "Test", username, password);
        doLogIn(username, password);

        FilesTabPage filesTabPage = new FilesTabPage(driver);
        final String fileName = "upload5m.zip";
        File file = new File(fileName);
        mockUploadFile(filesTabPage, file);

        deleteFile(filesTabPage);
    }

    private void deleteFile(FilesTabPage filesTabPage) {
        try {
            final String deletedFileTitle = filesTabPage.deleteFile();
            if (this.resultPage.isSuccess()) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("File deleted successfully"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab"))).click();
                List<WebElement> files = filesTabPage.getFiles();
                Optional<WebElement> deletedFile = files.stream()
                        .filter(
                                f ->
                                        f.findElement(By.cssSelector("th"))
                                                .getText().equals(deletedFileTitle)
                        )
                        .findFirst();

                Assertions.assertFalse(deletedFile.isPresent());
            } else {
                Assertions.fail();
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }

    }

    private void deleteDownloadPath(Path downloadPath) {
        if (Files.exists(downloadPath)) {
            // delete downloadPath directory and all its content
            try {
                Files.walk(downloadPath) // walks the depth 0 also (the path itself)
                        .sorted(Comparator.reverseOrder()) // so that innermost path processed first to ensure directory is empty before deleting it.
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                System.out.println("Error deleting path: " + e.getMessage());
                            }
                        });
            } catch (IOException e) {
                System.out.println("Error reading download directory while deleting: " + e.getMessage());
            }
            Assertions.assertFalse(Files.exists(downloadPath));
        }
    }

    private boolean waitForFile(Path directory, String fileName, long timeout, TimeUnit timeUnit) {
        long endTime = System.currentTimeMillis() + timeUnit.toMillis(timeout);

        while (System.currentTimeMillis() < endTime) {
            if (Files.exists(directory.resolve(fileName))) {
                return true;
            }
            try {
                Thread.sleep(1000); // Wait for 1 second before checking again
            } catch (InterruptedException e) {
                System.out.println("Waiting for file download interrupted: " + e.getMessage());
            }
        }
        return false;
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
        String title = "Test Title";
        String description = "Test Description";
        createMockNote(title, description, notesTabPage);

    }

    private void createMockNote(String title, String description, NotesTabPage notesTabPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
            notesTabPage.createNote(title, description);
            if (this.resultPage.isSuccess()) {
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("Note created successfully"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
                Assertions.assertEquals(title, notesTabPage.getNoteTitle());
                Assertions.assertEquals(description, notesTabPage.getNoteDescription());
            } else {
                Assertions.fail();
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }
    }

    @Test
    void testUpdateNote() {

        String username = "UNT";
        String password = "123";

        doMockSignUp("Update Note", "Test", username, password);
        doLogIn(username, password);

        NotesTabPage notesTabPage = new NotesTabPage(driver);
        createMockNote("Test Title", "Test description", notesTabPage);
        updateNote(notesTabPage, "New Test Title", "New test description");
    }

    private void updateNote(NotesTabPage notesTabPage, String newTitle, String newDescription) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
            String noteId = notesTabPage.editNote(newTitle, newDescription);
            if (this.resultPage.isSuccess()) {
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("Note updated successfully"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
                List<WebElement> notes = notesTabPage.getNotes();
                Optional<WebElement> editedNote = notes.stream()
                        .filter(note -> note.findElement(By.cssSelector("td:first-of-type button"))
                                .getAttribute("data-id").equals(noteId))
                        .findFirst();
                Assertions.assertTrue(editedNote.isPresent());
                Assertions.assertEquals(newTitle, editedNote.get().findElement(By.cssSelector("th:first-of-type")).getText());
                Assertions.assertEquals(newDescription, editedNote.get().findElement(By.cssSelector("td:last-of-type")).getText());
            } else {
                Assertions.fail();
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }
    }

    @Test
    void testDeleteNote() {

        String username = "DNT";
        String password = "123";

        doMockSignUp("Delete Note", "Test", username, password);
        doLogIn(username, password);

        NotesTabPage notesTabPage = new NotesTabPage(driver);
        createMockNote("Test Title", "Test description", notesTabPage);

        deleteNote(notesTabPage);

    }

    private void deleteNote(NotesTabPage notesTabPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
            final String deletedNoteId = notesTabPage.deleteNote();
            if (this.resultPage.isSuccess()) {
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("Note deleted successfully"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
                List<WebElement> notes = notesTabPage.getNotes();
                Optional<WebElement> deletedNote = notes.stream()
                        .filter(note -> note.findElement(By.cssSelector("td:first-of-type a"))
                                .getAttribute("href").split("/")[2].equals(deletedNoteId))
                        .findFirst();

                Assertions.assertFalse(deletedNote.isPresent());
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }
    }

    @Test
    void testSaveCredential() {

        String username = "SCT";
        String password = "123";

        doMockSignUp("Save Credential", "Test", username, password);
        doLogIn(username, password);

        CredentialTabPage credentialTabPage = new CredentialTabPage(driver);
        mockSaveCredential("Test url", "Test username", "Test password", credentialTabPage);

    }

    private void mockSaveCredential(String credentialUrl, String credentialUsername, String credentialPassword, CredentialTabPage credentialTabPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
            credentialTabPage.storeCredential(credentialUrl, credentialUsername, credentialPassword);
            if (this.resultPage.isSuccess()) {
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("Credentials stored successfully"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
                Assertions.assertEquals(credentialUrl, credentialTabPage.getCredentialUrl());
                Assertions.assertEquals(credentialUsername, credentialTabPage.getCredentialUsername());
                Assertions.assertNotEquals(credentialPassword, credentialTabPage.getCredentialPassword());
            } else {
                Assertions.fail();
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }

    }

    @Test
    void testUpdateCredential() {

        String username = "UCT";
        String password = "123";

        doMockSignUp("Update Credential", "Test", username, password);
        doLogIn(username, password);

        CredentialTabPage credentialTabPage = new CredentialTabPage(driver);
        mockSaveCredential("Test url", "Test username", "Test password", credentialTabPage);
        mockUpdateCredential(credentialTabPage, "New Test url", "New test username", "New test password");

    }

    private void mockUpdateCredential(CredentialTabPage credentialTabPage, String newUrl, String newUsername, String newPassword) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
            String credentialId = credentialTabPage.updateCredential(newUrl, newUsername, newPassword);
            if (this.resultPage.isSuccess()) {
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("Credentials updated successfully"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
                List<WebElement> credentials = credentialTabPage.getCredentials();
                Optional<WebElement> editedCredential = credentials.stream()
                        .filter(credential -> credential.findElement(By.cssSelector("td:first-of-type button"))
                                .getAttribute("data-id").equals(credentialId))
                        .findFirst();

                Assertions.assertTrue(editedCredential.isPresent());
                Assertions.assertEquals(newUrl, editedCredential.get().findElement(By.cssSelector("th:first-of-type")).getText());
                Assertions.assertEquals(newUsername, editedCredential.get().findElement(By.cssSelector("td:nth-of-type(2)")).getText());
                Assertions.assertNotEquals(newPassword, editedCredential.get().findElement(By.cssSelector("td:last-of-type")).getText());
                Assertions.assertEquals(newPassword, credentialTabPage.getCredentialPasswordInput());
            } else {
                Assertions.fail();
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }
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
        mockSaveCredential(credentialUrl, credentialUsername, credentialPassword, credentialTabPage);
        mockDeleteCredential(credentialTabPage);
    }

    private void mockDeleteCredential(CredentialTabPage credentialTabPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
            String credentialId = credentialTabPage.deleteCredential();
            if (this.resultPage.isSuccess()) {
                Assertions.assertTrue(this.resultPage.getSuccessMsg().contains("Credentials removed successfully"));
                this.resultPage.redirectOnSuccess();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
                List<WebElement> credentials = credentialTabPage.getCredentials();
                Optional<WebElement> deletedCredential = credentials.stream()
                        .filter(credential -> credential.findElement(By.cssSelector("td:first-of-type a")).getAttribute("href")
                                .split("/")[2].equals(credentialId))
                        .findFirst();
                Assertions.assertFalse(deletedCredential.isPresent());
            } else {
                Assertions.fail();
            }
        } catch (TimeoutException timeoutException) {
            System.out.println(timeoutException.getMessage());
            Assertions.fail(timeoutException.getMessage());
        }

    }
}
