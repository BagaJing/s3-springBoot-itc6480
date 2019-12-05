package com.awsdemo.demo.singleAction;

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
public class action {
    /*
    @Autowired
    private amazonClient amazonClient;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String placeOrder;
    private Queue<String> queue = new LinkedList<>();

    public void setIndexShowOrder(String placeOrder, Model model, String subDir, String folderName){
        logger.info("PageShow Order Received : "+placeOrder);
        List<String> list = amazonClient.getKeyFromS3Bucket(subDir);
        List<List<String>> res = contentTypeUtils.addTypes(list);
        model.addAttribute("objects",res);
        model.addAttribute("rootDir",subDir.split("/"));
        model.addAttribute("parent",folderName);
    }

    public void setGoToOrder(String placeOrder,Model model,String subDir){
        logger.info("GoTo Order Received : "+placeOrder);
        List<String> list = amazonClient.getKeyFromS3Bucket(subDir);
        List<List<String>> res = contentTypeUtils.addTypes(list);
        model.addAttribute("objects",res);
        model.addAttribute("rootDir",subDir);
    }

    public void setUploadOrder(String placeOrder, MultipartFile[] files, String dir, String folder, RedirectAttributes attributes){
        logger.info("Upload Order Request Received: "+placeOrder);
        String uploadResponse = "";
        String returnDir = dir; // the value to redirect
        dir = dir + (folder.equals("")? "":"/"+folder);
        if (!dir.startsWith("/")) dir = "/"+dir;
        logger.info("dir "+dir);
        try{
            uploadResponse = amazonClient.batchUploadFiles(files,dir);

        }catch (Exception e){
            e.printStackTrace();
        }
        attributes.addFlashAttribute("dir",returnDir);
        logger.info("Upload Order Request Finished: "+placeOrder);
    }


    public String setDeleteOrder(String placeOrder,String path,String root, RedirectAttributes attributes){
        logger.info("Delete Order Request Received: "+placeOrder);
        try {
            amazonClient.deleteFile(path);
            attributes.addFlashAttribute("dir",root);
            logger.info("Delete Order Requeste Finished: "+placeOrder);
        } catch (Exception e){
            logger.error("Delete File Exception",e);
        }
        return "redirect:/depoytest3/client/upload";
    }

    public void setDelteFolderOrder(String placeOrder,String prefix,String root,RedirectAttributes attributes){
        logger.info("Delte Folder Order Received: "+placeOrder);
        try {
            amazonClient.deleteFolder(prefix);
            attributes.addFlashAttribute("dir",root);
            logger.info("Delete ORder Request Finished: "+placeOrder);
        }catch (Exception e){
            logger.error("Delete Folder Exception",e.getMessage());
        }
    }
    public void setRenameFileOrder(String placeOrder,
                                   String parent,
                                   String oldName,
                                   String newName,
                                   String root,
                                   RedirectAttributes attributes){
        logger.info("Rename File Order Request Recived: "+placeOrder);
        oldName = parent+"/"+(root.equals("")? "":root+"/")+oldName;
        newName = parent+"/"+(root.equals("")? "":root+"/")+newName;
        logger.info(oldName);
        logger.info(newName);
        try{
            amazonClient.renameFile(oldName,newName);
            attributes.addFlashAttribute("dir",root);
        }catch (Exception e){
            logger.error("Rename process exception",e);
        }

        queue.offer(placeOrder);
        logger.info("Rename File Order Request Finished "+placeOrder);
    }
    public  void setRenameFolderOrder(String placeOrder,
                                      String parent,
                                      String oldName,
                                      String newName,
                                      String root,
                                      RedirectAttributes attributes){
        logger.info("Rename Folder Order Request Received: "+placeOrder);
        oldName = parent+"/"+(root.equals("")? "":root+"/")+oldName;
        int level = oldName.split("/").length;
        logger.info("dir Level test "+level);
        try{
            amazonClient.renameFolder(oldName,newName,level);
            attributes.addFlashAttribute("dir",root);
        }catch (Exception e){
            logger.error("Rename process exception",e);
        }
    }

     */
}
