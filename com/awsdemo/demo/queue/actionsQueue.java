package com.awsdemo.demo.queue;

import com.awsdemo.demo.services.amazonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class actionsQueue {
    @Autowired
    private amazonClient amazonClient;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private String placeOrder;
    private String completeOrder;
    private Queue<String> queue = new LinkedList<>();

    public void setUploadOrder(String placeOrder, MultipartFile[] files,String dir){
        new Thread(()->{
           logger.info("Upload Order Request Received: "+placeOrder);
           String uploadResponse = "";
           try{
               uploadResponse = amazonClient.batchUploadFiles(files,dir);
           }catch (Exception e){
               e.printStackTrace();
           }
           if (uploadResponse.equals("Completed")){
               queue.offer(placeOrder);
               logger.info("Upload Order Request Finished: "+placeOrder);
           }
        }).start();
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

    public String getCompleteOrder(){
        return completeOrder;
    }
    public void  setCompleteOrder(String completeOrder){
        this.completeOrder = completeOrder;
    }

    public Queue<String> getQueue() {
        return queue;
    }

    public void setQueue(Queue<String> queue) {
        this.queue = queue;
    }
}
