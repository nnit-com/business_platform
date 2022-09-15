package com.nnit.pb.handler.af;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * af 属性配置
 * 
 * @author lufy
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "af")
public class AFProperties {
	private String afUn;
	private String afUp;
	private String hpsaUn;
	private String hpsaUp;
	public String getAfUn() {
		return afUn;
	}
	public void setAfUn(String afUn) {
		this.afUn = afUn;
	}
	public String getAfUp() {
		return afUp;
	}
	public void setAfUp(String afUp) {
		this.afUp = afUp;
	}
	public String getHpsaUn() {
		return hpsaUn;
	}
	public void setHpsaUn(String hpsaUn) {
		this.hpsaUn = hpsaUn;
	}
	public String getHpsaUp() {
		return hpsaUp;
	}
	public void setHpsaUp(String hpsaUp) {
		this.hpsaUp = hpsaUp;
	}
	
}
