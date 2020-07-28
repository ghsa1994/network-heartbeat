package com.wyy.pojo;

import java.util.List;

/**
 * @Classname prop
 * @Description TODO
 * @Date 2020/7/24 上午 10:37
 * @Created by wangyongyi
 */
public class Config {
    private int port;//端口
    private int reconnectionTime; //等待重连时间 秒
    private String sendAddrss; //mailgun发送邮箱
    private List<String> receiverList; //接受信息邮箱
    private String mailgunPassword; //mailgun密码
    private List<String> SCKeyList;//Server酱通知
    private String onLineTitle;  // 连接正常邮件标题
    private String offLineTitle; // 连接失败邮件标题
    private String onLinesendText;  // 连接正常邮件内容
    private String offLinesendText;  // 连接失败邮件内容

    public Config() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReconnectionTime() {
        return reconnectionTime;
    }

    public void setReconnectionTime(int reconnectionTime) {
        this.reconnectionTime = reconnectionTime;
    }

    public String getSendAddrss() {
        return sendAddrss;
    }

    public void setSendAddrss(String sendAddrss) {
        this.sendAddrss = sendAddrss;
    }

    public List<String> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<String> receiverList) {
        this.receiverList = receiverList;
    }

    public String getMailgunPassword() {
        return mailgunPassword;
    }

    public void setMailgunPassword(String mailgunPassword) {
        this.mailgunPassword = mailgunPassword;
    }

    public List<String> getSCKeyList() {
        return SCKeyList;
    }

    public void setSCKeyList(List<String> SCKeyList) {
        this.SCKeyList = SCKeyList;
    }

    public String getOnLineTitle(MsgKey msgKey) {
        return onLineTitle
                .replace("#{time}", msgKey.getTime())
                .replace("#{clientAddress}",msgKey.getClientAddress());
    }

    public void setOnLineTitle(String onLineTitle) {
        this.onLineTitle = onLineTitle;
    }

    public String getOffLineTitle(MsgKey msgKey) {
        return offLineTitle
                .replace("#{time}", msgKey.getTime())
                .replace("#{clientAddress}",msgKey.getClientAddress());
    }

    public void setOffLineTitle(String offLineTitle) {
        this.offLineTitle = offLineTitle;
    }

    public String getOnLinesendText(MsgKey msgKey) {
        return onLinesendText
                .replace("#{time}", msgKey.getTime())
                .replace("#{clientAddress}",msgKey.getClientAddress());
    }

    public void setOnLinesendText(String onLinesendText) {
        this.onLinesendText = onLinesendText;
    }

    public String getOffLinesendText(MsgKey msgKey) {
        return offLinesendText
                .replace("#{time}", msgKey.getTime())
                .replace("#{clientAddress}",msgKey.getClientAddress());
    }

    public void setOffLinesendText(String offLinesendText) {
        this.offLinesendText = offLinesendText;
    }
}
