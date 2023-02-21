package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO users(username, salt, password, firstname, lastname)" +
            "VALUES(#(username), #(salt), #(password), #(firstname), #(lastname))")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int saveUser(User user);
}
