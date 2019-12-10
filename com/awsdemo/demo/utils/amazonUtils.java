package com.awsdemo.demo.utils;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class amazonUtils {
    /*
     *S3 uploading files method require "File" parameters
     *The springframework provides multipartFile Interface
     * Then convert process is needed
     */
    public static File convertMultiPartFile(MultipartFile file,String commonPrefix) throws IOException {
        File targetFile = new File("/tmp/"+commonPrefix+"-"+generateFileName(file));
        FileOutputStream fos = new FileOutputStream(targetFile);
        fos.write(file.getBytes());
        fos.close();
        return targetFile;
    }
    public static File covertFileNoCache(MultipartFile mFile) {
        if(!mFile.isEmpty()) {
            CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) mFile;
            DiskFileItem diskFileItem = (DiskFileItem) commonsmultipartfile.getFileItem();
            return diskFileItem.getStoreLocation();
        }
        return null;
    }
    public static List<File> covertMultiPartFiles(MultipartFile[] files) throws IOException{
       // System.out.println("awsAsyncUtils. Thread: "+Thread.currentThread().getName());
        List<File> newFiles = new ArrayList<>();
        FileOutputStream fos;
        for (int i = 0 ; i < files.length;i++){
            File targetFile = new File(generateFileName(files[i]));
            fos = new FileOutputStream(targetFile);
            fos.write(files[i].getBytes());
            newFiles.add(targetFile);
            fos.close();
        }
        return newFiles;
    }
    /*
     * it is possible for uploading the same file more than one time
     * than generating unique name for the file each time is needed
     * use timestamp to generate it
     */
    private static String generateFileName(MultipartFile file){
        return file.getOriginalFilename().replace(" ","_");
    }
    public static void printProgressBar(double pct){
        final int bar_size = 40;
        final String empty_bar = "                                        ";
        final String filled_bar = "########################################";
        int amt_full = (int) (bar_size * (pct / 100.0));
        System.out.format("  [%s%s]", filled_bar.substring(0, amt_full),
                empty_bar.substring(0, bar_size - amt_full));
    }
    public static void eraseProgressBar(){
        // erase_bar is bar_size (from printProgressBar) + 4 chars.
        final String erase_bar = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
        System.out.format(erase_bar);
    }
}
