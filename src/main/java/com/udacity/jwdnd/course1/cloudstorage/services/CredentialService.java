package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getUserCredentials(Integer userId) {
        return this.credentialMapper.getCredentialsByUserId(userId);
    }

    public int storeCredential(Credential credential) {
        return this.credentialMapper.saveCredential(credential);
    }

    public int updateCredential(Credential credential) {
        return this.credentialMapper.updateCredential(credential);
    }

    public void deleteCredential(Integer credentialId, Integer userId) {
        this.credentialMapper.deleteCredentialByCredentialIdAndUserId(credentialId, userId);
    }
}
