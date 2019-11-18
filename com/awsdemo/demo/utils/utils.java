package com.awsdemo.demo.utils;

import java.util.Random;

public class utils {
    public static String getRandomOrderNum(int num){
        String result = "";
        Random ran = new Random();
        for (int i = 0 ; i < num ; i++){
            result += String.valueOf(ran.nextInt(10));
        }
        return result;
    }
    public static void main(String[] args){
        System.out.println(getRandomOrderNum(8));
    }
}
