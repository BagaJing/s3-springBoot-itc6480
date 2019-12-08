package com.awsdemo.demo.controllers;

import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.downloadQueue.downLoadsQueue;
import com.awsdemo.demo.services.customerService;
import com.awsdemo.demo.singleAction.action;
import com.awsdemo.demo.utils.utils;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vo.userBasic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;



@Controller
//@RequestMapping("/client")

@RequestMapping("/depoytest3/client") //test
public class clientController {

    private final static int DEFAULT_ORDER_LENTGH = 8;
    private Logger logger = LoggerFactory.getLogger(getClass());;
    @Autowired
    private downLoadsQueue downQueue;
    @Autowired
    private action action;
    @Autowired
    private customerService customerService;
    @GetMapping("")
    public String test(){
        return "test";
    }
    @GetMapping("/index/{id}")
    public String showIndex(Model model, @PathVariable Long id) throws NotFoundException {
        String placeOrder = "INDEX-" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setIndexShowOrder(placeOrder,model,"",id);
        return "index";
    }

    @GetMapping("/gotosub/{id}")
    public String gotoNext(@RequestParam("subDir") String subDir, Model model,@PathVariable Long id) throws NotFoundException {
        String placeOrder = "INDEX-" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setIndexShowOrder(placeOrder,model,subDir,id);
       return "index";
    }



    @PostMapping("/upload/{id}")
    public String upload(@RequestParam String path, // relative path
                         @RequestParam String folder,
                         @RequestParam("source") MultipartFile[] files,
                         @PathVariable Long id,
                         RedirectAttributes attributes) throws NotFoundException {
        if (path.length()>1) path = path.substring(1);
        String placeOrder = "UPLOAD-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setUploadOrder(placeOrder,files,path,folder,attributes,id);
        action.saveRecord("UPLOAD",id,placeOrder,files.length);
        return "redirect:/depoytest3/client/upload/"+id;
    }


    @GetMapping("/upload/{id}")
    public String upload_redirect(@ModelAttribute("dir") String subDir,Model model,@PathVariable Long id) throws NotFoundException {
        if (subDir.equals("/")) subDir = "";
        logger.info("subDir Test "+subDir);
        String placeOrder = "INDEX-" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setIndexShowOrder(placeOrder,model,subDir,id);
        return "index";
    }


    @PostMapping("/delete/{id}")
    public String delete_file(@RequestParam("path") String path,
                              @RequestParam("root") String root,
                                              RedirectAttributes attributes,
                             @PathVariable Long id) throws NotFoundException {
        if (root.length()>1) root = root.substring(1);
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String folderName = c.getNickname();
        path = folderName+"/"+ path;
        //logger.info("Rppt Value "+root);
        String placeOrder = "DELETE-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setDeleteOrder(placeOrder,path,root,attributes);
        return "redirect:/depoytest3/client/upload/"+id ;
    }
    @PostMapping("/deletefolder/{id}")
    public String rename_folder(@RequestParam String prefix,
                                                @RequestParam String root,
                                                RedirectAttributes attributes,
                                @PathVariable Long id) throws NotFoundException {
        if (root.length()>1) root = root.substring(1);
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String folderName = c.getNickname();
        String placeOrder = "DELETE-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setDelteFolderOrder(placeOrder,prefix,root,attributes,folderName);
        return "redirect:/depoytest3/client/upload/"+id;
    }
    @PostMapping("/renamefile/{id}")
    public  String rename_file(@RequestParam String oldName,
                               @RequestParam String root,
                               @RequestParam String newName,
                               @PathVariable Long id,
                               RedirectAttributes attributes) throws NotFoundException {
        root = (root.equals("/") ? "" : root);
        root = (root.startsWith("/")? root.substring(1):root);
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String folderName = c.getNickname();
        String placeOrder = "RENAME-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setRenameFileOrder(placeOrder,folderName,oldName,newName,root,attributes);
        return "redirect:/depoytest3/client/upload/"+id;
    }

    @PostMapping("/renamefolder/{id}")
    public String rename_folder(@RequestParam String oldName,
                                @RequestParam String root,
                                @RequestParam String newName,
                                RedirectAttributes attributes,
                                @PathVariable Long id) throws NotFoundException {
        root = (root.equals("/") ? "" : root);
        root = (root.startsWith("/")? root.substring(1):root);
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String folderName = c.getNickname();
        String placeOrder = "RENAME-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        action.setRenameFolderOrder(placeOrder,folderName,oldName,newName,root,attributes);
        return "redirect:/depoytest3/client/upload/"+id;
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@RequestParam("name") String name,
                                             @RequestParam("root") String root,
                                           @PathVariable Long id) throws IOException, NotFoundException {
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String folderName = c.getNickname();
        String order = "DOWN-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        return downQueue.setsingleDownloadFileOrder(order,path);
    }

    @GetMapping("/downloadfolder/{id}")
    public ResponseEntity<byte[]> downloadFolders(@RequestParam("name") String name,
                                                    @RequestParam("root") String root,
                                                  @PathVariable Long id) throws IOException, NotFoundException {
        Customer c = customerService.findCustomerById(id);
        if (c == null) throw new NotFoundException("User Not Found");
        String folderName = c.getNickname();
        String order = "DOWN-"+utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        String path = folderName+"/"+(root.equals("")? "":root+"/")+name;
        return downQueue.setsingleDownloadFolderOrder(order,path);
    }



}
