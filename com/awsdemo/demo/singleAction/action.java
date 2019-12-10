package com.awsdemo.demo.singleAction;

import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.domain.Record;
import com.awsdemo.demo.services.amazonClient;
import com.awsdemo.demo.services.customerService;
import com.awsdemo.demo.utils.contentTypeUtils;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Component
public class action {
    @Autowired
    private amazonClient amazonClient;
    @Autowired
    private com.awsdemo.demo.services.recordService recordService;
    @Autowired
    private customerService customerService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void setIndexShowOrder(String placeOrder, Model model, String subDir,Long id) throws NotFoundException {
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String folderName = c.getNickname();
        logger.info("PageShow Order Received : "+placeOrder);
        List<String> list = amazonClient.getKeyFromS3Bucket(subDir,folderName);
        List<List<String>> res = contentTypeUtils.addTypes(list);
        model.addAttribute("id",id);
        model.addAttribute("objects",res);
        model.addAttribute("rootDir",subDir.split("/"));
      //  model.addAttribute("uploadRoot",uploadRoot);
        model.addAttribute("parent",folderName);
        model.addAttribute("records",recordService.listByCustomer(c));;
    }
    public void setGoToOrder(String placeOrder,Model model,String subDir,String folderName){
        logger.info("GoTo Order Received : "+placeOrder);
        List<String> list = amazonClient.getKeyFromS3Bucket(subDir,folderName);
        List<List<String>> res = contentTypeUtils.addTypes(list);
        model.addAttribute("objects",res);
        model.addAttribute("rootDir",subDir);
    }
    //@Async("taskAsyncPool")
    public void setUploadOrder(String placeOrder, MultipartFile[] files, String dir, String folder, RedirectAttributes attributes, Long id) throws NotFoundException {
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String returnDir = dir;
        String folderName = c.getNickname();
        logger.info("Upload Order Request Received: "+placeOrder);
        String uploadResponse = "";
        logger.info("dir "+dir);
        dir = dir + (folder.equals("/")? "":"/"+folder);
        if ((!dir.startsWith("/"))&&(!dir.equals("/"))) dir = "/"+dir;
        if (dir.startsWith("//")) dir = dir.substring(1);
        logger.info("dir "+dir);
        try{
            uploadResponse = amazonClient.batchUploadFiles(files,dir,folderName);
            if (uploadResponse.startsWith("Error")) {
                logger.info("File rejected");
                attributes.addAttribute("rejected", uploadResponse);
            }
            logger.info(uploadResponse);

        }catch (Exception e){
            e.printStackTrace();
        }
        attributes.addAttribute("dir",returnDir);
        logger.info("Upload Order Request Finished: "+placeOrder);
    }
    @Async("taskAsyncPool")
    public void saveRecord(String type, Long id,String orderNum,int num) throws NotFoundException {
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        Record r = new Record();
        r.setType(type);
        r.setCustomer(c);
        r.setOrdernum(orderNum);
        r.setCreatedDate(new Date());
        r.setNumber(num);
        recordService.save(r);
    }

    public void setDeleteOrder(String placeOrder,String path,String root, RedirectAttributes attributes){
        logger.info("Delete Order Request Received: "+placeOrder);
        try {
            amazonClient.deleteFile(path);
            attributes.addAttribute("dir",root);
            logger.info("Delete Order Requeste Finished: "+placeOrder);
        } catch (Exception e){
            logger.error("Delete File Exception",e);
        }
    }
    public void setDelteFolderOrder(String placeOrder,String prefix,String root,RedirectAttributes attributes,String folderName){
        logger.info("Delte Folder Order Received: "+placeOrder);
        try {
            amazonClient.deleteFolder(prefix,folderName);
            attributes.addAttribute("dir",root);
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
        }catch (Exception e){
            logger.error("Rename process exception",e);
        }
        attributes.addAttribute("dir",root);
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
            attributes.addAttribute("dir",root);
        }catch (Exception e){
            logger.error("Rename process exception",e);
        }
        //if (!subHolders.isEmpty()) logger.info(subHolders.toString());
        logger.info("Rename Folder Order Request Finished: "+placeOrder);
    }
}
