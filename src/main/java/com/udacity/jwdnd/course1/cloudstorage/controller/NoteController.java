package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String createNote(Authentication authentication, @ModelAttribute("noteForm") Note newNote, Model model) {

        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        newNote.setUserId(userId);

        if (this.noteService.createNote(newNote) < 0) {
            model.addAttribute("error",
                    "There was an error creating the note. Please try again after some time.");
        } else {
            model.addAttribute("success",
                    "Note created successfully.");
        }

        return "result";
    }
}
