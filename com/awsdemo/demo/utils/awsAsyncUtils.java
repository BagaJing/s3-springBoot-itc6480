package com.awsdemo.demo.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class awsAsyncUtils {
    /*
     *S3 uploading files method require "File" parameters
     *The springframework provides multipartFile Interface
     * Then convert process is needed
     */
    public File convertMultiPartFile(MultipartFile file) throws IOException {
        System.out.println("awsAsyncUtils. Thread: "+Thread.currentThread().getName());
        System.out.println();
        File targetFile = new File(generateFileName(file));
        FileOutputStream fos = new FileOutputStream(targetFile);
        fos.write(file.getBytes());
        fos.close();
        return targetFile;
    }
    public List<File> covertMultiPartFiles(MultipartFile[] files) throws IOException{
        System.out.println("awsAsyncUtils. Thread: "+Thread.currentThread().getName());
        List<File> newFiles = new ArrayList<>();
        System.out.println();
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
    private String generateFileName(MultipartFile file){
        return new Date().getTime()+"-"+file.getOriginalFilename().replace(" ","_");
    }
    public void printProgressBar(double pct){
        final int bar_size = 40;
        final String empty_bar = "                                        ";
        final String filled_bar = "########################################";
        int amt_full = (int) (bar_size * (pct / 100.0));
        System.out.format("  [%s%s]", filled_bar.substring(0, amt_full),
                empty_bar.substring(0, bar_size - amt_full));
    }
    public void eraseProgressBar(){
        // erase_bar is bar_size (from printProgressBar) + 4 chars.
        final String erase_bar = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
        System.out.format(erase_bar);
    }
}
