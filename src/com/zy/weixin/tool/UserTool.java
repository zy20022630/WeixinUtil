package com.zy.weixin.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
import com.zy.weixin.json.UserInfoJson;
import com.zy.weixin.json.UserListJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信用户操作的工具类
 * @author zy20022630
 */
public class UserTool {

	private HttpClient client;
	
    /**
     * (私有的)无参构造器
     */
	private UserTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static UserTool instance = new UserTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static UserTool getInstance() {
		return instance;
	}
	
	/**
	 * 获取关注人员列表
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param next_openid --String-- 下一个OpenID(用于分批获取)
	 * @return null 或 UserListJson对象
	 * @throws WeiXinException
	 */
	public UserListJson getUserList(final String accessToken, String next_openid) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法获取关注人员列表");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	UserListJson userListJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.user.queryList.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		tmpStr = tmpStr.replaceAll("NEXT_OPENID", ToolUtil.isStrEmpty(next_openid) ? "" : next_openid);
    		
    		getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				inputStreamReader = new InputStreamReader(getMethod.getResponseBodyAsStream(),WeixinConfig.getConfig("weixin.url.encoding"));
				reader = new BufferedReader(inputStreamReader);
				tmpBuffer.delete(0, tmpBuffer.length());
	            while ((tmpStr = reader.readLine()) != null) {
	                tmpBuffer.append(tmpStr);
	            }
	            tmpStr = tmpBuffer.toString();
	            
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1)//表示失败
	            	rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            else
	            	userListJson = JSON.parseObject(tmpStr, UserListJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【获取关注人员列表】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
	    	tmpStr = null;
			if (inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
				} finally{
					inputStreamReader = null;
				}
			}
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				} finally{
					reader = null;
				}
			}
			rtnMsgJson = null;
    	}
		
		return userListJson;
	}
	
	/**
	 * 获取用户基本信息（包括UnionID机制）
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param openId --String*-- 用户的OpenID
	 * @param language --String-- 国家地区语言版本（zh_CN简体，zh_TW繁体，en英语）（默认zh_CN简体）
	 * @return null 或 UserInfoJson对象
	 * @throws WeiXinException
	 */
	public UserInfoJson gainUserInfo(final String accessToken, String openId, String language) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法获取用户基本信息");
		
		if (ToolUtil.isStrEmpty(openId))
			throw new WeiXinException("没有设置参数，无法获取用户基本信息");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	UserInfoJson userInfoJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.user.baseinfo.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		tmpStr = tmpStr.replaceAll("OPENID", openId);
    		tmpStr = tmpStr.replaceAll("LANGUAGE", ToolUtil.isStrEmpty(language) ? WeixinConfig.getConfig("weixin.language.default") : language);
    		
    		getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				inputStreamReader = new InputStreamReader(getMethod.getResponseBodyAsStream(),WeixinConfig.getConfig("weixin.url.encoding"));
				reader = new BufferedReader(inputStreamReader);
				tmpBuffer.delete(0, tmpBuffer.length());
	            while ((tmpStr = reader.readLine()) != null) {
	                tmpBuffer.append(tmpStr);
	            }
	            tmpStr = tmpBuffer.toString();
	            
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1)//表示失败
	            	rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            else
	            	userInfoJson = JSON.parseObject(tmpStr, UserInfoJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【获取用户基本信息】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
	    	tmpStr = null;
			if (inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
				} finally{
					inputStreamReader = null;
				}
			}
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				} finally{
					reader = null;
				}
			}
			rtnMsgJson = null;
    	}
		
		return userInfoJson;
	}
	
	/**
	 * 设置用户备注
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param openId --String*-- 用户的OpenID
	 * @param remark --String*-- 新的备注名（长度必须小于15个汉字）
	 * @return true表示操作成功
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public boolean updateUserRemark(final String accessToken, String openId, String remark) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法设置用户备注");
		
		if (ToolUtil.isStrEmpty(openId) || ToolUtil.isStrEmpty(remark))
			throw new WeiXinException("没有设置参数，无法设置用户备注");
		else if (remark.length() >= 15)
			throw new WeiXinException("备注长度不小于15个汉字，无法设置用户备注");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		JSONObject jsonObject = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
		
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.user.modifyremark.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		jsonObject = new JSONObject();
    		jsonObject.put("openid", openId);
    		jsonObject.put("remark", remark);
    		
    		postMethod = new PostMethod(tmpStr);
			postMethod.setRequestBody(jsonObject.toJSONString());
			postMethod.getParams().setContentCharset(WeixinConfig.getConfig("weixin.url.encoding"));
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = postMethod.getResponseBodyAsString();
				
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            if (rtnMsgJson != null){
					if (rtnMsgJson.getErrcode() == 0)
						return true;
					else{
						throw new WeiXinException(rtnMsgJson.getErrmsg());
					}
				}
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【设置用户备注失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			jsonObject = null;
			if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
	    	tmpStr = null;
	    	rtnMsgJson = null;
    	}
		
		return false;
	}

}