package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO Files(filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int saveFile(File file);

    @Select("SELECT * FROM Files WHERE userid = #{userId} AND filename = #{fileName}")
    File getFileByUserIdAndFilename(Integer userId, String fileName);

    @Select("SELECT * FROM Files WHERE userid = #{userId}")
    List<File> getFilesByUserId(Integer userId);

    @Select("SELECT * FROM Files WHERE fileid = #{fileId} AND userid = #{userId}")
    File getFileByFileIdAndUserId(Integer fileId, Integer userId);

    @Delete("DELETE FROM Files WHERE fileid = #{fileId} AND userid = #{userId}")
    void deleteFileByFileIdAndUserId(Integer fileId, Integer userId);
}
