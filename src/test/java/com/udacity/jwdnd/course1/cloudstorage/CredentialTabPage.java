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

    /**
     * get the last row's edit button
     */
    @FindBys({
            @FindBy(id = "credential-table"),
            @FindBy(css = ".btn-success:last-of-type")}
    )
    private WebElement editCredentialBtn;

    /**
     * get the last row's delete button
     */
    @FindBy(css = "#credential-table .btn-danger:last-of-type")
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
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2)).until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void storeCredential(String url, String username, String password) {
        this.waitForVisibilityOf(this.addNewCredentialBtn);
        this.addNewCredentialBtn.click();
        this.waitForVisibilityOf(this.credentialUrlInput, this.credentialUsernameInput, this.credentialPasswordInput);
        this.credentialUrlInput.sendKeys(url);
        this.credentialUsernameInput.sendKeys(username);
        this.credentialPasswordInput.sendKeys(password);
        this.waitForVisibilityOf(this.saveCredentialBtn);
        this.saveCredentialBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
    }

    public String updateCredential(String newUrl, String newUsername, String newPassword) {

        this.waitForVisibilityOf(this.editCredentialBtn);
        this.editCredentialBtn.click();
        this.waitForVisibilityOf(this.credentialUrlInput, this.credentialUsernameInput, this.credentialPasswordInput);
        String credentialId = this.credentialIdInput.getAttribute("value");
        this.credentialUrlInput.clear();
        this.credentialUrlInput.sendKeys(newUrl);
        this.credentialUsernameInput.clear();
        this.credentialUsernameInput.sendKeys(newUsername);
        this.credentialPasswordInput.clear();
        this.credentialPasswordInput.sendKeys(newPassword);
        this.waitForVisibilityOf(this.saveCredentialBtn);
        this.saveCredentialBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
        return credentialId;
    }

    public String deleteCredential() {
        this.waitForVisibilityOf(this.deleteCredentialBtn);
        String[] urlStrings = this.deleteCredentialBtn.getAttribute("href").split("/");
        String credentialId = urlStrings[urlStrings.length - 1];
        this.deleteCredentialBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
        return credentialId;
    }

    public String getCredentialUrl() {
        this.waitForVisibilityOf(this.credentialUrl);
        return this.credentialUrl.getText();
    }

    public String getCredentialPassword() {
        this.waitForVisibilityOf(this.credentialPassword);
        return this.credentialPassword.getText();
    }

    public String getCredentialPasswordInput() {
        this.waitForVisibilityOf(this.editCredentialBtn);
        this.editCredentialBtn.click();
        this.waitForVisibilityOf(this.credentialPasswordInput);
        return this.credentialPasswordInput.getAttribute("value");
    }

    public List<WebElement> getCredentials() {
        try {
            this.waitForVisibilityOf(this.credentials.toArray(WebElement[]::new));
        } catch (TimeoutException timeoutException) {
            return new ArrayList<>();
        }
        return this.credentials;
    }

    public String getCredentialUsername() {
        this.waitForVisibilityOf(this.credentialUsername);
        return this.credentialUsername.getText();
    }

}
