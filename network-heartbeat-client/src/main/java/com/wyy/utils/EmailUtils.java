package com.wyy.utils;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


public class EmailUtils {
	/**
	 * 使用Mailgun发送免费邮件
	 * @param sendAddrss
	 * @param receiver
	 * @param mailgunPassword
	 * @param title
	 * @param sendText
	 * @return
	 */
	public boolean SendEmailByMailgun(String sendAddrss, String receiver, String mailgunPassword, String title, String sendText) {
		boolean flag;
		try {
			Properties props = System.getProperties();
			props.put("mail.smtps.host", "smtp.mailgun.org");
			props.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(props, null);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sendAddrss));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
			msg.setSubject(title);
			msg.setText(sendText);
			msg.setSentDate(new Date());
			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.mailgun.com", sendAddrss, mailgunPassword);
			t.sendMessage(msg, msg.getAllRecipients());
//			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println(e.toString());
			flag = false;
		}
		return flag;
	}
	
	public static void main(String[] args) {
		EmailUtils sen = new EmailUtils();
		sen.SendEmailByMailgun("postmaster@wyysa.com","1127123522@qq.com","2c7f5f61078997fe46ba872ced37d81a","Test_title","Test_sendText");
	}
}
