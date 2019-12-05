package com.awsdemo.demo.queue;

import com.awsdemo.demo.domain.Customer;
import com.awsdemo.demo.domain.Record;
import com.awsdemo.demo.services.customerServiceImpl;
import com.awsdemo.demo.services.recordService;
import com.awsdemo.demo.services.recordServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;

@Component
@Import({actionsQueue.class,DeferredResultHolder.class})
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private actionsQueue theQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String INDEX = "index-dev";
    private static String RE_INDEX = "redirect:/dev/index";
    private static String RE_UPLOAD = "redirect:/dev/upload";
    private static String TEST = "redirect:/dev/test";
    @Override
    @Async("taskAsyncPool")
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        recordServiceImpl recordService = new recordServiceImpl();
        customerServiceImpl customerService = new customerServiceImpl();
        resultMap result = new resultMap();
        /*
        new Thread(()->{
           while (true){
               if (theQueue.getCompleteOrder()!= null){
                   String orderNumber = theQueue.getCompleteOrder();
                   String orderType = "";
                   if (orderNumber.startsWith("UP")) orderType = "Upload Order";
                   if (orderNumber.startsWith("RFO")) orderType = "Rename Folder Order";
                   if (orderNumber.startsWith("RFI")) orderType = "Rename File Order";
                   logger.info("Retrun Order Status: "+orderNumber);
                   resultHolder.getMap().get(orderNumber).setResult("Place "+orderType+" Success. Order Number: "+orderNumber);
                   theQueue.setCompleteOrder(null);
               }else{
                   try {
                       Thread.sleep(100);
                   }catch (InterruptedException e){
                       e.printStackTrace();
                   }
               }
           }
        }).start();
         */

            while (true) {
                if (!theQueue.getQueue().isEmpty()) {
                    String orderNumber = theQueue.getQueue().poll();
                    String type = orderNumber.substring(0,orderNumber.indexOf("-"));
                    String orderType = result.getMap().getOrDefault(type,"index-dev");
                    logger.info("Retrun Order Status: " + orderNumber);
                    logger.info("Place " + orderType + " Success. Order Number: " + orderNumber);
                    resultHolder.getMap().get(orderNumber).setResult(orderType);
                }else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    }
}
