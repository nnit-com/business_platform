package com.nnit.pb.handler.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetProperties {
	
	@Value("${mail.debug}")
	private boolean mail_debug;
	
	@Value("${mail.smtp.auth}")
	private boolean mail_smtp_auth;
	
	@Value("${mail.smtp.host}")
	private String mail_smtp_host;
	
	@Value("${mail.transport.protocol}")
	private String mail_transport_protocol;
	
	@Value("${mail.smtp.port}")
	private int mail_smtp_port;
	
	@Value("${mail.smtp.socketFactory.port}")
	private int mail_smtp_socketFactory_port;
	
	@Value("${mail.smtp.timeout}")
	private int mail_smtp_timeout;
	
	@Value("${mail.account}")
	private String mail_account;
	
	@Value("${mail.pass}")
	private String mail_pass;
	
	@Value("${mail.alias}")
	private String mail_account_alias;
	
	@Value("${mail.smtp.starttls.enable}")
	private boolean mail_smtp_starttls_enable;

	public boolean isMail_debug() {
		return mail_debug;
	}

	public boolean isMail_smtp_auth() {
		return mail_smtp_auth;
	}

	public String getMail_smtp_host() {
		return mail_smtp_host;
	}

	public String getMail_transport_protocol() {
		return mail_transport_protocol;
	}

	public int getMail_smtp_port() {
		return mail_smtp_port;
	}

	public int getMail_smtp_socketFactory_port() {
		return mail_smtp_socketFactory_port;
	}

	public int getMail_smtp_timeout() {
		return mail_smtp_timeout;
	}

	public String getMail_account() {
		return mail_account;
	}

	public String getMail_pass() {
		return mail_pass;
	}

	public String getMail_account_alias() {
		return mail_account_alias;
	}

	public boolean isMail_smtp_starttls_enable() {
		return mail_smtp_starttls_enable;
	}
	
	
	
}
