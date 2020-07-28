package com.wyy.server;

import com.wyy.pojo.HttpReturn;
import com.wyy.utils.EmailUtils;
import com.wyy.utils.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SendMsgServer {


    /**
     * 发送mailgun邮件
     *
     * @param emailTitle 发送邮件标题
     * @param emailText 发送邮件内容
     * @param receiverList 接受者邮箱集合
     */
    public void SendEmailByMailgun(String emailTitle, String emailText,String sendAddrss, List<String> receiverList,String mailgunPassword) {
        System.out.println("开始发送邮件。");
        System.out.println("- 邮件主题：" + emailTitle);
        System.out.println("- 邮件内容：" + emailText.replace("\n","\\n"));
        System.out.println("- 接受人：" + receiverList.toString());
        EmailUtils emailUtils = new EmailUtils();
        System.out.println("- 正在发送邮件:");
        receiverList.stream().forEach(receiver -> {
            boolean b = emailUtils.SendEmailByMailgun(sendAddrss, receiver, mailgunPassword, emailTitle, emailText);
//			boolean b = true;
            if (b) {
                System.out.println("- " + receiver + "：成功！");
            } else {
                System.out.println("- " + receiver + "：失败！");
            }
        });
        System.out.println("邮件发送完成！");
    }



    public void SendMsgByServerChan(String msgTitle, String msgText,List<String> SCKeyList) {
        String uri = "https://sc.ftqq.com";
        System.out.println("开始发送消息。");
        System.out.println("- 消息标题：" + msgTitle);
        System.out.println("- 消息内容：" + msgText.replace("\n","，"));
        System.out.println("- 接受人SCKey：" + SCKeyList.toString());
        SCKeyList.stream().forEach(SCKey -> {
            HttpReturn httpReturn = null;
            try {
                httpReturn = HttpUtils.get(uri + "/" + SCKey + ".send?text=" + URLEncoder.encode(msgTitle,"utf-8") + "&desp=" + URLEncoder.encode(msgText.replace("\n","，"),"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (httpReturn.getStatus() == 200) {
                System.out.println("- " + SCKey + "：成功！");
            } else {
                System.out.println("- " + SCKey + "：失败！");
            }
        });
        System.out.println("消息发送完成！");

    }
}
