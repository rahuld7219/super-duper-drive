package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM Credentials WHERE userid = #{userId}")
    List<Credential> getCredentialsByUserId(Integer userId);

    @Insert("INSERT INTO Credentials(url, username, key, password, userid) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int saveCredential(Credential credential);

    @Update("UPDATE Credentials SET url = #{url}, key = #{key}, username = #{username}, password = #{password} " +
            "WHERE credentialid = #{credentialId} AND userid = #{userId}")
    int updateCredential(Credential credential);

    @Delete("DELETE FROM Credentials WHERE credentialid = #{credentialId} AND userid = #{userId}")
    void deleteCredentialByCredentialIdAndUserId(Integer credentialId, Integer userId);
}
