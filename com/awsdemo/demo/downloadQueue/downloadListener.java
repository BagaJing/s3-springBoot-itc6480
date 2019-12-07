package com.awsdemo.demo.downloadQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.AbstractMap;

@Component
@Import({downLoadsQueue.class,DeferredResponseHolder.class})
public class downloadListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private downLoadsQueue queue;
    @Autowired
    private DeferredResponseHolder responseHolder;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    @Async("taskAsyncPool")
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        while (true){
            if (!queue.getQueue().isEmpty()){
                AbstractMap.SimpleEntry<String, ResponseEntity<byte[]>> entry = queue.getQueue().poll();
                String orderId = entry.getKey();
                logger.info("Download Order "+orderId+" finished. Start to deliver");
                responseHolder.getResourceMap().get(orderId).setResult(entry.getValue());
            }else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    logger.error("Interuptted Exception from downloadListener",e);
                }
            }
        }


    }
}
