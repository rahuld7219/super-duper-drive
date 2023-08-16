package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NotesTabPage {

    private final WebDriver webDriver;

    @FindBy(id = "add-new-note")
    private WebElement addNewNoteBtn;

    @FindBys({
            @FindBy(id = "notesTable"),
            @FindBy(className = "btn-success")}
    )
    private WebElement editNoteBtn;

    @FindBy(css = "#notesTable .btn-danger")
    private WebElement deleteNoteBtn;

    @FindBy(css = "#notesTable tbody tr:first-child th")
    private WebElement noteTitle;

    @FindBy(css = "#notesTable tbody tr:first-child td:nth-child(2)")
    private WebElement noteDescription;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionTextArea;

    @FindBy(id = "save-note-btn")
    private WebElement saveNoteBtn;

    @FindBy(id = "close-note-btn")
    private WebElement closeNoteBtn;

    public NotesTabPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForVisibilityOf(WebElement... webElements) {
        WebDriverWait webDriverWait = new WebDriverWait(this.webDriver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void createNote(String title, String description) {
        waitForVisibilityOf(this.addNewNoteBtn);
        this.addNewNoteBtn.click();
        waitForVisibilityOf(this.noteTitleInput, this.noteDescriptionTextArea);
        this.noteTitleInput.sendKeys(title);
        this.noteDescriptionTextArea.sendKeys(description);
        waitForVisibilityOf(this.saveNoteBtn);
        this.saveNoteBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
    }

    public String getNoteTitle() {
        waitForVisibilityOf(this.noteTitle);
        return this.noteTitle.getText();
    }

    public String getNoteDescription() {
        waitForVisibilityOf(this.noteDescription);
        return noteDescription.getText();
    }
}
