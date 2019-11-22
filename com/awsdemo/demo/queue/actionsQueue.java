package com.awsdemo.demo.queue;

import com.awsdemo.demo.services.amazonClient;
import com.awsdemo.demo.utils.contentTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class actionsQueue {
    @Autowired
    private amazonClient amazonClient;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String placeOrder;
    private Queue<String> queue = new LinkedList<>();
    @Async("taskAsyncPool")
    public void setIndexShowOrder(String placeOrder, Model model,String subDir){
        logger.info("PageShow Order Received : "+placeOrder);
        List<String> list = amazonClient.getKeyFromS3Bucket(subDir);
        List<List<String>> res = contentTypeUtils.addTypes(list);
        model.addAttribute("objects",res);
        model.addAttribute("rootDir",subDir.split("/"));
        queue.offer(placeOrder);
    }
    @Async("taskAsyncPool")
    public void setGoToOrder(String placeOrder,Model model,String subDir){
        logger.info("GoTo Order Received : "+placeOrder);
        List<String> list = amazonClient.getKeyFromS3Bucket(subDir);
        List<List<String>> res = contentTypeUtils.addTypes(list);
        model.addAttribute("objects",res);
        model.addAttribute("rootDir",subDir);
        queue.offer(placeOrder);
    }
    @Async("taskAsyncPool")
    public void setUploadOrder(String placeOrder, MultipartFile[] files,String dir,RedirectAttributes attributes){
           logger.info("Upload Order Request Received: "+placeOrder);
           String uploadResponse = "";
           try{
               uploadResponse = amazonClient.batchUploadFiles(files,dir);

           }catch (Exception e){
               e.printStackTrace();
           }
           if (uploadResponse.equals("Completed")){
               attributes.addFlashAttribute("dir",dir);
               queue.offer(placeOrder);
               logger.info("Upload Order Request Finished: "+placeOrder);
           }
    }

    public  void setRenameFolderOrder(String placeOrder,String oldName,String newName){
        new Thread(()->{
           logger.info("Rename Folder Order Request Received: "+placeOrder);
           boolean rfoResponse = false;
           try{
               rfoResponse = amazonClient.reNameFolder(oldName,newName);
           }catch (Exception e){
               logger.error("Rename process exception",e);
           }
           if (rfoResponse){
               queue.offer(placeOrder);
               logger.info("Rename Folder Order Request Finished: "+placeOrder);
           }
        }).start();
    }

    public void setRenameFileOrder(String placeOrder,String oldName,String newName){
        new Thread(()->{
            logger.info("Rename File Order Request Recived: "+placeOrder);
            boolean rfiResponse = false;
            try{
                rfiResponse = amazonClient.renameFile(oldName,newName);
            }catch (Exception e){
                logger.error("Rename process exception",e);
            }
            if (rfiResponse){
                queue.offer(placeOrder);
                logger.info("Rename File Order Request Finished "+placeOrder);
            }
        }).start();
    }

    public Queue<String> getQueue() {
        return queue;
    }

    public void setQueue(Queue<String> queue) {
        this.queue = queue;
    }
}
