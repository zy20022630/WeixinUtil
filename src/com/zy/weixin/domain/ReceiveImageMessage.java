package com.zy.weixin.domain;

/**
 * 接收的图片消息
 * @author zy20022630
 */
public class ReceiveImageMessage {

	private String toUserName;	//开发者微信号  
	private String fromUserName;//发送方帐号（一个OpenID）  
	private String createTime;	//消息创建时间 （整型）  
	private String msgType;		//image
	
	private String picUrl;		//图片链接  
	private String mediaId;		//图片消息媒体id，可以调用多媒体文件下载接口拉取数据。  
	private String msgId;		//消息id，64位整型 
	
	public ReceiveImageMessage() {
		super();
	}
	
	public String toString(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("ToUserName=[").append(toUserName).append("],")
				 .append("FromUserName=[").append(fromUserName).append("],")
				 .append("CreateTime=[").append(createTime).append("],")
				 .append("MsgType=[").append(msgType).append("]");
		if (picUrl != null)
			tmpBuffer.append(",PicUrl=[").append(picUrl).append("]");
		if (mediaId != null)
			tmpBuffer.append(",MediaId=[").append(mediaId).append("]");
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

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
}