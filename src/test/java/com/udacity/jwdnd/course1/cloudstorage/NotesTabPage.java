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

public class NotesTabPage {

    private final WebDriver webDriver;

    @FindBy(id = "add-new-note")
    private WebElement addNewNoteBtn;

    /**
     * get the last row's edit button
     */
    @FindBys({
            @FindBy(id = "notes-table"),
            @FindBy(css = ".btn-success:last-of-type")}
    )
    private WebElement editNoteBtn;

    /**
     * get the last row's delete button
     */
    @FindBy(css = "#notes-table .btn-danger:last-of-type")
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
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public void createNote(String title, String description) {
        this.waitForVisibilityOf(this.addNewNoteBtn);
        this.addNewNoteBtn.click();
        this.waitForVisibilityOf(this.noteTitleInput, this.noteDescriptionTextArea);
        this.noteTitleInput.sendKeys(title);
        this.noteDescriptionTextArea.sendKeys(description);
        this.waitForVisibilityOf(this.saveNoteBtn);
        this.saveNoteBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
    }

    public String editNote(String newTitle, String newDescription) {
        this.waitForVisibilityOf(this.editNoteBtn);
        this.editNoteBtn.click();
        this.waitForVisibilityOf(this.noteTitleInput, this.noteDescriptionTextArea);
        String noteId = this.noteIdInput.getAttribute("value");
        this.noteTitleInput.clear();
        this.noteTitleInput.sendKeys(newTitle);
        this.noteDescriptionTextArea.clear();
        this.noteDescriptionTextArea.sendKeys(newDescription);
        this.waitForVisibilityOf(this.saveNoteBtn);
        this.saveNoteBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
        return noteId;
    }

    public String deleteNote() {
        this.waitForVisibilityOf(this.deleteNoteBtn);
        String[] urlStrings = this.deleteNoteBtn.getAttribute("href").split("/");
        String noteId = urlStrings[urlStrings.length - 1];
        this.deleteNoteBtn.click();
        new WebDriverWait(this.webDriver, Duration.ofSeconds(2))
                .until(ExpectedConditions.titleContains("Result"));
        return noteId;
    }

    public String getNoteTitle() {
        this.waitForVisibilityOf(this.noteTitle);
        return this.noteTitle.getText();
    }

    public String getNoteDescription() {
        this.waitForVisibilityOf(this.noteDescription);
        return this.noteDescription.getText();
    }

    public List<WebElement> getNotes() {
        try {
            this.waitForVisibilityOf(this.notes.toArray(WebElement[]::new));
        } catch (TimeoutException timeoutException) {
            return new ArrayList<>();
        }
        return this.notes;
    }

}
