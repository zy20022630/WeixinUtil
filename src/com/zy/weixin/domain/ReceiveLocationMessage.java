package com.zy.weixin.domain;

/**
 * 接收的地理位置消息
 * @author zy20022630
 */
public class ReceiveLocationMessage {

	private String toUserName;	//开发者微信号
	private String fromUserName;//发送方帐号（一个OpenID）
	private String createTime;	//消息创建时间 （整型）
	private String msgType;		//location
	
	private String locationX;	//地理位置维度
	private String locationY;	//地理位置精度
	private String scale;		//地图缩放大小
	private String label;		//地理位置信息
	private String msgId;		//消息id，64位整型
	
	public ReceiveLocationMessage() {
		super();
	}
	
	public String toString(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("ToUserName=[").append(toUserName).append("],")
				 .append("FromUserName=[").append(fromUserName).append("],")
				 .append("CreateTime=[").append(createTime).append("],")
				 .append("MsgType=[").append(msgType).append("]");
		if (locationX != null)
			tmpBuffer.append(",Location_X=[").append(locationX).append("]");
		if (locationY != null)
			tmpBuffer.append(",Location_Y=[").append(locationY).append("]");
		if (scale != null)
			tmpBuffer.append(",Scale=[").append(scale).append("]");
		if (label != null)
			tmpBuffer.append(",Label=[").append(label).append("]");
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
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

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
}