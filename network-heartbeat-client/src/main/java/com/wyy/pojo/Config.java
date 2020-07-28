package com.wyy.pojo;

import java.util.List;

/**
 * @Classname prop
 * @Description TODO
 * @Date 2020/7/24 上午 10:37
 * @Created by wangyongyi
 */
public class Config {
    private int frequency;//发送心跳包的间隔时间
    private String server;//服务器地址
    private int port;//服务器端口

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
