package com.awsdemo.demo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class imageCheck {
    public static void isImageValid(List<File> files, List<String> errorList) throws IOException {
        for (File file : files) {
            BufferedImage sourceImage = ImageIO.read(file);
            if (!file.getName().substring(file.getName().indexOf(".")+1).equals("jpg")){
                errorList.add(file.getName()+" is not jpg file\n");
                continue;
            }
            if ((file.length()/1024.0) > 4000){
                errorList.add(file.getName()+" is larger than 4MB\n");
                continue;
            }
            if (sourceImage.getWidth()>4200||sourceImage.getWidth()<600){
                errorList.add(file.getName()+"'s width is not in allowed scope\n");
                continue;
            }
            if (sourceImage.getHeight()>4200||sourceImage.getHeight()<100){
                errorList.add(file.getName()+"'s height is not in allowed scope\n");
                continue;
            }
            if (!isWhiteBlack(sourceImage.getWidth(),sourceImage.getHeight(),sourceImage)){
                errorList.add(file.getName()+" is colorful picuture\n");
            }
        }
    }
    public static boolean isWhiteBlack(int w,int h,BufferedImage image){
        for (int i = 0 ; i < w ; i+=2){
            for (int j = 0; j < h ; j+=2){
                int[] rgb = new int[3];
                int pixel = image.getRGB(i,j);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                if (GetDiff(rgb)>50) return false;
            }
        }
        return true;
    }
    private static int GetDiff(int[] rgb){
        return Math.max(Math.abs(rgb[0]-rgb[2]),Math.max(Math.abs(rgb[0]-rgb[1]),Math.abs(rgb[1]-rgb[2])));
    }

}
