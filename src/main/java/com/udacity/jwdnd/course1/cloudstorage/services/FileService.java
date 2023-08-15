package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class FileService {

    private final UserMapper userMapper;
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    public boolean isFileNameAvailableForUser(Integer userId, String fileName) {
        return Objects.isNull(this.fileMapper.getFileByUserIdAndFilename(userId, fileName));
    }

    public List<File> getUserFiles(Integer userId) {
        return this.fileMapper.getFilesByUserId(userId);
    }

    public int uploadFile(MultipartFile file, Integer userId) {

        try {
            return this.fileMapper.saveFile(
                    new File(null, file.getOriginalFilename(), file.getContentType(),
                            String.valueOf(file.getSize()), userId,
                            file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File downloadFile(Integer fileId, Integer userId) {
        return this.fileMapper.getFileByFileIdAndUserId(fileId, userId);
    }

    public void deleteFile(Integer fileId, Integer userId) {
        this.fileMapper.deleteFileByFileIdAndUserId(fileId, userId);
    }
}
