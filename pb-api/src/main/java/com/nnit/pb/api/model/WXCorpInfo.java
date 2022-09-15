package com.nnit.pb.api.model;

import com.nnit.pb.common.constant.WXConstants;

public class WXCorpInfo {
	private String corpid = WXConstants.WX_CORPID;
	private String corpsecret = WXConstants.WX_CORPSECRET;
	public String getCorpid() {
		return corpid;
	}
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public String getCorpsecret() {
		return corpsecret;
	}
	public void setCorpsecret(String corpsecret) {
		this.corpsecret = corpsecret;
	}
	
	


}
