package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FilesTabPage {

    private final WebDriver webDriver;

    @FindBy(id = "file-upload-input")
    private WebElement fileUploadInput;

    @FindBy(id = "upload-btn")
    private WebElement fileUploadBtn;

    /**
     * get the last row's view button
     */
    @FindBy(css = "#files-table tbody tr:last-child td .btn-success")
    private WebElement downloadFileBtn;

    /**
     * get the last row's delete button
     */
    @FindBy(css = "#files-table tbody tr:last-child td .btn-danger")
    private WebElement deleteFileBtn;

    @FindBy(css = "#files-table tbody tr:last-child th")
    private WebElement fileTitle;

    @FindBy(className = "file-list")
    private List<WebElement> files;

    public FilesTabPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForVisibilityOf(WebElement... webElements) {
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2)).until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void uploadFile(String filePath) {
        this.waitForVisibilityOf(this.fileUploadInput, this.fileUploadBtn);
        this.fileUploadInput.sendKeys(filePath);
        this.fileUploadBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
    }

    public void downloadFile() {
        this.waitForVisibilityOf(this.downloadFileBtn);
        this.downloadFileBtn.click();
    }

    public String deleteFile() {
        this.waitForVisibilityOf(this.deleteFileBtn);
        String deletedFileTitle = this.getFileTitle();
        this.deleteFileBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
        return deletedFileTitle;
    }

    public String getFileTitle() {
        this.waitForVisibilityOf(this.fileTitle);
        return this.fileTitle.getText();
    }

    public List<WebElement> getFiles() {
        try {
            this.waitForVisibilityOf(this.files.toArray(WebElement[]::new));
        } catch (TimeoutException timeoutException) {
            return new ArrayList<>();
        }
        return this.files;
    }
}
