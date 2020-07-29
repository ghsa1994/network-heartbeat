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
    private final String DEVCONFIGFILE="E:\\config.properties";
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
        config.setFrequency(Integer.parseInt(configProp.get("frequency")));
        config.setServer(configProp.get("server"));
        config.setPort(Integer.parseInt(configProp.get("port")));


        return config;
    }
}
