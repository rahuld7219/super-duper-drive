package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    @FindBy(id = "inputUsername")
    private WebElement usernameInput;

    @FindBy(id = "inputPassword")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void waitForVisibilityOf(WebElement... webElements) {
        new WebDriverWait(this.driver, Duration.ofSeconds(2)).until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void login(String username, String password) {
        waitForVisibilityOf(this.usernameInput, this.passwordInput, this.loginButton);
        this.usernameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
        this.loginButton.submit();
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.titleContains("Home"));
    }
}
