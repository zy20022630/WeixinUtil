package com.zy.weixin.domain;

/**
 * 接收的事件消息<br/><br/>
 * 不同类型的差异部分：<br/>
 * 1、关注/取消关注事件：<br/>
 * Event  事件类型，subscribe(订阅)、unsubscribe(取消订阅)
 * <br/><br/>
 * 
 * 2、扫描带参数二维码事件<br/>
 * (1)、用户未关注时，进行关注后的事件推送<br/>
 * Event  事件类型，subscribe  <br/>
 * EventKey  事件KEY值，qrscene_为前缀，后面为二维码的参数值  <br/>
 * Ticket  二维码的ticket，可用来换取二维码图片 <br/>
 * (2)、用户已关注时的事件推送<br/>
 * Event  事件类型，subscribe  <br/>
 * EventKey  事件KEY值，是一个32位无符号整数  <br/>
 * Ticket  二维码的ticket，可用来换取二维码图片
 * <br/><br/>
 * 
 * 3、上报地理位置事件<br/>
 * Event  事件类型，LOCATION  <br/>
 * Latitude  地理位置纬度  <br/>
 * Longitude  地理位置经度  <br/>
 * Precision  地理位置精度
 * <br/><br/>
 * 
 * 4、自定义菜单事件<br/>
 * Event  事件类型，CLICK  <br/>
 * EventKey  事件KEY值，与自定义菜单接口中KEY值对应
 * <br/><br/>
 * 
 * 5、模板消息发送后的反馈<br/>
 * Event	事件为模板消息发送结束（即TEMPLATESENDJOBFINISH）
 * MsgID	消息id
 * Status	发送状态（成功、用户拒绝接收、发送失败[非用户拒绝]）
 * 
 * @author zy20022630
 */
public class ReceiveEventMessage {
	
	private String toUserName;	//开发者微信号  
	private String fromUserName;//发送方帐号（一个OpenID）  
	private String createTime;	//消息创建时间 （整型）  
	private String msgType;		//event
	
	private String event;		//事件类型，subscribe(订阅)、unsubscribe(取消订阅)、LOCATION(上报地理位置)、CLICK(菜单点击)、TEMPLATESENDJOBFINISH（模板消息发送结束）
	private String eventKey;	//事件KEY值，qrscene_为前缀，后面为二维码的参数值
	private String ticket;		//二维码的ticket，可用来换取二维码图片 
	private String latitude;	//地理位置纬度
	private String longitude;	//地理位置经度
	private String precision;	//地理位置精度
	private String msgID;	//消息id
	private String status;	//发送状态（成功、用户拒绝接收、发送失败[非用户拒绝]）

	public ReceiveEventMessage() {
		super();
	}
	
	public String toString(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("ToUserName=[").append(toUserName).append("],")
				 .append("FromUserName=[").append(fromUserName).append("],")
				 .append("CreateTime=[").append(createTime).append("],")
				 .append("MsgType=[").append(msgType).append("]");
		if (event != null)
			tmpBuffer.append(",Event=[").append(event).append("]");
		if (eventKey != null)
			tmpBuffer.append(",EventKey=[").append(eventKey).append("]");
		if (ticket != null)
			tmpBuffer.append(",Ticket=[").append(ticket).append("]");
		if (latitude != null)
			tmpBuffer.append(",Latitude=[").append(latitude).append("]");
		if (longitude != null)
			tmpBuffer.append(",Longitude=[").append(longitude).append("]");
		if (precision != null)
			tmpBuffer.append(",Precision=[").append(precision).append("]");
		if (msgID != null)
			tmpBuffer.append(",MsgID=[").append(msgID).append("]");
		if (status != null)
			tmpBuffer.append(",Status=[").append(status).append("]");
		return tmpBuffer.toString();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}