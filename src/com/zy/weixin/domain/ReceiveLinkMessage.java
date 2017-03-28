package com.zy.weixin.domain;

/**
 * 接收的链接消息
 * @author zy20022630
 */
public class ReceiveLinkMessage {

	private String toUserName;	//开发者微信号
	private String fromUserName;//发送方帐号（一个OpenID）
	private String createTime;	//消息创建时间 （整型）
	private String msgType;		//link
	
	private String title;		//消息标题
	private String description;	//消息描述
	private String url;			//消息链接
	private String msgId;		//消息id，64位整型
	
	public ReceiveLinkMessage() {
		super();
	}
	
	public String toString(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("ToUserName=[").append(toUserName).append("],")
				 .append("FromUserName=[").append(fromUserName).append("],")
				 .append("CreateTime=[").append(createTime).append("],")
				 .append("MsgType=[").append(msgType).append("]");
		if (title != null)
			tmpBuffer.append(",Title=[").append(title).append("]");
		if (description != null)
			tmpBuffer.append(",Description=[").append(description).append("]");
		if (url != null)
			tmpBuffer.append(",Url=[").append(url).append("]");
		if (msgId != null)
			tmpBuffer.append(",MsgId=[").append(msgId).append("]");
		return tmpBuffer.toString();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}