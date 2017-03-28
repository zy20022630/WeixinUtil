package com.zy.weixin.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.domain.ReceiveEventMessage;
import com.zy.weixin.domain.ReceiveImageMessage;
import com.zy.weixin.domain.ReceiveLinkMessage;
import com.zy.weixin.domain.ReceiveLocationMessage;
import com.zy.weixin.domain.ReceiveTextMessage;
import com.zy.weixin.domain.ReceiveVideoMessage;
import com.zy.weixin.domain.ReceiveVoiceMessage;
import com.zy.weixin.util.ToolUtil;

/**
 * 解析接收消息的工具类
 * @author zy20022630
 */
public class MessageAnalyzeTool {
	
	private static final String ENCODING_UTF8 = "UTF-8";
	
	/**
	 * 从请求中(即从HttpServletRequest对象中)获取消息XML字符串
	 * @param inputStream --InputStream*-- HttpServletRequest对象的InputStream(通过request.getInputStream()获取)
	 * @return null或消息XML字符串
	 * @throws WeiXinException 
	 */
	public static String getXmlMessageFromRequest(InputStream inputStream) throws WeiXinException{
		if (inputStream == null)
			return null;
		
		InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	StringBuffer tmpBuffer = null;
    	String tmpStr = null;
    	
    	try {
			tmpBuffer = new StringBuffer();
			inputStreamReader = new InputStreamReader(inputStream, ENCODING_UTF8);
			reader = new BufferedReader(inputStreamReader);
			while ((tmpStr = reader.readLine()) != null) {
			    tmpBuffer.append(tmpStr);
			}
			return tmpBuffer.toString();
		} catch (Exception e) {
			throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【从请求中获取消息XML字符串失败】失败原因：").append(e.getMessage()).toString());
		} finally {
        	//清空
			tmpStr = null;
			tmpBuffer = null;
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				} finally{
					reader = null;
				}
			}
			if (inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
				} finally{
					inputStreamReader = null;
				}
			}
		}
	}
	
	/**
	 * 解析(接收到的消息)XML字符串，返回对应对象
	 * @param strXML --String*-- (从请求中获取到的消息)XML字符串
	 * @return null 或 ReceiveTextMessage 或 ReceiveImageMessage 或 ReceiveVoiceMessage 或 ReceiveVideoMessage 或 ReceiveLocationMessage 或 ReceiveLinkMessage 或 ReceiveEventMessage
	 * @throws WeiXinException 
	 */
	public static Object parseReceiveMessage(String strXML) throws WeiXinException{
		StringBuffer tmpBuffer = null;
		Map<String, String> map = null;
		String tmpStr = null;
		String event = null;
		ReceiveTextMessage textMessage = null;
		ReceiveImageMessage imageMessage = null;
		ReceiveVoiceMessage voiceMessage = null;
		ReceiveVideoMessage videoMessage = null;
		ReceiveLocationMessage locationMessage = null;
		ReceiveLinkMessage linkMessage = null;
		ReceiveEventMessage eventMessage = null;
		
		try {
			tmpBuffer = new StringBuffer();
			map = parseXml(strXML);
			tmpStr = map.get("MsgType");//text、image、voice、video、location、link、event
			if ("text".equals(tmpStr)){//0-text
				textMessage = new ReceiveTextMessage();
				textMessage.setToUserName(map.get("ToUserName"));
				textMessage.setFromUserName(map.get("FromUserName"));
				textMessage.setCreateTime(map.get("CreateTime"));
				textMessage.setMsgType(tmpStr);
				textMessage.setContent(map.get("Content"));
				textMessage.setMsgId(map.get("MsgId"));
				return textMessage;
			}else if ("image".equals(tmpStr)){//1-image
				imageMessage = new ReceiveImageMessage();
				imageMessage.setToUserName(map.get("ToUserName"));
				imageMessage.setFromUserName(map.get("FromUserName"));
				imageMessage.setCreateTime(map.get("CreateTime"));
				imageMessage.setMsgType(tmpStr);
				imageMessage.setPicUrl(map.get("PicUrl"));
				imageMessage.setMediaId(map.get("MediaId"));
				imageMessage.setMsgId(map.get("MsgId"));
				return imageMessage;
			}else if ("voice".equals(tmpStr)){//2-voice
				voiceMessage = new ReceiveVoiceMessage();
				voiceMessage.setToUserName(map.get("ToUserName"));
				voiceMessage.setFromUserName(map.get("FromUserName"));
				voiceMessage.setCreateTime(map.get("CreateTime"));
				voiceMessage.setMsgType(tmpStr);
				voiceMessage.setMediaId(map.get("MediaId"));
				voiceMessage.setFormat(map.get("Format"));
				voiceMessage.setMsgId(map.get("MsgId"));
				voiceMessage.setRecognition(map.get("Recognition"));
				return voiceMessage;
			}else if ("video".equals(tmpStr)){//3-video
				videoMessage = new ReceiveVideoMessage();
				videoMessage.setToUserName(map.get("ToUserName"));
				videoMessage.setFromUserName(map.get("FromUserName"));
				videoMessage.setCreateTime(map.get("CreateTime"));
				videoMessage.setMsgType(tmpStr);
				videoMessage.setMediaId(map.get("MediaId"));
				videoMessage.setThumbMediaId(map.get("ThumbMediaId"));
				videoMessage.setMsgId(map.get("MsgId"));
				return videoMessage;
			}else if ("location".equals(tmpStr)){//4-location
				locationMessage = new ReceiveLocationMessage();
				locationMessage.setToUserName(map.get("ToUserName"));
				locationMessage.setFromUserName(map.get("FromUserName"));
				locationMessage.setCreateTime(map.get("CreateTime"));
				locationMessage.setMsgType(tmpStr);
				locationMessage.setLocationX(map.get("Location_X"));
				locationMessage.setLocationY(map.get("Location_Y"));
				locationMessage.setScale(map.get("Scale"));
				locationMessage.setLabel(map.get("Label"));
				locationMessage.setMsgId(map.get("MsgId"));
				return locationMessage;
			}else if ("link".equals(tmpStr)){//5-link
				linkMessage = new ReceiveLinkMessage();
				linkMessage.setToUserName(map.get("ToUserName"));
				linkMessage.setFromUserName(map.get("FromUserName"));
				linkMessage.setCreateTime(map.get("CreateTime"));
				linkMessage.setMsgType(tmpStr);
				linkMessage.setTitle(map.get("Title"));
				linkMessage.setDescription(map.get("Description"));
				linkMessage.setUrl(map.get("Url"));
				linkMessage.setMsgId(map.get("MsgId"));
				return linkMessage;
			}else if ("event".equals(tmpStr)){//6-event
				event = map.get("Event");
				if (ToolUtil.isStrEmpty(event))
					return null;
				else if ("scancode_push".equals(event))	//scancode_push：扫码推事件的事件推送
					return null;
				else if ("scancode_waitmsg".equals(event))//scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框的事件推送
					return null;
				else if ("pic_sysphoto".equals(event))//pic_sysphoto：弹出系统拍照发图的事件推送
					return null;
				else if ("pic_photo_or_album".equals(event))//pic_photo_or_album：弹出拍照或者相册发图的事件推送
					return null;
				else if ("pic_weixin".equals(event))//pic_weixin：弹出微信相册发图器的事件推送
					return null;
				else if ("location_select".equals(event))//location_select：弹出地理位置选择器的事件推送
					return null;
				
				eventMessage = new ReceiveEventMessage();
				eventMessage.setToUserName(map.get("ToUserName"));
				eventMessage.setFromUserName(map.get("FromUserName"));
				eventMessage.setCreateTime(map.get("CreateTime"));
				eventMessage.setMsgType(tmpStr);
				eventMessage.setEvent(map.get("Event"));
				eventMessage.setEventKey(map.get("EventKey"));
				eventMessage.setTicket(map.get("Ticket"));
				eventMessage.setLatitude(map.get("Latitude"));
				eventMessage.setLongitude(map.get("Longitude"));
				eventMessage.setPrecision(map.get("Precision"));
				eventMessage.setMsgID(map.get("MsgID"));
				eventMessage.setStatus(map.get("Status"));
				
				return eventMessage;
			}else{
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append(tmpStr).append("消息类型暂不存在").toString());
			}
		} catch (Exception e) {
			throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【解析(接收到的消息)XML字符串失败】失败原因：").append(e.getMessage()).toString());
		} finally {
        	//清空
			tmpStr = null;
        	tmpBuffer = null;
        	if (map != null)
        		map.clear();
        	map = null;
        }
	}
	
	/**
	 * 解析(从请求中获取到的消息)XML字符串
	 * @param strXML --String*-- (从请求中获取到的消息)XML字符串
	 * @return 放置节点信息的Map(KEY-name, VALUE-text)
	 * @throws WeiXinException 
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, String> parseXml(String strXML) throws WeiXinException{
		Map<String, String> map = new HashMap<String, String>();
		
		if (ToolUtil.isStrEmpty(strXML))
			return map;
		
        Document document = null;
        Element rootElement = null;
        Element element = null;
        List list = null;
        StringBuffer tmpBuffer = null;
        try{
        	tmpBuffer = new StringBuffer();
            document = DocumentHelper.parseText(strXML);
            rootElement = document.getRootElement();
            list = rootElement.elements();
            for (Object obj : list){
                element = (Element) obj;
                map.put(element.getName(), element.getText());
            }
        } catch (Exception e){
        	throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【从请求中获取消息XML字符串失败】失败原因：").append(e.getMessage()).toString());
        } finally {
        	//清空
        	tmpBuffer = null;
            document = null;
            rootElement = null;
            element = null;
            if (list != null)
                list.clear();
            list = null;
        }
        return map;
	}
}