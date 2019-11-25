package com.awsdemo.demo.downloadQueue;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeferredResponseHolder {
    private Map<String, DeferredResult<ResponseEntity<Resource>>> resourceMap = new HashMap<>();
    public Map<String, DeferredResult<ResponseEntity<Resource>>> getResourceMap() {
        return resourceMap;
    }
    public void setResourceMap(Map<String, DeferredResult<ResponseEntity<Resource>>> resourceMap) {
        this.resourceMap = resourceMap;
    }
}
