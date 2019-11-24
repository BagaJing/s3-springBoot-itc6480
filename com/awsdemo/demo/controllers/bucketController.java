package com.awsdemo.demo.controllers;

import com.awsdemo.demo.queue.DeferredResultHolder;
import com.awsdemo.demo.queue.actionsQueue;
import com.awsdemo.demo.services.amazonClient;
import com.awsdemo.demo.utils.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;


@RestController
@RequestMapping("/s3/")
public class bucketController {

    private amazonClient amazonClient;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static int DEFALUT_ORDER_LENGTH = 8;
    @Autowired
    bucketController(amazonClient amazonClient){
        this.amazonClient = amazonClient;
    }
    @Autowired
    private actionsQueue myQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    @PostMapping("/upload")
    public DeferredResult<String> Upload(@RequestParam(value = "files") MultipartFile[] files,@RequestParam(value = "subDir") String dir){
        logger.info("Main Thread receive upload task ");
        String orderNum = "UP"+utils.getRandomOrderNum(DEFALUT_ORDER_LENGTH);
       // myQueue.setUploadOrder(orderNum,files,dir);

        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(orderNum,result);
        logger.info("Main Thread release");
        return result;
        /*
        Callable<String> result = ()->{
            logger.info("task Thread start");
            String status = this.amazonClient.batchUploadFiles(files);
            logger.info("task Thread return");
            return status;
        };*/
    }

    @GetMapping("/listBucketObjects")
    public List<String> getKeys(@RequestParam(value = "subDir") String subDir){
        return this.amazonClient.getKeyFromS3Bucket(subDir);
    }
    @GetMapping("/download")
    public Callable<ResponseEntity<Resource>> downloadFile(@RequestPart(value = "fileName") String fileName) throws IOException {
        logger.info("Main Thread receive download task");
        Callable<ResponseEntity<Resource>> result = ()->{
            logger.info("task Thread start");
            ResponseEntity<Resource> resource = this.amazonClient.download(fileName);
            logger.info("task Thread return");
            return resource;
        };
        logger.info("Main Thread release");
        return result;
    }
    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam(value = "fileNames") String[] fileNames){
        String result = "";
        try {
            for (int i = 0 ; i < fileNames.length;i++){
                this.amazonClient.deleteFile(fileNames[i]);
            }
        }catch (Exception e){
            result = e.getMessage();
            e.printStackTrace();
        }
        if (result == "") result = "delete completed";
        return result;
    }

    @PostMapping("/renameFolder")
    public DeferredResult<String> renameFolder(@RequestParam(value = "oldName") String oldName,@RequestParam(value = "newName") String newName){
        logger.info("Main Thread receive folder rename task");
        String order = "RFO"+utils.getRandomOrderNum(DEFALUT_ORDER_LENGTH);
        //myQueue.setRenameFolderOrder(order,oldName,newName);
        DeferredResult<String> result = new DeferredResult<String>();
        resultHolder.getMap().put(order,result);
        logger.info("Main Thread release");
        return result;
    }
    @PostMapping("/renameFile")
    public DeferredResult<String> reNameFile(@RequestParam(value = "oldName") String oldName,
                             @RequestParam(value = "newName") String newName){
        logger.info("Main thread receive file rename task");
        String order = "RFI"+utils.getRandomOrderNum(DEFALUT_ORDER_LENGTH);
       // myQueue.setRenameFileOrder(order,oldName,newName);
        DeferredResult<String> result = new DeferredResult<String>();
        resultHolder.getMap().put(order,result);
        logger.info("Main Thread release");
        return result;
    }
    @PostMapping("/deleteFolder")
    public String delteFolder(@RequestParam(value = "prefix") String prefix){
        if (prefix.equals("")) return "No prefix caught";
        amazonClient.deleteFolder(prefix);
        return "deleted";
    }
}
