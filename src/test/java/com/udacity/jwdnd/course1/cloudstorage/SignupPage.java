package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    private WebElement firstNameInput;

    @FindBy(id = "inputLastName")
    private WebElement lastNameInput;

    @FindBy(id = "inputUsername")
    private WebElement usernameInput;

    @FindBy(id = "inputPassword")
    private WebElement passwordInput;

    @FindBy(id = "buttonSignUp")
    private WebElement signUpButton;

    private final WebDriver driver;

    public SignupPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void waitForVisibilityOf(WebElement... webElements) {
        new WebDriverWait(this.driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void signup(String firstName, String lastName, String username, String password) {
        waitForVisibilityOf(firstNameInput, lastNameInput, usernameInput, passwordInput, signUpButton);
        this.firstNameInput.sendKeys(firstName);
        this.lastNameInput.sendKeys(lastName);
        this.usernameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
        this.signUpButton.submit();
        new WebDriverWait(this.driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
    }

}
