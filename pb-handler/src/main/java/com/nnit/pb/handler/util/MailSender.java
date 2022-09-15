package com.nnit.pb.handler.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailSender {
	
	@Autowired
	GetProperties properties;
	
	private final static Logger logger = LoggerFactory.getLogger(MailSender.class);
	
	/*
	 * toAddress: use ; to split multiple addresses
	 * isHTML: true means HTML format content, false means plain text
	 */
	public boolean sendMail(String toAddress, String mailTitle, String mailContent, boolean isHTML){
		if(toAddress==null||"".equals(toAddress.trim())){
			return false;
		}
		if(toAddress.indexOf("@")<=0){
			return false;
		}
		if(mailContent==null||"".equals(mailContent.trim())){
			return false;
		}
		
		Properties props = new Properties();
	  	// debug
	  	props.setProperty("mail.debug", String.valueOf(properties.isMail_debug()));
	  	// auth
	  	props.setProperty("mail.smtp.auth", String.valueOf(properties.isMail_smtp_auth()));
	  	// set mail host
	  	//props.setProperty("mail.host", "smtp.163.com");
	  	props.setProperty("mail.smtp.host", properties.getMail_smtp_host());
	  	// protocol
	  	props.setProperty("mail.transport.protocol", properties.getMail_transport_protocol());
//	  	props.setProperty("mail.smtp.socketFactory.class",
//	  	    "javax.net.ssl.SSLSocketFactory");
	  	props.setProperty("mail.smtp.port", String.valueOf(properties.getMail_smtp_port()));
	  	props.setProperty("mail.smtp.socketFactory.port", String.valueOf(properties.getMail_smtp_socketFactory_port()));
	  	props.setProperty("mail.smtp.timeout", String.valueOf(properties.getMail_smtp_timeout()));
	 	props.setProperty("mail.smtp.starttls.enable", String.valueOf(properties.isMail_smtp_starttls_enable()));
	  	// set environment
	  	Authenticator authenticator = new Authenticator() {  
            @Override  
            protected PasswordAuthentication getPasswordAuthentication() {
            	return new PasswordAuthentication(properties.getMail_account(), properties.getMail_pass());
            }  
        };  
        Session session = Session.getInstance(props, authenticator); 
  	    // create message
  	    Message msg = new MimeMessage(session);
  	   
  	    try {
  	    	mailTitle = MimeUtility.encodeWord(mailTitle,"UTF-8","Q");
			msg.setSubject(mailTitle);
			// set content or text
			if(isHTML){
				msg.setContent(mailContent,"text/html;charset=UTF-8");
			}else{
				msg.setText(mailContent);
			}
	  	    // set from
	  	    msg.setFrom(new InternetAddress(properties.getMail_account(), properties.getMail_account_alias()));
	  	    //msg.setRecipient(RecipientType.TO, new InternetAddress(emailaddress));
	  	    
	  	    if(toAddress.indexOf(";")>0){
	  	    	List<String> addressList = new ArrayList<>();
	  	    	String[] addressArray = toAddress.split(";");
	  	    	for(String addr : addressArray){
	  	    		if(addr!=null && !"".equals(addr.trim()) && addr.indexOf("@")>0){
	  	    			addressList.add(addr.trim());
	  	    		}
	  	    	}
	  	    	Address[] tos = new InternetAddress[addressList.size()];
	  	    	for(int i=0;i<addressList.size();i++){
	  	    		String address = addressList.get(i);
	  	    		tos[i] = new InternetAddress(address);
	  	    	}
	  	    	msg.setRecipients(RecipientType.TO, tos);
	  	    }else{
	  	    	msg.setRecipient(RecipientType.TO, new InternetAddress(toAddress.trim()));
	  	    }

	  	    Transport.send(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("send mail exception", e);
			return false;
		}
  	    return true;
	}

}
