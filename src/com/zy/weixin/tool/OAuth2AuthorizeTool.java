package com.zy.weixin.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.OAuth2AuthorizeJson;
import com.zy.weixin.json.OAuth2AuthorizeUserInfoJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信OAuth2.0网页授权的工具类
 * @author zy20022630
 */
public class OAuth2AuthorizeTool {

	private HttpClient client;
	
	/**
	 * 仅能查看微信用户的OpenId
	 */
	public static final String SCOPE_SNSAPI_BASE = "snsapi_base";
	
	/**
	 * 可以查看微信用户的基本信息
	 */
	public static final String SCOPE_SNSAPI_USERINFO = "snsapi_userinfo";
	
    /**
     * (私有的)无参构造器
     */
	public OAuth2AuthorizeTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static OAuth2AuthorizeTool instance = new OAuth2AuthorizeTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static OAuth2AuthorizeTool getInstance() {
		return instance;
	}
	
	/**
	 * 创建授权网页URL
	 * @param appId --String*-- 第三方用户唯一凭证
	 * @param redirectUrl --String*-- 授权后重定向的回调链接地址(以http://开头)
	 * @param scope --String*-- 应用授权作用域，值为OAuth2AuthorizeTool.SCOPE_SNSAPI_BASE或OAuth2AuthorizeTool.SCOPE_SNSAPI_USERINFO。(snsapi_base：不弹出授权页面，直接跳转，只能获取用户openid；snsapi_userinfo：弹出授权页面，可通过openid拿到昵称、性别、所在地，即使在未关注的情况下，只要用户授权，也能获取其信息)
	 * @param state --String-- 重定向后会带上state参数 (值为a-zA-Z0-9格式)
	 * @return null 或 URL字符串
	 * @throws WeiXinException
	 */
	public String getAuthorizeUrl(String appId, String redirectUrl, String scope, String state) throws WeiXinException{
		if (ToolUtil.isStrEmpty(redirectUrl))
			throw new WeiXinException("没有设置参数[重定向URL]，无法创建授权网页URL");
		
		if (!SCOPE_SNSAPI_BASE.equals(scope) && !SCOPE_SNSAPI_USERINFO.equals(scope))
			throw new WeiXinException("参数[应用授权作用域]值错误，无法创建授权网页URL");
		
		if (!ToolUtil.isStrEmpty(state)){
			if (!ToolUtil.numberOrLetterValidate(state) || state.length() > 128)
				throw new WeiXinException("参数[重定向URL参数state]格式错误，无法创建授权网页URL");
		}

		String strURL = null;
		
		try {
			strURL = WeixinConfig.getConfig("weixin.oauth2.authorize.getCodeAndRedirect");
			strURL = strURL.replaceAll("APPID", appId);
			strURL = strURL.replaceAll("REDIRECT_URI", URLEncoder.encode(redirectUrl, WeixinConfig.getConfig("weixin.url.encoding")));
			strURL = strURL.replaceAll("SCOPE", scope);
			strURL = strURL.replaceAll("STATE", ToolUtil.isStrEmpty(state) ? "" : state);
		} catch (Exception e){
			throw new WeiXinException(e.getMessage());
		}

		return strURL;
	}
	
	/**
	 * 通过code获取网页授权access_token
	 * @param appid --String*-- 第三方用户唯一凭证
	 * @param appsecret --String*-- 第三方用户唯一凭证密钥
	 * @param code --String*-- 换取access_token的临时票据
	 * @return null 或 OAuth2AuthorizeJson对象
	 * @throws WeiXinException
	 */
	public OAuth2AuthorizeJson getAccessTokenByCode(String appid, String appsecret, String code) throws WeiXinException{
		if (ToolUtil.isStrEmpty(appid) || ToolUtil.isStrEmpty(appsecret) || ToolUtil.isStrEmpty(code))
			throw new WeiXinException("没有设置参数，无法获取网页授权access_token");

		String tmpStr = null;
		GetMethod getMethod = null;
		int status = 0;
		StringBuffer tmpBuffer = new StringBuffer();
		CommonReturnMsgJson rtnMsgJson = null;
		OAuth2AuthorizeJson authorizeJson = null;
		
		try {
			//获取URL链接
			tmpStr = WeixinConfig.getConfig("weixin.oauth2.authorize.getAccessToken.address");
			tmpStr = tmpStr.replaceAll("APPID", appid);
			tmpStr = tmpStr.replaceAll("SECRET", appsecret);
			tmpStr = tmpStr.replaceAll("CODE", code);
		
			getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = getMethod.getResponseBodyAsString();
				
				if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1)//表示失败
	            	rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            else
	            	authorizeJson = JSON.parseObject(tmpStr, OAuth2AuthorizeJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
		} catch (Exception e) {
			throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【获取网页授权access_token失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpStr = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
			tmpBuffer = null;
			rtnMsgJson = null;
		}
		
		return authorizeJson;
	}
	
