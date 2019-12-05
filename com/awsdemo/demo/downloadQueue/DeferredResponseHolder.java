package com.awsdemo.demo.downloadQueue;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeferredResponseHolder {
    private Map<String, DeferredResult<ResponseEntity<byte[]>>> resourceMap = new HashMap<>();
    public Map<String, DeferredResult<ResponseEntity<byte[]>>> getResourceMap() {
        return resourceMap;
    }
    public void setResourceMap(Map<String, DeferredResult<ResponseEntity<byte[]>>> resourceMap) {
        this.resourceMap = resourceMap;
    }
}
