package com.wyy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname DateUtils
 * @Description TODO
 * @Date 2020/7/24 下午 4:42
 * @Created by wangyongyi
 */
public class DateUtils {

    /**
     * 用秒获取毫秒
     * @param second
     * @return
     */
    public static int getMsecBySecond(int second){
        return second * 1000;
    }

    /**
     * 获取当前时间String
     * @return
     */
    public static String getNewDateString(){
        return getFormatDateString(new Date(),"yyyy/MM/dd HH:mm:ss");
    }

    /**
     * 格式化时间String
     * @return
     */
    public static String getFormatDateString(Date date,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        return sdf.format(date);

    }


}
