package com.awsdemo.demo.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class contentTypeUtils {
    private static final String[][] MIME_strTable ={
            //Video
            {".3gp", "video/3gpp"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            //mp4
            {".mp4", "video/mp4"},
            {".mpg4", "video/mp4"},

            {".mpe", "video/x-mpeg"},
            //mpeg
            {"", "video/mpg"},
            {".mpeg", "video/mpg"},
            {".mpg", "video/mpg"},

            //audio
            {".m3u", "audio/x-mpegurl"},
            //mp4a-latm
            {"", "audio/mp4a-latm"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            //x-mpeg
            {".mp2", "x-mpeg"},
            {".mp3", "audio/x-mpeg"},

            {".mpga", "audio/mpeg"},
            {".ogg", "audio/ogg"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},

            //text
            //plain
            {"", "text/plain"},
            {".c", "text/plain"},
            {".java", "text/plain"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".h", "text/plain"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".sh", "text/plain"},
            {".log", "text/plain"},
            {".txt", "text/plain"},
            {".xml", "text/plain"},
            //html
            {".html", "text/html"},
            {".htm", "text/html"},
            {".css", "text/css"},

            //image
            {".jpg", "image/jpeg"},
            {".jpeg", "image/jpeg"},
            {".bmp", "image/bmp"},
            {".gif", "image/gif"},
            {".png", "image/png"},

            //application
            {".bin", "application/octet-stream"},
            {".class", "application/octet-stream"},
            {".exe", "application/octet-stream"},
            {"class", "application/octet-stream"},

            {".apk", "application/vnd.android.package-archive"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},

            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".jar", "application/java-archive"},
            {".js", "application/x-javascript"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".msg", "application/vnd.ms-outlook"},
            {".pdf", "application/pdf"},
            //vnd.ms-powerpoint
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},

            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".rtf", "application/rtf"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".wps", "application/vnd.ms-works"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}
    };
    private static HashMap<String,String> typesMap = new HashMap<>();
    private static void initializeMap(){
        if (typesMap.size() == 0){
            for(int i = 0; i < MIME_strTable.length; i++){
                typesMap.put(MIME_strTable[i][0],MIME_strTable[i][1]);
            }
        }
    }
    public static String toCotentType(String suffix){
        initializeMap();
        if (suffix==null || suffix.trim()=="") return "application/octet-stream";
        if (!typesMap.containsKey(suffix)) return "application/octet-stream";
        else return typesMap.get(suffix);
    }

    public static List<List<String>> addTypes(List<String> target){
        initializeMap();
        List<List<String>> res = new LinkedList<>();
        List<String> unit = new LinkedList<>();
        String suffix = "";
        for (String name : target){
            String path = name.substring(name.indexOf("/")+1);
            if (!path.equals("")) unit.add(path);
            else continue;
            unit.add(name.substring(name.lastIndexOf("/")+1));
            if(name.contains(".")) suffix = name.substring(name.lastIndexOf("."));
            if (suffix.equals("")) unit.add("");
            else unit.add(typesMap.getOrDefault(suffix,""));
            res.add(new LinkedList<>(unit));
            unit.clear();
            suffix = "";
        }
        return res;
    }
    /*
    public static void main(String[] args){
        String test = ".jpg";
        long currentTime = System.currentTimeMillis();
        System.out.println(toCotentType(test));
        long affterTime = System.currentTimeMillis();
        System.out.println(affterTime-currentTime);
    }

     */
}
