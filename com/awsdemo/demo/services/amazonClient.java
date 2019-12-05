package com.awsdemo.demo.services;

import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Future;
public interface amazonClient {

     String uploadFile(MultipartFile multipartFile,String folderName);
     String batchUploadFiles(MultipartFile[] multipartFiles,String dir,String folderName);
     List<String> getKeyFromS3Bucket(String subDir,String folderName);
     ResponseEntity<byte[]> download(String fileName) throws IOException;
     ResponseEntity<byte[]> downloadFolder(String path) throws IOException;
     //ResponseEntity<Resource> download(String fileName) throws IOException;
     //ResponseEntity<Resource> downloadFolder(String path) throws IOException;
     void deleteFile(String fileName);
     void deleteFolder(String prefix,String folderName);
     void renameFolder(String oldPrefix,String newFolder,int level);
     boolean renameFile(String oldName,String newName);
     boolean createCustomer(String nickname);
}
