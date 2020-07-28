package com.wyy;

import com.wyy.server.SocketServer;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        String runType = args.length == 0 ? "prod" : args[0];
        if (!runType.equals("dev") && !runType.equals("prod")) {
            System.out.println("ERROR:RunType must be dev or prod and you don't have to enter parameters,The software will use the default parameter:prod");
            System.out.println("错误：运行类型你必须输入dev或者prod，或者你可以不输入，程序将使用默认参数：prod");
            return;
        }
        System.out.println("runType:" + runType);
        SocketServer socketServer = new SocketServer(runType);
        socketServer.start();
    }
}
