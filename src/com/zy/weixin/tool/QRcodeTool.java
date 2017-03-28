package com.zy.weixin.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.QRcodeMsgJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信二维码的工具类
 * @author zy20022630
 */
public class QRcodeTool {

	private HttpClient client;
	
    /**
     * (私有的)无参构造器
     */
	private QRcodeTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static QRcodeTool instance = new QRcodeTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static QRcodeTool getInstance() {
		return instance;
	}
	
	/**
	 * 创建临时二维码
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param sceneId --Long*-- 自设定的场景值ID(32位整型)（大于100000）
	 * @param expireSeconds --Long*-- 二维码有效时间，以秒为单位（ 最大不超过604800（即7天）） 
	 * @return null 或 CommonReturnMsgJson对象 或 QRcodeMsgJson对象
	 * @throws WeiXinException 
	 */
	@SuppressWarnings("deprecation")
	public Object createTempQRcode(final String accessToken, Long sceneId, Long expireSeconds) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法创建临时二维码");

		if (sceneId == null || sceneId.longValue() <= 100000)
			throw new WeiXinException("参数[自设定的场景值ID]设值有错");

		if (expireSeconds == null || expireSeconds.longValue() > 604800)
			throw new WeiXinException("参数[有效时间]设值有错");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpStr = null;
		JSONObject jsonObject = null;
		JSONObject jsonObject1 = null;
		JSONObject jsonObject2 = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	CommonReturnMsgJson errorMsgJson = null;
    	QRcodeMsgJson qrcodeMsgJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.qrcode.create.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		jsonObject = new JSONObject();
    		jsonObject.put("expire_seconds", expireSeconds);
    		jsonObject.put("action_name", "QR_SCENE");
    		
    		jsonObject1 = new JSONObject();
    		jsonObject1.put("scene_id", sceneId);
    		jsonObject2 = new JSONObject();
    		jsonObject2.put("scene", jsonObject1);
    		jsonObject.put("action_info", jsonObject2);
		
    		postMethod = new PostMethod(tmpStr);
			postMethod.setRequestBody(jsonObject.toJSONString());
			postMethod.getParams().setContentCharset(WeixinConfig.getConfig("weixin.url.encoding"));
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = postMethod.getResponseBodyAsString();
				if (ToolUtil.isStrEmpty(tmpStr))
					throw new WeiXinException("响应中的返回值为空");

