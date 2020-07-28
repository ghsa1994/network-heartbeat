package com.wyy.server;

import java.io.*;
import java.net.*;
import java.nio.CharBuffer;
import java.util.concurrent.TimeoutException;

import com.wyy.pojo.Config;
import com.wyy.utils.DateUtils;
import com.wyy.utils.HttpUtils;

import static java.lang.Thread.sleep;

public class SocketClientServer{
	private static ReadPropServer readPropServer;
	private static Config config;


	public static void setConfig(Config config) {
		SocketClientServer.config = config;
	}

	private final static String TESTURL="http://www.baidu.com";//用于检查本地网络使用

	//开始和结束标识符
	private final static String SOAP_BEGIN = "<SOAP-BEGIN>";
	private final static String SOAP_END = "</SOAP-END>";

	/**
	 * 0：本地网络未连接
	 * 1：本地网络已连接，连接接服务器异常
	 * 2：本地网络已连接，连接接服务器正常
	 */
	volatile int netType = 0;
	volatile Socket socket = null;
	volatile String breatheCode = "*";


	public SocketClientServer(String runType){
		readPropServer = new ReadPropServer();
		this.setConfig(readPropServer.getProp(runType));
	}

	public void start() {
		System.out.println("客户端已启动！");
		while (true) {
			System.out.println("网络状态已改变，测试网络并尝试连接服务器！");
			Thread checkNetThread = new Thread(new CheckNet());
			checkNetThread.start();//检查本地网络
			while (netType == 0) {
				//如果本地网络异常，主线程就卡在这里，按照发送心跳包的间隔进行睡眠
				try {
					sleep(DateUtils.getMsecBySecond(config.getFrequency()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (netType == 1) {
				//本地网络正常的情况下才开始获取socket
				socket = new Socket();
				try {
					SocketAddress address = new InetSocketAddress(config.getServer(), config.getPort());
					socket.connect(address, DateUtils.getMsecBySecond(config.getFrequency()));
					//没有异常就说明获取到了Socket
					System.out.println("- 连接服务器成功！"+ " " + getbreatheCode());
					netType = 2;
					break;
				} catch (SocketTimeoutException  e ) {
					//服务器连接失败
					netType = 1;
					System.out.println("- 连接服务器失败！"+ " " + getbreatheCode());
				} catch (NoRouteToHostException e) {
					//这个异常说明网络连接出现问题了
					checkNetThread.interrupt();//结束进程之前，先结束当前的检查网络的线程，下一次循环会新启动一个
					netType = 0;//设置状态为0，退出检查服务器状态的循环
				} catch (IOException e) {
					e.printStackTrace();
				}
				/*try {
					sleep(DateUtils.getMsecBySecond(config.getFrequency()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}

			if (netType == 2) {
				//都正常，开始发送数据
				checkNetThread.interrupt();
				System.out.println("服务器连接成功，开始发送心跳包！");
				sendHearbeat();
			}
		}
	}


	/**
	 * 检查本地网络
	 */
	class CheckNet implements Runnable{
		@Override
		public void run() {
			int sleepTime = DateUtils.getMsecBySecond(config.getFrequency());
			int sleepTime2 = sleepTime * 10;
			while (netType != 2 && !Thread.interrupted()) {//不正常的时候才开始测试本地网络
				try {
					boolean b = HttpUtils.CheckURLConn(TESTURL);
					if (b) {
						netType = 1;
						System.out.println("- 测试本地网络正常！"+ " " + getbreatheCode());
						sleep(sleepTime2);//本地网络正常的时候减小频率
					} else {
						netType = 0;
						System.out.println("- 测试本地网络异常！"+ " " + getbreatheCode());
						sleep(sleepTime);
					}

				} catch (Exception e) {
//					e.printStackTrace();
					break;
				}

			}
		}

	}

	/**
	 * 发送心跳包给服务器
	 */
	public synchronized void sendHearbeat (){
		//开始接受回包
		new Thread(new ReceiveThread()).start();
		//开始发送消息
		try {
			while(true) {
				String send = getSend();
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				pw.write(send);
				pw.flush();
			}
		} catch (Exception e) {
			netType = 1;
//			e.printStackTrace();
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
		//正常断开
		netType = 1;
	}
		/**
		 * @return 这里获取消息控制时间
		 * @throws InterruptedException
		 */
		public String getSend() throws InterruptedException{
        	sleep(DateUtils.getMsecBySecond(config.getFrequency()));
            return SOAP_BEGIN+DateUtils.getNewDateString()+SOAP_END;
        }

	/**
	 * 接受回包
	 */
	class ReceiveThread implements Runnable{
        @Override 
        public void run() {
			try {
				System.out.println("-----------------------------------\n收到服务器心跳包时间：");
				while(true) {
					Reader reader = new InputStreamReader(socket.getInputStream());
					CharBuffer charBuffer = CharBuffer.allocate(70);

					while (reader.read(charBuffer) != -1) {
						charBuffer.flip();
						System.out.println(charBuffer.toString().substring(42) + " " + getbreatheCode());
					}
					System.out.println("服务器已断开连接！");
					netType = 1;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//这里发现异常了也关闭socket,让发送的主线程也抛出异常
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


    public String getbreatheCode(){
		//呼吸灯效果
		return breatheCode = breatheCode.equals("*") ? "+" : "*";
	}
} 