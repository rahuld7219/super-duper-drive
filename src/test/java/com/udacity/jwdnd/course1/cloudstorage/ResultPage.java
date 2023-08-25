package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ResultPage {

    private final WebDriver webDriver;

    @FindBy(id = "success")
    private WebElement successHeader;

    @FindBy(id = "error")
    private WebElement errorHeader;

    @FindBy(id = "success-msg")
    private WebElement successMsg;

    @FindBy(id = "success-redirect")
    private WebElement redirectLinkOnSuccess;

    @FindBy(id = "custom-error-msg")
    private WebElement customErrorMsg;

    @FindBy(id = "error-redirect")
    private WebElement redirectLinkOnError;

    @FindBy(id = "error-status-code")
    private WebElement errorHttpStatusCode;

    @FindBy(id = "error-msg")
    private WebElement errorMsg;

    public ResultPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForVisibilityOf(WebElement... webElements) {
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public String getSuccessMsg() {
        this.waitForVisibilityOf(this.successMsg);
        return successMsg.getText();
    }

    public void redirectOnSuccess() {
        this.waitForVisibilityOf(this.redirectLinkOnSuccess);
        this.redirectLinkOnSuccess.click();
    }

    public String getCustomErrorMsg() {
        this.waitForVisibilityOf(this.customErrorMsg);
        return this.customErrorMsg.getText();
    }

    public void redirectOnError() {
        this.waitForVisibilityOf(this.redirectLinkOnError);
        this.redirectLinkOnError.click();
    }

    public boolean isSuccess() {
        try {
            this.waitForVisibilityOf(this.successHeader);
        } catch (TimeoutException timeoutException) {
            return false;
        }
        return true;
    }

    public boolean isError() {
        try {
            this.waitForVisibilityOf(this.errorHeader);
        } catch (TimeoutException timeoutException) {
            return false;
        }
        return true;
    }
}
