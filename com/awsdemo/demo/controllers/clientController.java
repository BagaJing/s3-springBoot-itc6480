package com.awsdemo.demo.controllers;

import com.awsdemo.demo.downloadQueue.DeferredResponseHolder;
import com.awsdemo.demo.downloadQueue.downLoadsQueue;
import com.awsdemo.demo.queue.DeferredResultHolder;
import com.awsdemo.demo.queue.actionsQueue;
import com.awsdemo.demo.singleAction.action;
import com.awsdemo.demo.utils.utils;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;



@Controller
@RequestMapping("/client")
//@RequestMapping("/depoytest3/client") //test use
@Import({actionsQueue.class,DeferredResponseHolder.class,downLoadsQueue.class,DeferredResultHolder.class})
public class clientController {
    /*
    private final static int DEFAULT_ORDER_LENTGH = 8;
    private Logger logger = LoggerFactory.getLogger(getClass());
   // @Autowired
    //private com.awsdemo.demo.services.amazonClient amazonClient;
    @Autowired
    private DeferredResultHolder resultHolder;
    @Autowired
    private downLoadsQueue downQueue;
    @Autowired
    private DeferredResponseHolder responseHolder;
    @Autowired
    private action action;
    @Value("${amazonProperties.folderName}")
    private String folderName;
    @GetMapping("")
    public String test(){
        return "test";
    }
    @GetMapping("/index")
    public String showIndex(Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setIndexShowOrder(placeOrder,model,"",folderName);
        return "index";
    }

    @GetMapping("/gotosub")
    public String gotoNext(@RequestParam("subDir") String subDir, Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setIndexShowOrder(placeOrder,model,subDir,folderName);
       return "index";
    }



    @PostMapping("/upload")
    public String upload(@RequestParam("path")String path, // relative path
                         @RequestParam("source")MultipartFile[] files,
                         RedirectAttributes attributes){
        String placeOrder = "UPLOAD" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setUploadOrder(placeOrder,files,path,"",attributes);
        return "redirect:/depoytest3/client/upload";
       // return "index";
    }
    @PostMapping("/uploadfolder")
    public String uploadAsFolder(@RequestParam("folder") String folder,// upload as a folder or not
                         @RequestParam("path") String path, // relative path
                         @RequestParam("source")MultipartFile[] files,
                         RedirectAttributes attributes){
        String placeOrder = "UPLOAD" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setUploadOrder(placeOrder,files,path,folder,attributes);
        return "redirect:/depoytest3/client/upload";
    }

    @GetMapping("/upload")
    public String upload_redirect(@ModelAttribute("dir") String subDir,Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setIndexShowOrder(placeOrder,model,subDir,folderName);
        return "index";
    }


    @PostMapping("/delete")
    public String delete_file(@RequestParam("path") String path,
                              @RequestParam("root") String root,
                                              RedirectAttributes attributes){
        path = folderName+"/"+ path;
        //logger.info("Root Value "+root);
        String placeOrder = "DELETE"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        return action.setDeleteOrder(placeOrder,path,root,attributes);
        //return "redirect:/depoytest3/client/upload";
    }
    @PostMapping("/deletefolder")
    public String rename_folder(@RequestParam("prefix") String prefix,
                                                @RequestParam("root") String root,
                                                RedirectAttributes attributes){
        String placeOrder = "DELETE"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setDelteFolderOrder(placeOrder,prefix,root,attributes);
        return "redirect:/depoytest3/client/upload";
    }
    @PostMapping("/renamefile")
    public  String rename_file(@RequestParam("oldName") String oldName,
                                               @RequestParam("root") String root,
                                               @RequestParam("newName") String newName,
                                               RedirectAttributes attributes){
        String placeOrder = "RENAME"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setRenameFileOrder(placeOrder,folderName,oldName,newName,root,attributes);
        return "redirect:/depoytest3/client/upload";
    }

    @PostMapping("/renamefolder")
    public String rename_folder(@RequestParam("oldName") String oldName,
                                                @RequestParam("root") String root,
                                                @RequestParam("newName") String newName,
                                                RedirectAttributes attributes){
        String placeOrder = "RENAME"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setRenameFolderOrder(placeOrder,folderName,oldName,newName,root,attributes);
        return "redirect:/depoytest3/client/upload";
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("name") String name,
                                             @RequestParam("root") String root) throws IOException{
        String order = "DOWN"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        return downQueue.setsingleDownloadFileOrder(order,path);
    }

    @GetMapping("/downloadfolder")
    public ResponseEntity<byte[]> downloadFolders(@RequestParam("name") String name,
                                                    @RequestParam("root") String root) throws IOException{
        String order = "DOWN"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        return downQueue.setsingleDownloadFolderOrder(order,path);
    }

     */

}
