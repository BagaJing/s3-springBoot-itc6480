package com.awsdemo.demo.controllers;

import com.awsdemo.demo.services.amazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("test")
public class HelloController {
    @Autowired
    private amazonClient aclient;
    @GetMapping("/")
    public String hello(){
        return "test";
    }
    @GetMapping("showlist")
    public List<String> showList(){
        List<String> list = aclient.getKeyFromS3Bucket("");
        return list;
    }
    @PostMapping("upload")
    public String upload(@RequestParam("files")MultipartFile files){
        return aclient.uploadFile(files);
    }
}
