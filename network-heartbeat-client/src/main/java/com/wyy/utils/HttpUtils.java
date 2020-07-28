package com.wyy.utils;

import com.wyy.pojo.HttpReturn;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {

    /**
     * get请求
     * @param uri
     * @return
     */
    public static HttpReturn get(String uri) {

        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpGet httpGet = new HttpGet(uri);

                client = HttpClients.createDefault();

                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
//                System.out.println(result);
                return new HttpReturn(200, result);
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpReturn(500, "");
        }
    }

    /**
     * 获取本地地址
     * @return
     */
    public static String getHostAddress() {
        InetAddress ia=null;
        try {
            ia=ia.getLocalHost();
            String localip=ia.getHostAddress();
            return localip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    /**
     * 获取本机机器名称
     * @return
     */
    public static String getHostName() {
        InetAddress ia=null;
        try {
            ia=ia.getLocalHost();
            String localname=ia.getHostName();
            return localname;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }


    /**
     * 检查网址是否能访问
     * @return
     */
    public static boolean CheckURLConn(String CheckURL) {
        boolean ifComm;
        try {
            URL url=new URL(CheckURL);
            URLConnection conn=url.openConnection();
            String str=conn.getHeaderField(0);
            if (str.indexOf("OK")> 0){
                ifComm= true;
            }else{
                ifComm= false;
            }
        } catch (Exception e) {
            ifComm= false;
        }
        return ifComm;
    }


}
