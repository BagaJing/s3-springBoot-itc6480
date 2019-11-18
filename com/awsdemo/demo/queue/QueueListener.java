package com.awsdemo.demo.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private actionsQueue theQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
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
        new Thread(()->{
            while (true) {
                if (!theQueue.getQueue().isEmpty()) {
                    String orderNumber = theQueue.getQueue().poll();
                    String orderType = "";
                    if (orderNumber.startsWith("UP")) orderType = "Upload Order";
                    if (orderNumber.startsWith("RFO")) orderType = "Rename Folder Order";
                    if (orderNumber.startsWith("RFI")) orderType = "Rename File Order";
                    logger.info("Retrun Order Status: " + orderNumber);
                    resultHolder.getMap().get(orderNumber).setResult("Place " + orderType + " Success. Order Number: " + orderNumber);
                }else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
