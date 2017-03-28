package com.zy.weixin.json;

/**
 * 获取jsapi_ticket成功时的JSON
 * @author zy20022630
 */
public class GetJSAPITicketJson {
	
	private int errcode;
	private String errmsg;
	private String ticket;
	private int expires_in;//有效期7200秒
	
	public GetJSAPITicketJson() {
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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

}