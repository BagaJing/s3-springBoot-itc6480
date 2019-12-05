package com.awsdemo.demo.controllers;

import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.downloadQueue.DeferredResponseHolder;
import com.awsdemo.demo.downloadQueue.downLoadsQueue;
import com.awsdemo.demo.queue.DeferredResultHolder;
import com.awsdemo.demo.utils.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
@RequestMapping("/dev")
@Controller
public class devController {
    private final static int DEFAULT_ORDER_LENTGH = 8;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private com.awsdemo.demo.queue.actionsQueue actionsQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    @Autowired
    private downLoadsQueue downQueue;
    @Autowired
    private DeferredResponseHolder responseHolder;
    @GetMapping("/index")
    /*
    public String showIndex(Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,"",folderName);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        // return "index-dev";
        return "index-dev";
    }

     */

    public DeferredResult<String> frontTest(Model model,HttpSession session){
        String placeOrder = "INDEX-" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        Customer c = (Customer)session.getAttribute("customer");
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        actionsQueue.setIndexShowOrder(placeOrder,model,"",folderName,c);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }

    @GetMapping("/gotosub")
    /*
    public String gotoNext(@RequestParam("subDir") String subDir, Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,subDir,folderName);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        //return "index-dev";
        return "index-dev";
    }

     */

    public DeferredResult<String> gotoNext(@RequestParam("subDir") String subDir, Model model,HttpSession session){
        Customer c = (Customer)session.getAttribute("customer");
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        String placeOrder = "INDEX-" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,subDir,folderName,c);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }



    @PostMapping("/upload")
    /*
    public String upload(@RequestParam("folder") String folder,// upload as a folder or not
                         @RequestParam("path") String path, // relative path
                         @RequestParam("files") MultipartFile[] files,
                         RedirectAttributes attributes){
        String placeOrder = "UPLOAD" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setUploadOrder(placeOrder,files,path,folder,attributes);
        return "redirect:/client/upload";
    }

     */

public DeferredResult<String> upload(@RequestParam("files")MultipartFile[] files,
                                     @RequestParam("path") String path, // relative path
                                     @RequestParam("folder") String folder, // upload as a folder or not
                                     HttpSession session,
                                     RedirectAttributes attributes){
    Customer c = (Customer)session.getAttribute("customer");
    String folderName = c.getNickname();
    String placeOrder = "UPLOAD-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
    logger.info("Test path value "+path);
    logger.info("Test folder value "+folder);
    actionsQueue.setUploadOrder(placeOrder,files,path,folder,attributes,folderName);
    actionsQueue.saveRecord("UPLOAD",c,placeOrder,files.length);
    DeferredResult<String> result = new DeferredResult<>();
    resultHolder.getMap().put(placeOrder,result);
    return result;
}
    @GetMapping("/upload")
    /*
    public String upload_redirect(@ModelAttribute("dir") String subDir, Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,subDir,folderName);
        return "index-dev";
    }

     */

    public DeferredResult<String> upload_redirect(@ModelAttribute("dir") String subDir,Model model,HttpSession session){
        Customer c = (Customer)session.getAttribute("customer");
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        String placeOrder = "INDEX-" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,subDir,folderName,c);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }

    @PostMapping("/delete")
    public DeferredResult<String> delete_file(@RequestParam("path") String path,
                                              @RequestParam("root") String root,
                                              RedirectAttributes attributes,
                                              HttpSession session){
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        path = folderName+"/"+ path;
        //logger.info("Rppt Value "+root);
        String placeOrder = "DELETE-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setDeleteOrder(placeOrder,path,root,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @PostMapping("/deletefolder")
    public DeferredResult<String> rename_folder(@RequestParam("prefix") String prefix,
                                                @RequestParam("root") String root,
                                                RedirectAttributes attributes,
                                                HttpSession session){
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        String placeOrder = "DELETE-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setDelteFolderOrder(placeOrder,prefix,root,attributes,folderName);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return  result;
    }
    @PostMapping("/renamefile")
    public  DeferredResult<String> rename_file(@RequestParam("oldName") String oldName,
                                               @RequestParam("root") String root,
                                               @RequestParam("newName") String newName,
                                               RedirectAttributes attributes,
                                               HttpSession session){
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        String placeOrder = "RENAME-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setRenameFileOrder(placeOrder,folderName,oldName,newName,root,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }

    @PostMapping("/renamefolder")
    public DeferredResult<String> rename_folder(@RequestParam("oldName") String oldName,
                                                @RequestParam("root") String root,
                                                @RequestParam("newName") String newName,
                                                RedirectAttributes attributes,
                                                HttpSession session){
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        String placeOrder = "RENAME-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setRenameFolderOrder(placeOrder,folderName,oldName,newName,root,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @GetMapping("/download")
    /*
    public ResponseEntity<byte[]> download(@RequestParam("name") String name,
                                           @RequestParam("root") String root) throws IOException {
        String order = "DOWN"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        return downQueue.setDownloadFileOrder(order,path);
    }

     */

    public DeferredResult<ResponseEntity<byte[]>> download(@RequestParam("name") String name,
                                             @RequestParam("root") String root,
                                                           HttpSession session) throws IOException {
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        String order = "DOWN-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        DeferredResult<ResponseEntity<byte[]>> result = new DeferredResult<>();
        responseHolder.getResourceMap().put(order,result);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        downQueue.setDownloadFileOrder(order,path);
        return result;
    }


    @GetMapping("/downloadfolder")
    /*
    public ResponseEntity<byte[]> downloadFolders(@RequestParam("name") String name,
                                                  @RequestParam("root") String root) throws IOException{
        String order = "DOWN"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        return downQueue.setDownloadFolderOrder(order,path);
    }

     */

    public DeferredResult<ResponseEntity<byte[]>> downloadFolders(@RequestParam("name") String name,
                                @RequestParam("root") String root,HttpSession session) throws IOException {
        String folderName = ((Customer)session.getAttribute("customer")).getNickname();
        String order = "DOWN-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        DeferredResult<ResponseEntity<byte[]>> result = new DeferredResult<>();
        responseHolder.getResourceMap().put(order,result);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        downQueue.setDownloadFolderOrder(order,path);
        return result;
    }


}
