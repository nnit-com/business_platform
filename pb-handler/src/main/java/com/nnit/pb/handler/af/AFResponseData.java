package com.nnit.pb.handler.af;

public class AFResponseData {
	private boolean success;
	private String result;
	private String error;
	private String afResult;
	private String afStatus;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getAfResult() {
		return afResult;
	}
	public void setAfResult(String afResult) {
		this.afResult = afResult;
	}
	public String getAfStatus() {
		return afStatus;
	}
	public void setAfStatus(String afStatus) {
		this.afStatus = afStatus;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
