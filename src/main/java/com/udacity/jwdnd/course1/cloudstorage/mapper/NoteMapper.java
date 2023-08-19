package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO Notes(noteid, notetitle, notedescription, userid) " +
            "VALUES(1, #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int saveNote(Note note);

    @Select("SELECT * FROM Notes WHERE userid = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    @Update("UPDATE Notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int updateNote(Note note);

    @Delete("DELETE FROM Notes WHERE noteId = #{noteId} AND userid = #{userId}")
    void deleteNoteByNoteIdAndUserId(Integer noteId, Integer userId);

}
