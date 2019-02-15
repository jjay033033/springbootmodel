package top.lmoon.util;

import java.io.Serializable;

public class HttpResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String setCookie;
	
	private String content;
	
	private boolean success;

	public String getSetCookie() {
		return setCookie;
	}

	public void setSetCookie(String setCookie) {
		this.setCookie = setCookie;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
