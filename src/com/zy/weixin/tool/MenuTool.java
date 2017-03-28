package com.zy.weixin.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.MenuJson;
import com.zy.weixin.json.MenuListJson;
import com.zy.weixin.json.QueryMenuJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信菜单操作的工具类
 * @author zy20022630
 */
public class MenuTool {

	private HttpClient client;
	
    /**
     * (私有的)无参构造器
     */
	private MenuTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static MenuTool instance = new MenuTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static MenuTool getInstance() {
		return instance;
	}
	
	/**
	 * 创建菜单
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param createMenuJson --CreateMenuJson*-- 创建自定义菜单JSON对象
	 * @return true表示操作成功
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public boolean createMenu(final String accessToken, MenuListJson createMenuJson) throws WeiXinException {
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法创建菜单");
		if (createMenuJson == null || createMenuJson.getButton() == null)
			throw new WeiXinException("没有设置参数，无法创建菜单");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		List<MenuJson> button = null;
		List<MenuJson> subButton = null;
		PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	
		try {
			/*
			 * 效验菜单数据
			 */
			button = createMenuJson.getButton();
			if (button.isEmpty() || button.size() > 3)
				throw new WeiXinException("一级菜单超过3个，不能创建菜单");
			for (MenuJson menuJson : button){
				if (menuJson.getName() != null && ToolUtil.calculatePlaces(menuJson.getName()) > 8)
					throw new WeiXinException("一级菜单名称超过4个汉字或8个字母，不能创建菜单");
				
				subButton = menuJson.getSub_button();
				if (subButton == null || subButton.isEmpty())
					continue;
				else if (subButton.size() > 5)
					throw new WeiXinException("二级菜单超过5个，不能创建菜单");

				for (MenuJson subMenuJson : subButton){
					if (subMenuJson.getName() != null && ToolUtil.calculatePlaces(subMenuJson.getName()) > 14)
						throw new WeiXinException("二级菜单名称超过7个汉字或14个字母，不能创建菜单");
				}
			}
			
			tmpStr = WeixinConfig.getConfig("weixin.menu.create.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
			
    		postMethod = new PostMethod(tmpStr);
			postMethod.setRequestBody(JSON.toJSONString(createMenuJson));
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
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【创建菜单失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
		}
		
		return false;
	}
	
	/**
	 * 删除菜单
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @return true表示操作成功
	 * @throws WeiXinException
	 */
	public boolean deleteMenu(final String accessToken) throws WeiXinException {
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法删除菜单");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.menu.delete.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
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
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【删除菜单失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
	    	tmpStr = null;
			rtnMsgJson = null;
    	}
		
		return false;
	}
	
	/**
	 * 查询菜单
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @return null 或 创建自定义菜单JSON对象
	 * @throws WeiXinException
	 */
	public QueryMenuJson getMenuList(final String accessToken) throws WeiXinException {
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法查询菜单");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	QueryMenuJson queryMenuJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.menu.query.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
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
	            	queryMenuJson = JSON.parseObject(tmpStr, QueryMenuJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【查询菜单失败】失败原因：").append(e.getMessage()).toString());
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
		
		return queryMenuJson;
	}
	
}