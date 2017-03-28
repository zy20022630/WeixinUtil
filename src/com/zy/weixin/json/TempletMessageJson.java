package com.zy.weixin.json;

public class TempletMessageJson {

	private int errcode;//返回码
	private String errmsg;//返回信息
	private long msgid;//方法ID
	
	public TempletMessageJson() {
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

	public long getMsgid() {
		return msgid;
	}

	public void setMsgid(long msgid) {
		this.msgid = msgid;
	}
	
}