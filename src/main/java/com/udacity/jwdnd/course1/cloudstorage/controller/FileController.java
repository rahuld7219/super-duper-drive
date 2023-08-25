package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Controller
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("upload")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, Model model) {

        Integer userId = userService.getUser(authentication.getName()).getUserId();

        // TODO: handle empty file upload
        if (file.isEmpty()) {
            model.addAttribute("error",
                    "Cannot upload empty file.");
        } else if (!this.fileService.isFileNameAvailableForUser(userId, file.getOriginalFilename())) {
            model.addAttribute("error",
                    "File already exists with the filename: " + file.getOriginalFilename());
        } else if (this.fileService.uploadFile(file, userId) < 0) {
            model.addAttribute("error",
                    "There was an error uploading file. Please try again after some time.");
        } else {
            model.addAttribute("success",
                    "File: " + file.getOriginalFilename() + " upload successful.");
        }

        return "result"; // if used "redirect:/home" then it would redirect to /home, if used "redirect:home" then it would redirect to /file/home
    }

    @GetMapping("download/{fileId}")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, @PathVariable Integer fileId) {

        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        // TODO: handle if file not found or any error while downloading file
        File file = this.fileService.downloadFile(fileId, userId);

        if (Objects.isNull(file)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new ByteArrayResource(file.getFileData()));

    }

    @GetMapping("delete/{fileId}")
    public String deleteFile(Authentication authentication, @PathVariable Integer fileId, Model model) {

        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        // TODO: handle if file not found or any error while deleting file
        this.fileService.deleteFile(fileId, userId);
        model.addAttribute("success",
                "File deleted successfully.");
        //        model.addAttribute("error",
        //        "There was an error removing file. Please try again after some time.");
        return "result";
    }
}
