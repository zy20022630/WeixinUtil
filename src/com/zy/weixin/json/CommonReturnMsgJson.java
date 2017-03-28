package com.zy.weixin.json;

/**
 * 调用时的通用反馈信息JSON
 * @author zy20022630
 */
public class CommonReturnMsgJson {

	private int errcode;
	private String errmsg;
	
	public CommonReturnMsgJson() {
		super();
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}