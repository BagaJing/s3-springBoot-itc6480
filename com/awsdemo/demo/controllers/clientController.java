package com.awsdemo.demo.controllers;

import com.awsdemo.demo.queue.DeferredResultHolder;
import com.awsdemo.demo.queue.actionsQueue;
import com.awsdemo.demo.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private actionsQueue actionsQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    @GetMapping("/index")
    public DeferredResult<String> frontTest(Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,"");
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @GetMapping("/gotoSub")
    public DeferredResult<String> gotoNext(@RequestParam("subDir") String subDir, Model model){
        String placeOrder = "INDEX" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setIndexShowOrder(placeOrder,model,subDir);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @PostMapping("/upload")
    public DeferredResult<String> upload(@RequestParam("files")MultipartFile[] files,
                                         @RequestParam("path") String path){
        String placeOrder = "UPLOAD" + utils.getRandomOrderNum(DEFAULT_ORDER_LENTGH);
        actionsQueue.setUploadOrder(placeOrder,files,path);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
}
