package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, EncryptionService encryptionService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String homePage(Authentication authentication,
                           @ModelAttribute("noteForm") Note noteForm,
                           @ModelAttribute("credentialForm") Credential credentialForm,
                           Model model) {

        Integer userId = this.userService.getUser(authentication.getName()).getUserId();

        List<File> userFiles = this.fileService.getUserFiles(userId);
        List<Note> userNotes = this.noteService.getUserNotes(userId);
        List<Credential> userCredentials = this.credentialService.getUserCredentials(userId);

        model.addAttribute("userFiles", userFiles);
        model.addAttribute("userNotes", userNotes);
        model.addAttribute("userCredentials", userCredentials);
        model.addAttribute("encryptionService", this.encryptionService);

        return "home";
    }

}
