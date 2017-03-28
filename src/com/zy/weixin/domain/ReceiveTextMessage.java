package com.zy.weixin.domain;

/**
 * 接收的文本消息
 * @author zy20022630
 */
public class ReceiveTextMessage {

	private String toUserName;	//开发者微信号  
	private String fromUserName;//发送方帐号（一个OpenID）  
	private String createTime;	//消息创建时间 （整型）  
	private String msgType;		//text
	
	private String content;		//文本消息内容  
	private String msgId;		//消息id，64位整型
	
	public ReceiveTextMessage() {
		super();
	}
	
	public String toString(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("ToUserName=[").append(toUserName).append("],")
				 .append("FromUserName=[").append(fromUserName).append("],")
				 .append("CreateTime=[").append(createTime).append("],")
				 .append("MsgType=[").append(msgType).append("]");
		if (content != null)
			tmpBuffer.append(",Content=[").append(content).append("]");
		if (msgId != null)
			tmpBuffer.append(",MsgId=[").append(msgId).append("]");
		return tmpBuffer.toString();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
}