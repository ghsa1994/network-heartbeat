package com.wyy.server;

import com.wyy.pojo.Config;
import com.wyy.utils.FileUtils;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @Classname ReadPropServer
 * @Description TODO
 * @Date 2020/7/24 上午 10:35
 * @Created by wangyongyi
 */
public class ReadPropServer {
    // 调试的目录
    private final String DEVCONFIGFILE = "E:\\config.properties";
    private final String PRODCONFIGFILE = "./config.properties";

    public Config getProp (String configType){
        Config config = new Config();
        String configFile;
        if (configType.equals("prod")) {
            configFile = PRODCONFIGFILE;
        } else {
            configFile = DEVCONFIGFILE;
        }
        HashMap<String, String> configProp = FileUtils.readConfigFile(configFile);

        // 端口port
        config.setPort(Integer.parseInt(configProp.get("port")));

        // 配置参数
        // 联网状态
        config.setReconnectionTime(Integer.parseInt(configProp.get("reconnectionTime"))); // 等待重连的时间

        // mailgun邮件发送参数
        config.setSendAddrss(configProp.get("sendAddrss"));
        String receiver = configProp.get("receiver"); // 接受邮箱
        config.setReceiverList(Arrays.asList(receiver.split(",")));
        config.setMailgunPassword(configProp.get("mailgunPassword")); // mailgun密码

        //Server酱通知
        String SCKey = configProp.get("SCKey"); // 结束key
        config.setSCKeyList(Arrays.asList(SCKey.split(",")));

        // 邮件发送内容
        config.setOnLineTitle(configProp.get("onLineTitle")); // 连接正常邮件标题
        config.setOffLineTitle(configProp.get("offLineTitle")); // 连接失败邮件标题
        config.setOnLinesendText(configProp.get("onLinesendText")); // 连接正常邮件内容
        config.setOffLinesendText(configProp.get("offLinesendText")); // 连接失败邮件内容

        return config;
    }
}