	/**
	 * 根据refresh_token，刷新网页授权access_token
	 * @param appid --String*-- 第三方用户唯一凭证
	 * @param refreshToken --String*-- 换取access_token的票据code
	 * @return null 或 OAuth2AuthorizeJson对象
	 * @throws WeiXinException
	 */
	public OAuth2AuthorizeJson getNewAccessTokenByRefresh(String appid, String refreshToken) throws WeiXinException{
		if (ToolUtil.isStrEmpty(appid) || ToolUtil.isStrEmpty(refreshToken))
			throw new WeiXinException("没有设置参数，无法刷新网页授权access_token");
		
		String tmpStr = null;
		GetMethod getMethod = null;
		int status = 0;
		StringBuffer tmpBuffer = new StringBuffer();
		CommonReturnMsgJson rtnMsgJson = null;
		OAuth2AuthorizeJson authorizeJson = null;
		
		try {
			//获取URL链接
			tmpStr = WeixinConfig.getConfig("weixin.oauth2.authorize.refreshAccessToken.address");
			tmpStr = tmpStr.replaceAll("APPID", appid);
			tmpStr = tmpStr.replaceAll("REFRESH_TOKEN", refreshToken);
		
			getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = getMethod.getResponseBodyAsString();
				
				if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1)//表示失败
	            	rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            else
	            	authorizeJson = JSON.parseObject(tmpStr, OAuth2AuthorizeJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
		} catch (Exception e) {
			throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【刷新网页授权access_token失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpStr = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
			tmpBuffer = null;
			rtnMsgJson = null;
		}
		
		return authorizeJson;
	}
	
	/**
	 * 根据网页授权access_token，获取微信用户信息
	 * @param accessToken --String*-- 网页授权access_token
	 * @param openId --String*-- 微信用户的唯一标识
	 * @param language --String-- 国家地区语言版本（zh_CN简体，zh_TW繁体，en英语）（默认zh_CN简体）
	 * @return null 或 OAuth2AuthorizeUserInfoJson对象
	 * @throws WeiXinException
	 */
	public OAuth2AuthorizeUserInfoJson getUserInfoByAccessToken(String accessToken, String openId, String language) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken) || ToolUtil.isStrEmpty(openId))
			throw new WeiXinException("没有设置参数，无法获取微信用户信息");
	
		String tmpStr = null;
		GetMethod getMethod = null;
		int status = 0;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
		StringBuffer tmpBuffer = new StringBuffer();
		OAuth2AuthorizeUserInfoJson userInfoJson = null;
		CommonReturnMsgJson rtnMsgJson = null;
		
		try {
			//获取URL链接
			tmpStr = WeixinConfig.getConfig("weixin.oauth2.authorize.getUserInfo.address");
			tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
			tmpStr = tmpStr.replaceAll("OPENID", openId);
			tmpStr = tmpStr.replaceAll("LANGUAGE", ToolUtil.isStrEmpty(language) ? WeixinConfig.getConfig("weixin.language.default") : language);
			
			getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				inputStreamReader = new InputStreamReader(getMethod.getResponseBodyAsStream(),WeixinConfig.getConfig("weixin.url.encoding"));
				reader = new BufferedReader(inputStreamReader);
	            while ((tmpStr = reader.readLine()) != null) {
	                tmpBuffer.append(tmpStr);
	            }
	            tmpStr = tmpBuffer.toString();
	            
				if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1)//表示失败
	            	rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            else
	            	userInfoJson = JSON.parseObject(tmpStr, OAuth2AuthorizeUserInfoJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
		} catch (Exception e) {
			throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【获取微信用户信息失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpStr = null;
			tmpBuffer = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
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
	 * 检验授权凭证（网页授权access_token）是否有效
	 * @param accessToken --String*-- 网页授权access_token
	 * @param openId --String*-- 微信用户的唯一标识
	 * @return true表示有效，false表示无效
	 * @throws WeiXinException
	 */
	public boolean checkAccessToken(String accessToken, String openId) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken) || ToolUtil.isStrEmpty(openId))
			throw new WeiXinException("没有设置参数，无法检验授权凭证是否有效");
		
		String tmpStr = null;
		GetMethod getMethod = null;
		int status = 0;
		StringBuffer tmpBuffer = new StringBuffer();
		CommonReturnMsgJson rtnMsgJson = null;
		
		try {
			//获取URL链接
			tmpStr = WeixinConfig.getConfig("weixin.oauth2.authorize.checkAccessToken.address");
			tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
			tmpStr = tmpStr.replaceAll("OPENID", openId);
			
			getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = getMethod.getResponseBodyAsString();
				
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
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
		} catch (Exception e) {
			throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【检验授权凭证是否有效失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpStr = null;
			tmpBuffer = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
			rtnMsgJson = null;
		}
		
		return false;
	}
	
}