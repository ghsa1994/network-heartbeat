package com.wyy.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.CharBuffer;

import com.wyy.pojo.Config;
import com.wyy.pojo.MsgKey;
import com.wyy.utils.DateUtils;
import com.wyy.utils.HttpUtils;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class SocketServer {

	private static ReadPropServer readPropServer;
	private static SendMsgServer sendMsgServer;
	private static Config config;


	public static void setConfig(Config config) {
		SocketServer.config = config;
	}

	public SocketServer(String runType) {
		readPropServer = new ReadPropServer();
		sendMsgServer = new SendMsgServer();
		this.setConfig(readPropServer.getProp(runType));
	}

	// 心跳包定义
	static String reply = "Message from the network-heartbeat-server";
	private final static String SOAP_BEGIN = "<SOAP-BEGIN>";
	private final static String SOAP_END = "</SOAP-END>";

	/**
	 * online定义
	 *  0：已断开
	 *  1：已连接 
	 *  3：已断开且发送邮件
	 *  4：服务端初始化完成。等待首次连接
	 */
	volatile int online = 4;
	volatile String soapTime = DateUtils.getNewDateString();//心跳时间，每次心跳包都记录时间
	volatile String clientAddress = "127.0.0.1";//心跳时间，每次心跳包都记录时间
	volatile Thread socketThreadThread;
	volatile String breatheCode = "*";


	public void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(config.getPort());//服务器已启动
		System.out.println("服务端已启动！");
		System.out.println("服务IP地址视当前运行环境和需求自行获取！");
		System.out.println("服务端口：" + config.getPort());
		while (true) {
			Socket socket = serverSocket.accept();//接收客户端传过来的数据，会阻塞
			clientAddress = socket.getInetAddress().getHostAddress();
			String clientAddress = socket.getInetAddress().getHostAddress();
			if (socketThreadThread != null) {
				socketThreadThread.interrupt();
			}
			socketThreadThread = new Thread(new SocketThread(socket));
			socketThreadThread.start();//接受消息线程
		}
	}

	//接受消息
	class SocketThread implements Runnable {
		private Socket socket;
		private String temp = "";
		public SocketThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				socket.setSoTimeout(DateUtils.getMsecBySecond(config.getReconnectionTime()));
				Reader reader = new InputStreamReader(socket.getInputStream());
				Writer writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				CharBuffer charBuffer = CharBuffer.allocate(1024);
				while (reader.read(charBuffer) != -1) {
					charBuffer.flip();
					temp += charBuffer.toString();
					if (temp.indexOf(SOAP_BEGIN) != -1 && temp.indexOf(SOAP_END) != -1) {
						soapTime = DateUtils.getNewDateString();
						if (online == 3) {
							System.out.println("客户端已恢复连接！正在发送通知邮件。");
							SendMsg(1);
							System.out.println("-----------------------------------\n收到客户端心跳包时间：");
						}
						if (online == 4) {
							System.out.println("客户端已连接！正在发送测试邮件，请注意查收。客户端地址：" + clientAddress);
							SendMsg(4);
							System.out.println("-----------------------------------\n收到客户端心跳包时间：");
						}
						online = 1;
						//呼吸灯效果
						breatheCode = breatheCode.equals("*") ? "+" : "*";
//						System.out.print("\r" + temp.substring(12,31) + " " + breatheCode);//这个docker logs显示不了
						System.out.println(temp.substring(12,31) + "   " + breatheCode);
						temp = "";
						//返回客户端信息
						writer.write(reply + "," + soapTime);
						writer.flush();
					}
					//连续收到错误的包，一次包大小为62，错误十次就返回异常
					if (temp.length() > 620) {
						writer.write("非法连接！即将断开连接！");
						writer.flush();
						break;
					}
				}
				//正常断开
				if (online == 1) {//在线的状态下才开始断线计时
					System.out.println("客户端已断开连接，正在等待重连！");
					online = 0;
					try {
						sleep(DateUtils.getMsecBySecond(config.getReconnectionTime()));
						System.out.println("已断开连接超过" + config.getReconnectionTime() + "秒钟，正在发送通知邮件！");
						SendMsg(0);//发送消息
						online = 3;
					} catch (InterruptedException interruptedException) {//这个线程被标记关闭了，说明有新的线程，说明已重连
						System.out.println("客户端已恢复连接！取消发送邮件");
						System.out.println("-----------------------------------\n收到客户端心跳包时间：");
					}
				}
			} catch (SocketTimeoutException e) {
				//超时异常
				if (currentThread().isInterrupted()) {//这种情况是 超时时间内 已经重连的
//					System.out.println("线程" + currentThread().getId() + "已丢弃");
				} else {//这种情况是在等改时间内没有重连，如果是在线状态就开始计时，发邮件
					if (online == 1) {//在线的状态下才开始断线计时
						System.out.println("超过"+config.getReconnectionTime()+"秒没有收到心跳包，疑似客户端网络断开！正在发送通知邮件");
						online = 0;
						SendMsg(0);//发送消息
						online = 3;

					}
				}

			} catch (Exception e) {
				if (online == 1) {//在线的状态下才开始断线计时
					System.out.println("客户端已断开连接，正在等待重连！");
					online = 0;
					try {
						sleep(DateUtils.getMsecBySecond(config.getReconnectionTime()));
						System.out.println("已断开连接超过" + config.getReconnectionTime() + "秒钟，正在发送通知邮件！");
						SendMsg(0);//发送消息
						online = 3;

					} catch (InterruptedException interruptedException) {//这个线程被标记关闭了，说明有新的线程，说明已重连
						System.out.println("客户端已恢复连接！取消发送邮件");
						System.out.println("-----------------------------------\n收到客户端心跳包时间：");
					}
				}
			} finally {
				if (socket != null) {
					if (!socket.isClosed()) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
	}


	/**
	 * 发送通知信息
	 * @param msgType 0:断线，1：断线后已连接，4：初始化连接
	 */
	public void SendMsg(int msgType){
		MsgKey msgKey = new MsgKey();
		msgKey.setTime(soapTime);
		msgKey.setClientAddress(clientAddress);
		String msgTitle = "";
		String msgText = "";


		switch (msgType){
			case 0:
				msgTitle = config.getOffLineTitle(msgKey);
				msgText = config.getOffLinesendText(msgKey);
				break;
			case 1:
				msgTitle = config.getOnLineTitle(msgKey);
				msgText = config.getOnLinesendText(msgKey);
				break;
			case 4:
				msgTitle = "客户端已连接！";
				msgText = "客户端已连接！客户端断开连接的时候将会通知您。\n连接时间："+soapTime + "\n客户端地址：" + clientAddress;

		}

		if (!msgTitle.isEmpty() && !msgText.isEmpty()) {
			//发送邮件
			sendMsgServer.SendEmailByMailgun(
					msgTitle,
					msgText,
					config.getSendAddrss(),
					config.getReceiverList(),
					config.getMailgunPassword());

			//发送Server酱消息
			sendMsgServer.SendMsgByServerChan(msgTitle, msgText, config.getSCKeyList());
		}
	}

}