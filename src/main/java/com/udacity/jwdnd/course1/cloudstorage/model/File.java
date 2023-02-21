package com.udacity.jwdnd.course1.cloudstorage.model;

import java.sql.Blob;

public class File {

    private Integer fileId;

    private String filename;

    private String contentType;

    private String fileSize;

    private Integer userId;

    private Blob filedata;

//    private byte[] filedata;

}
