package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM Notes WHERE userid = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    @Insert("INSERT INTO Notes(notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int saveNote(Note note);

    @Update("UPDATE Notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} " +
            "WHERE noteid = #{noteId}")
    int updateNote(Note note);

    @Delete("DELETE FROM Notes WHERE noteid = #{noteId} AND userid = #{userId}")
    void deleteNoteByNoteIdAndUserId(Integer noteId, Integer userId);

}
