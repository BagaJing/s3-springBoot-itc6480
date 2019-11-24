package com.awsdemo.demo.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Future;

public interface amazonClient {

     String uploadFile(MultipartFile multipartFile);
     String batchUploadFiles(MultipartFile[] multipartFiles,String dir);
     List<String> getKeyFromS3Bucket(String subDir);
     ResponseEntity<Resource> download(String fileName) throws IOException;
     void deleteFile(String fileName);
     void deleteFolder(String prefix);
     void renameFolder_new(String oldPrefix,String newFolder,int level);
     boolean renameFile(String oldName,String newName);
     boolean reNameFolder(String oldFolder,String newFolder);
     Stack<String> reNameFolder_stack(String oldFolder, String newFolder);

}
