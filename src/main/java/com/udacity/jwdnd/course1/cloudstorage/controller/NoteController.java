package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("create")
    public String createNote(Authentication authentication, @ModelAttribute("noteForm") Note note, Model model) {

        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        note.setUserId(userId);
        if (Objects.isNull(note.getNoteId())) {
            if (this.noteService.createNote(note) < 0) {
                model.addAttribute("error",
                        "There was an error creating the note. Please try again after some time.");
            } else {
                model.addAttribute("success",
                        "Note created successfully.");
            }
        } else {
            if (this.noteService.updateNote(note) < 0) {
                model.addAttribute("error",
                        "There was an error updating the note. Please try again after some time.");
            } else {
                model.addAttribute("success",
                        "Note updated successfully.");
            }
        }

        return "result";
    }

    @GetMapping("delete/{noteId}")
    public String deleteNote(Authentication authentication, @PathVariable Integer noteId, Model model) {

        Integer userId = this.userService.getUser(authentication.getName()).getUserId();

        this.noteService.deleteNote(noteId, userId);

        model.addAttribute("success",
                "Note deleted successfully.");

        return "result";
    }
}
