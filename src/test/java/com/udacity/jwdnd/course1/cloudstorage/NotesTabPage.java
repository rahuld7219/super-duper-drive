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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NotesTabPage {

    private final WebDriver webDriver;

    @FindBy(id = "add-new-note")
    private WebElement addNewNoteBtn;

    @FindBys({
            @FindBy(id = "notes-table"),
            @FindBy(className = "btn-success")}
    )
    private WebElement editNoteBtn;

    @FindBy(css = "#notes-table .btn-danger")
    private WebElement deleteNoteBtn;

    @FindBy(className = "note-list")
    private List<WebElement> notes;

    @FindBy(css = "#notes-table tbody tr:last-child th")
    private WebElement noteTitle;

    @FindBy(css = "#notes-table tbody tr:last-child td:last-child")
    private WebElement noteDescription;

    @FindBy(id = "note-id")
    private WebElement noteIdInput;

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

    public String editNote(String title, String description) {
        waitForVisibilityOf(this.editNoteBtn);
        this.editNoteBtn.click();
        waitForVisibilityOf(this.noteTitleInput, this.noteDescriptionTextArea);
        String noteId = this.noteIdInput.getAttribute("value");
        this.noteTitleInput.clear();
        this.noteTitleInput.sendKeys(title);
        this.noteDescriptionTextArea.clear();
        this.noteDescriptionTextArea.sendKeys(description);
        waitForVisibilityOf(this.saveNoteBtn);
        this.saveNoteBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
        return noteId;
    }

    public String getNoteTitle() {
        waitForVisibilityOf(this.noteTitle);
        return this.noteTitle.getText();
    }

    public String getNoteDescription() {
        waitForVisibilityOf(this.noteDescription);
        return this.noteDescription.getText();
    }

    public WebElement getNoteIdInput() {
        waitForVisibilityOf(this.noteIdInput);
        return this.noteIdInput;
    }

    public WebElement getNoteTitleInput() {
        waitForVisibilityOf(this.noteTitleInput);
        return this.noteTitleInput;
    }

    public WebElement getNoteDescriptionTextArea() {
        waitForVisibilityOf(this.noteDescriptionTextArea);
        return this.noteDescriptionTextArea;
    }

    public List<WebElement> getNotes() {
        try {
            this.waitForVisibilityOf(this.notes.toArray(WebElement[]::new));

            // below code are equivalent
//            this.waitForVisibilityOf(this.notes.toArray(new WebElement[0]));
//            this.waitForVisibilityOf(this.notes.stream().toArray(WebElement[]::new));

        } catch (TimeoutException timeoutException) {
            return new ArrayList<>();
        }
        return this.notes;
    }

    public WebElement getDeleteNoteBtn() {
        this.waitForVisibilityOf(this.deleteNoteBtn);
        return this.deleteNoteBtn;
    }
}
