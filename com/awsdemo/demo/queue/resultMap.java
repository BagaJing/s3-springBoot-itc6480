package com.awsdemo.demo.queue;

import java.util.HashMap;
import java.util.Map;

/**
 * private static String INDEX = "index-dev";
 * private static String RE_INDEX = "redirect:/dev/index";
 * private static String RE_UPLOAD = "redirect:/dev/upload";
 * private static String TEST = "redirect:/dev/test";
 */
public class resultMap {
    private Map<String,String> map;

    public resultMap() {
        this.map = new HashMap<>();
        this.map.put("INDEX","index-dev");
        this.map.put("UPLOAD","redirect:/dev/upload");
        this.map.put("DELETE","redirect:/dev/upload");
        this.map.put("RENAME","redirect:/dev/upload");

    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
