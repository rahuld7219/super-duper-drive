package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CredentialTabPage {

    private final WebDriver webDriver;

    @FindBy(id = "add-new-credential")
    private WebElement addNewCredentialBtn;

    @FindBys({
            @FindBy(id = "credential-table"),
            @FindBy(className = "btn-success")}
    )
    private WebElement editCredentialBtn;

    @FindBy(css = "#credential-table .btn-danger")
    private WebElement deleteCredentialBtn;

    @FindBy(className = "credential-list")
    private List<WebElement> credentials;

    @FindBy(css = "#credential-table tbody tr:last-child th")
    private WebElement credentialUrl;

    @FindBy(css = "#credential-table tbody tr:last-child td:nth-last-child(2)")
    private WebElement credentialUsername;

    @FindBy(css = "#credential-table tbody tr:last-child td:last-child")
    private WebElement credentialPassword;

    @FindBy(id = "credential-id")
    private WebElement credentialIdInput;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlInput;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(id = "save-credential-btn")
    private WebElement saveCredentialBtn;

    @FindBy(id = "close-credential-btn")
    private WebElement closeCredentialBtn;

    public CredentialTabPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForVisibilityOf(WebElement... webElements) {
        WebDriverWait webDriverWait = new WebDriverWait(this.webDriver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void storeCredential(String url, String username, String password) {
        waitForVisibilityOf(this.addNewCredentialBtn);
        this.addNewCredentialBtn.click();
        waitForVisibilityOf(this.credentialUrlInput, this.credentialUsernameInput, this.credentialPasswordInput);
        this.credentialUrlInput.sendKeys(url);
        this.credentialUsernameInput.sendKeys(username);
        this.credentialPasswordInput.sendKeys(password);
        waitForVisibilityOf(this.saveCredentialBtn);
        this.saveCredentialBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
    }

    public String editCredential(String newUrl, String newUsername, String newPassword) {

        waitForVisibilityOf(this.editCredentialBtn);
        this.editCredentialBtn.click();
        waitForVisibilityOf(this.credentialUrlInput, this.credentialUsernameInput, this.credentialPasswordInput);
        String credentiaId = this.credentialIdInput.getAttribute("value");
        this.credentialUrlInput.clear();
        this.credentialUrlInput.sendKeys(newUrl);
        this.credentialUsernameInput.clear();
        this.credentialUsernameInput.sendKeys(newUsername);
        this.credentialPasswordInput.clear();
        this.credentialPasswordInput.sendKeys(newPassword);
        waitForVisibilityOf(this.saveCredentialBtn);
        this.saveCredentialBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
        return credentiaId;
    }

    public String getCredentialUrl() {
        waitForVisibilityOf(this.credentialUrl);
        return this.credentialUrl.getText();
    }

    public String getCredentialPassword() {
        waitForVisibilityOf(this.credentialPassword);
        return this.credentialPassword.getText();
    }

    public WebElement getCredentialIdInput() {
        waitForVisibilityOf(this.credentialIdInput);
        return this.credentialIdInput;
    }

    public WebElement getCredentialUrlInput() {
        waitForVisibilityOf(this.credentialUrlInput);
        return this.credentialUrlInput;
    }

    public WebElement getCredentialUsernameInput() {
        waitForVisibilityOf(this.credentialUsernameInput);
        return this.credentialUsernameInput;
    }

    public String getCredentialPasswordInput() {
        waitForVisibilityOf(this.editCredentialBtn);
        this.editCredentialBtn.click();
        waitForVisibilityOf(this.credentialPasswordInput);
        return this.credentialPasswordInput.getAttribute("value");
    }

    public List<WebElement> getCredentials() {
        try {
            this.waitForVisibilityOf(this.credentials.toArray(WebElement[]::new));

            // below code are equivalent
//            this.waitForVisibilityOf(this.notes.toArray(new WebElement[0]));
//            this.waitForVisibilityOf(this.notes.stream().toArray(WebElement[]::new));

        } catch (TimeoutException timeoutException) {
            return new ArrayList<>();
        }
        return this.credentials;
    }

    public WebElement getDeleteCredentialBtn() {
        this.waitForVisibilityOf(this.deleteCredentialBtn);
        return this.deleteCredentialBtn;
    }

    public String getCredentialUsername() {
        this.waitForVisibilityOf(this.credentialUsername);
        return this.credentialUsername.getText();
    }

}
