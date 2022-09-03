package com.mybook.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    /**
     * 获取上传图片名
     * @return
     */
    public static String getImageName(){
        SimpleDateFormat  sdf= new SimpleDateFormat("yyyyMMddHHmmssS");
        return sdf.format(new Date());
    }
    public static java.sql.Date getNewDate(java.sql.Date date,long amount){
        long nowDay= date.getTime();
        nowDay += amount*24*60*60*1000;
        return new java.sql.Date(nowDay);
    }

    public static int getSapn(java.sql.Date date1,Date date2){
        long span = date1.getTime()-date2.getTime();
        int day = (int)span/1000/60/60/24;
        return Math.abs(day);
    }
    public static void main(String[] args) {
        System.out.println(getImageName());
    }

}
