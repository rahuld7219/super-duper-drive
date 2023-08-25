package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("credential")
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("save")
    public String storeCredentials(Authentication authentication, @ModelAttribute("credentialForm") Credential credential, Model model) {

        Integer userId = this.userService.getUser(authentication.getName()).getUserId();

        credential.setUserId(userId);
        String encryptionKey = this.encryptionService.generateKey();
        credential.setKey(encryptionKey);
        credential.setPassword(this.encryptionService.encryptValue(credential.getPassword(), encryptionKey));

        if (Objects.isNull(credential.getCredentialId())) {
            if (this.credentialService.storeCredential(credential) < 0) {
                model.addAttribute("error", "There was an error saving the credentials. Please try again after some time.");
            } else {
                model.addAttribute("success", "Credentials stored successfully.");
            }
        } else {
            if (this.credentialService.updateCredential(credential) < 0) {
                model.addAttribute("error", "There was an error updating the credentials. Please try again after some time.");
            } else {
                model.addAttribute("success", "Credentials updated successfully.");
            }
        }

        return "result";
    }

    @GetMapping("delete/{credentialId}")
    public String deleteCredential(Authentication authentication, @PathVariable Integer credentialId, Model model) {
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();

        this.credentialService.deleteCredential(credentialId, userId);

        // TODO: handle if credentials not found or any error while deleting the credentials
        model.addAttribute("success", "Credentials removed successfully.");
        //        model.addAttribute("error",
        //        "There was an error removing credentials. Please try again after some time.");

        return "result";
    }
}