				if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1){//表示失败
	            	errorMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            	return errorMsgJson;
	            }else{
	            	qrcodeMsgJson = JSON.parseObject(tmpStr, QRcodeMsgJson.class);
	            	return qrcodeMsgJson;
	            }
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【创建临时二维码失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			tmpStr = null;
			jsonObject = null;
			jsonObject1 = null;
			jsonObject2 = null;
			if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
	    	errorMsgJson = null;
	    	qrcodeMsgJson = null;
		}
	}
	
	/**
	 * 创建永久二维码
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param sceneId --Long-- 自设定的场景值ID（整形）（只支持1--100000）
	 * @param sceneStr --String-- 自设定的场景值ID（字符串形式的ID）（长度限制为1到64）
	 * @return null 或 CommonReturnMsgJson对象 或 QRcodeMsgJson对象
	 * @throws WeiXinException 
	 */
	@SuppressWarnings("deprecation")
	public Object createQRcode(final String accessToken, Long sceneId, String sceneStr) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法创建永久二维码");
		if (sceneId == null && ToolUtil.isStrEmpty(sceneStr))
			throw new WeiXinException("参数[自设定的场景值ID]不能为空");
		if (sceneId != null && (sceneId.intValue() < 1 || sceneId.intValue() > 100000))
			throw new WeiXinException("参数[自设定的场景值ID]sceneId设值有错");
		if (!ToolUtil.isStrEmpty(sceneStr) && (sceneStr.length() < 1 || sceneStr.length() > 64))
			throw new WeiXinException("参数[自设定的场景值ID]sceneStr设值有错");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpStr = null;
		JSONObject jsonObject = null;
		JSONObject jsonObject1 = null;
		JSONObject jsonObject2 = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	CommonReturnMsgJson errorMsgJson = null;
    	QRcodeMsgJson qrcodeMsgJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.qrcode.create.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		jsonObject = new JSONObject();
    		if (sceneId != null){
    			jsonObject.put("action_name", "QR_LIMIT_SCENE");
        		jsonObject1 = new JSONObject();
        		jsonObject1.put("scene_id", sceneId);
        		jsonObject2 = new JSONObject();
        		jsonObject2.put("scene", jsonObject1);
        		jsonObject.put("action_info", jsonObject2);
    		} else {
    			jsonObject.put("action_name", "QR_LIMIT_STR_SCENE");
        		jsonObject1 = new JSONObject();
        		jsonObject1.put("scene_str", sceneStr);
        		jsonObject2 = new JSONObject();
        		jsonObject2.put("scene", jsonObject1);
        		jsonObject.put("action_info", jsonObject2);
    		}
    		
    		postMethod = new PostMethod(tmpStr);
			postMethod.setRequestBody(jsonObject.toJSONString());
			postMethod.getParams().setContentCharset(WeixinConfig.getConfig("weixin.url.encoding"));
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = postMethod.getResponseBodyAsString();
				if (ToolUtil.isStrEmpty(tmpStr))
					throw new WeiXinException("响应中的返回值为空");

				if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1){//表示失败
	            	errorMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            	return errorMsgJson;
	            }else{
	            	qrcodeMsgJson = JSON.parseObject(tmpStr, QRcodeMsgJson.class);
	            	return qrcodeMsgJson;
	            }
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【创建临时二维码失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			tmpStr = null;
			jsonObject = null;
			jsonObject1 = null;
			jsonObject2 = null;
			if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
	    	errorMsgJson = null;
	    	qrcodeMsgJson = null;
		}
	}
	
	/**
	 * 下载二维码图片
	 * @param ticket --String*-- 二维码ticket
	 * @param filePath --String*-- 存放目标文件的绝对路径(绝对目录+文件名+.+后缀名)
	 * @return true表示成功
	 * @throws WeiXinException 
	 */
	public boolean downloadQRcode(String ticket, String filePath) throws WeiXinException{
		if (ToolUtil.isStrEmpty(ticket) || ToolUtil.isStrEmpty(filePath))
			throw new WeiXinException("没有设置参数，无法下载二维码图片");
		
		StringBuffer tmpBuffer = new StringBuffer();
    	int status = 0;
    	String tmpStr = null;
    	GetMethod getMethod = null;
    	
    	InputStream inputStream = null;
    	File file = null;
    	FileOutputStream fileOutputStream = null;
    	byte[] b = null;
    	int j = 0;

    	try {
    		//效验
    		file = new File(filePath);
    		if (file == null || file.isDirectory())
    			throw new WeiXinException("传入的存放目标文件的绝对路径有误，无法下载二维码图片");

    		if (file.exists())
    			file.delete();
    		
    		tmpStr = WeixinConfig.getConfig("weixin.qrcode.gain.address");
    		tmpStr = tmpStr.replaceAll("TICKET", URLEncoder.encode(ticket, WeixinConfig.getConfig("weixin.url.encoding")));
    		
    		getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				inputStream = getMethod.getResponseBodyAsStream();
				fileOutputStream = new FileOutputStream(file);
				b = new byte[1024];
				j = 0;
				while ((j = inputStream.read(b)) != -1) {
					fileOutputStream.write(b, 0, j);
				}
				fileOutputStream.flush();
				
				//验证二维码图片下载成功
				file = new File(filePath);
				if (file != null && file.exists() && file.isFile() && file.canRead() && file.length() > 0)
					return true;
			}else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
			
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【下载二维码图片失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			tmpStr = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
				} finally{
					inputStream = null;
				}
			}
			file = null;
			if (fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
				} finally{
					fileOutputStream = null;
				}
			}
			b = null;
		}
		
		return false;
	}
	
}