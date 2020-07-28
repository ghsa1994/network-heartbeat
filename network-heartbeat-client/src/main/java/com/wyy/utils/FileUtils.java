package com.wyy.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * @Classname fileUtils
 * @Description TODO
 * @Date 2020/7/24 上午 9:25
 * @Created by wangyongyi
 */
public class FileUtils {
    /**
     * 获取文档编码
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String codeString(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        bin.close();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }


    /**
     * 读取配置文件
     * @param proPath
     * @return
     */
    public static HashMap<String, String> readConfigFile(String proPath) {
        HashMap<String, String> par = new HashMap<String, String>();
        Properties prop = new Properties();
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(proPath), FileUtils.codeString(proPath));
            prop.load(in);
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next().trim();
                    par.put(key, prop.getProperty(key));
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return par;
    }

}
