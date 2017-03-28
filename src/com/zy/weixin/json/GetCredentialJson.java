package com.zy.weixin.json;

/**
 * 获取access token成功时的JSON
 * @author zy20022630
 */
public class GetCredentialJson {
	
	private String access_token;
	private int expires_in;
	
	public GetCredentialJson(){
		super();
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
}