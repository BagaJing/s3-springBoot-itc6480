package com.awsdemo.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class zipUtils {
    private static final int BUFFER_SIZE = 2*1024;
    public static void folderToZip(String dir, OutputStream out){
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceDir = new File(dir);
            compress(sourceDir,zos,sourceDir.getName());
        }catch (Exception e){
            throw new RuntimeException("zip process exception from zipUtils",e);
        }finally {
            if (zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void compress(File sourceFile,ZipOutputStream zos,String name) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        if (sourceFile.isDirectory()){ //压缩文件夹的情况
            File[] listFiles = sourceFile.listFiles();
            if (listFiles.length==0||listFiles==null){ //文件夹为空时 直接打包空文件夹
                zos.putNextEntry(new ZipEntry(name));
                zos.closeEntry();
            }else{ //文件夹飞空 考虑子文件夹的情况，用递归处理
                for(File file : listFiles){
                    compress(file,zos,name+"/"+file.getName());
                }
            }
        }else { //递归出口, 把文件copy到zip流中
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buffer))!=-1)
                zos.write(buffer,0,len);
            //complete the entry
            zos.closeEntry();
            in.close();
        }
        //释放内存
        buffer = null;
    }
}
//References: https://www.cnblogs.com/zeng1994/p/7862288.html