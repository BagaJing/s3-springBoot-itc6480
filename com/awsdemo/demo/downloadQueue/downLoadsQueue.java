package com.awsdemo.demo.downloadQueue;

import com.awsdemo.demo.services.amazonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
//@Import({amazonClient.class})
public class downLoadsQueue {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private amazonClient amazonClient;
    Queue<AbstractMap.SimpleEntry<String,ResponseEntity<Resource>>> queue = new LinkedList<>();

    public ResponseEntity<byte[]> setDownloadFileOrder(String orderId, String path) throws IOException{
        return amazonClient.download(path);
    }
    /*
    @Async("taskAsyncPool")
    public void setDownloadFileOrder(String orderId, String path) throws IOException {
        ResponseEntity<Resource> res = amazonClient.download(path);
        AbstractMap.SimpleEntry<String,ResponseEntity<Resource>> entry = new AbstractMap.SimpleEntry<>(orderId,res);
        queue.offer(entry);
    }

     */

    public ResponseEntity<byte[]> setDownloadFolderOrder(String orderId,String path) throws IOException{
        return amazonClient.downloadFolder(path);
    }
    /*
    @Async("taskAsyncPool")
    public void setDownloadFolderOrder(String orderId,String path) throws IOException{
        ResponseEntity<Resource> res = amazonClient.downloadFolder(path);
        AbstractMap.SimpleEntry<String,ResponseEntity<Resource>> entry = new AbstractMap.SimpleEntry<>(orderId,res);
        queue.offer(entry);
    }

     */

    public Queue<AbstractMap.SimpleEntry<String, ResponseEntity<Resource>>> getQueue() {
        return queue;
    }

    public void setQueue(Queue<AbstractMap.SimpleEntry<String, ResponseEntity<Resource>>> queue) {
        this.queue = queue;
    }
}