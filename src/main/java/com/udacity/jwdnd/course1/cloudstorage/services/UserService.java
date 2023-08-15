package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    /**
     * Returns true if username is available to register (i.e., not exist already)
     *
     * @param username
     * @return
     */
    public boolean isUsernameAvailable(String username) {
        return Objects.isNull(this.getUser(username));
    }

    public User getUser(String username) {
        return this.userMapper.getUser(username);
    }
    public int createUser(User user) {

        /*
        Generate a salt of 16 random bytes
         */
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        /*
            encode the salt in Base64
         */
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        return this.userMapper.saveUser(
                new User(null, user.getUsername(), encodedSalt,
                        hashedPassword, user.getFirstName(), user.getLastName())
        );
    }
}
