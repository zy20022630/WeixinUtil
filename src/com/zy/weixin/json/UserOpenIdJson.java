package com.zy.weixin.json;

import java.util.List;

/**
 * JSON
 * @author zy20022630
 */
public class UserOpenIdJson {
	
	private List<String> openid;	//OPENID的列表

	public UserOpenIdJson() {
		super();
	}

	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}

}