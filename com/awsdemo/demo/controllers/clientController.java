package com.awsdemo.demo.controllers;

import com.awsdemo.demo.queue.DeferredResultHolder;
import com.awsdemo.demo.queue.actionsQueue;
import com.awsdemo.demo.utils.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/client")
public class clientController {
    private final static int DEFAULT_ORDER_LENTGH = 8;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private actionsQueue actionsQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    @Value("${amazonProperties.folderName}")
    private String folderName;
    @GetMapping("/index")
    public DeferredResult<String> frontTest(Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,"",folderName);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @GetMapping("/gotoSub")
    public DeferredResult<String> gotoNext(@RequestParam("subDir") String subDir, Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,subDir,folderName);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }

    @PostMapping("/upload")
    public DeferredResult<String> upload(@RequestParam("files")MultipartFile[] files,
                                         @RequestParam("path") String path, // relative path
                                         @RequestParam("folder") String folder, // upload as a folder or not
                                         RedirectAttributes attributes){
        String placeOrder = "UPLOAD" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setUploadOrder(placeOrder,files,path,folder,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @GetMapping("/upload")
    public DeferredResult<String> upload_redirect(@ModelAttribute("dir") String subDir,Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,subDir,folderName);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @PostMapping("/delete")
    public DeferredResult<String> delete_file(@RequestParam("path") String path,
                                              @RequestParam("root") String root,
                                              RedirectAttributes attributes){
        path = folderName+"/"+ path;
        //logger.info("Rppt Value "+root);
        String placeOrder = "DELETE"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setDeleteOrder(placeOrder,path,root,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @PostMapping("/deleteFolder")
    public DeferredResult<String> rename_folder(@RequestParam("prefix") String prefix,
                                                @RequestParam("root") String root,
                                                RedirectAttributes attributes){
        String placeOrder = "DELETE"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setDelteFolderOrder(placeOrder,prefix,root,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return  result;
    }
    @PostMapping("/renameFile")
    public  DeferredResult<String> rename_file(@RequestParam("oldName") String oldName,
                                               @RequestParam("root") String root,
                                               @RequestParam("newName") String newName,
                                               RedirectAttributes attributes){
        String placeOrder = "RENAME"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setRenameFileOrder(placeOrder,folderName,oldName,newName,root,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }

    @PostMapping("/renameFolder")
    public DeferredResult<String> rename_folder(@RequestParam("oldName") String oldName,
                                                @RequestParam("root") String root,
                                                @RequestParam("newName") String newName,
                                                RedirectAttributes attributes){
        String placeOrder = "RENAME"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setRenameFolderOrder(placeOrder,folderName,oldName,newName,root,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
}
