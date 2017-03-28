package com.zy.weixin.domain;

/**
 * 接收的语音消息<br/>
 * <br/>
 * 关闭语音识别功能：<br/>
 * Format  语音格式：speex
 * <br/><br/>
 * 
 * 开启语音识别功能：<br/>
 * Format  语音格式：amr  <br/>
 * Recognition  语音识别结果，UTF8编码
 * <br/><br/>
 * 
 * @author zy20022630
 */
public class ReceiveVoiceMessage {
	
	private String toUserName;	//开发者微信号  
	private String fromUserName;//发送方帐号（一个OpenID）  
	private String createTime;	//消息创建时间 （整型）  
	private String msgType;		//voice
	
	private String mediaId;		//语音消息媒体id，可以调用多媒体文件下载接口拉取数据
	private String format;		//语音格式，如amr，speex等
	private String msgId;		//消息id，64位整型
	private String recognition;	//语音识别结果，UTF8编码
	
	public ReceiveVoiceMessage() {
		super();
	}
	
	public String toString(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("ToUserName=[").append(toUserName).append("],")
				 .append("FromUserName=[").append(fromUserName).append("],")
				 .append("CreateTime=[").append(createTime).append("],")
				 .append("MsgType=[").append(msgType).append("]");
		if (mediaId != null)
			tmpBuffer.append(",MediaId=[").append(mediaId).append("]");
		if (format != null)
			tmpBuffer.append(",Format=[").append(format).append("]");
		if (recognition != null)
			tmpBuffer.append(",Recognition=[").append(recognition).append("]");
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
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

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	
}