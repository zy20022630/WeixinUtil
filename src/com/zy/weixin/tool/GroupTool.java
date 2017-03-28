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
import com.alibaba.fastjson.JSONObject;
import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.CreateGroupJson;
import com.zy.weixin.json.GroupListJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信用户分组的工具类
 * @author zy20022630
 */
public class GroupTool {
	
	private HttpClient client;
	
    /**
     * (私有的)无参构造器
     */
	private GroupTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static GroupTool instance = new GroupTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static GroupTool getInstance() {
		return instance;
	}
	
	/**
	 * 创建分组
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param groupName --String*-- 分组名字(30个字符以内)
	 * @return null 或 CreateGroupJson对象
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public CreateGroupJson createGroup(final String accessToken, String groupName) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法创建分组");
		
		if (ToolUtil.isStrEmpty(groupName))
			throw new WeiXinException("没有设置参数，无法创建分组");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		JSONObject subJSONObject = null;
		JSONObject jsonObject = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	CreateGroupJson createGroupJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.group.create.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		subJSONObject = new JSONObject();
    		subJSONObject.put("name", groupName);
    		jsonObject = new JSONObject();
    		jsonObject.put("group", subJSONObject);
    		
    		postMethod = new PostMethod(tmpStr);
			postMethod.setRequestBody(jsonObject.toJSONString());
			postMethod.getParams().setContentCharset(WeixinConfig.getConfig("weixin.url.encoding"));
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = postMethod.getResponseBodyAsString();
				
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1)//表示失败
	            	rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            else
	            	createGroupJson = JSON.parseObject(tmpStr, CreateGroupJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【创建分组失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			subJSONObject = null;
			jsonObject = null;
			if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
	    	tmpStr = null;
	    	rtnMsgJson = null;
    	}
		
		return createGroupJson;
	}
	
	/**
	 * 获取分组列表
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @return null 或 GroupListJson对象
	 * @throws WeiXinException
	 */
	public GroupListJson getGroupList(final String accessToken) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法获取分组列表");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	GroupListJson groupListJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.group.query.address");
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
	            	groupListJson = JSON.parseObject(tmpStr, GroupListJson.class);
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【获取分组列表失败】失败原因：").append(e.getMessage()).toString());
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
		
		return groupListJson;
	}
	
	/**
	 * 查询用户所在分组
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param openId --String*-- 用户的OpenID
	 * @return null 或 用户所属的groupid
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public String getGroupId(final String accessToken, String openId) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法查询用户所在分组");
		
		if (ToolUtil.isStrEmpty(openId))
			throw new WeiXinException("没有设置参数，无法查询用户所在分组");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		JSONObject jsonObject = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
		String groupId = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.group.getGroupId.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		jsonObject = new JSONObject();
    		jsonObject.put("openid", openId);
    		
    		postMethod = new PostMethod(tmpStr);
			postMethod.setRequestBody(jsonObject.toJSONString());
			postMethod.getParams().setContentCharset(WeixinConfig.getConfig("weixin.url.encoding"));
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = postMethod.getResponseBodyAsString();
				
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
				
	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1)//表示失败
	            	rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            else {
	            	groupId = JSON.parseObject(tmpStr).getString("groupid");
	            }
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【查询用户所在分组失败】失败原因：").append(e.getMessage()).toString());
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
		
		return groupId;
	}
	
	/**
	 * 修改分组名称
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param groupId --String*-- 分组ID
	 * @param groupName --String*-- 分组名字(30个字符以内)
	 * @return true表示操作成功
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public boolean updateGroupName(final String accessToken, String groupId, String groupName) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法修改分组名称");
		
		if (ToolUtil.isStrEmpty(groupId) || ToolUtil.isStrEmpty(groupName))
			throw new WeiXinException("没有设置参数，无法修改分组名称");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		JSONObject subJSONObject = null;
		JSONObject jsonObject = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.group.modifyName.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		subJSONObject = new JSONObject();
    		subJSONObject.put("id", groupId);
    		subJSONObject.put("name", groupName);
    		jsonObject = new JSONObject();
    		jsonObject.put("group", subJSONObject);
    		
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
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【修改分组名称失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			subJSONObject = null;
			jsonObject = null;
			if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
	    	tmpStr = null;
	    	rtnMsgJson = null;
    	}
		
		return false;
	}
	
	/**
	 * 移动用户到新的分组
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param openId --String*-- 用户的OpenID
	 * @param toGroupId --String*-- 新的分组ID
	 * @return true表示操作成功
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public boolean moveUserToGroup(final String accessToken, String openId, String toGroupId) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法移动用户到新的分组");
		
		if (ToolUtil.isStrEmpty(openId) || ToolUtil.isStrEmpty(toGroupId))
			throw new WeiXinException("没有设置参数，无法移动用户到新的分组");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		JSONObject jsonObject = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
		
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.group.moveUser.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		jsonObject = new JSONObject();
    		jsonObject.put("openid", openId);
    		jsonObject.put("to_groupid", toGroupId);
    		
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
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【移动用户到新的分组失败】失败原因：").append(e.getMessage()).toString());
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
	
	/**
	 * 批量移动用户到新的分组
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param openIdList --List*-- 用户的OpenID的List集合（size不能超过50）
	 * @param toGroupId --String*-- 新的分组ID
	 * @return true表示操作成功
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public boolean batchMoveUserToGroup(final String accessToken, List<String> openIdList, String toGroupId) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法批量移动用户到新的分组");
		
		if (openIdList == null || openIdList.isEmpty() || ToolUtil.isStrEmpty(toGroupId))
			throw new WeiXinException("没有设置参数，无法批量移动用户到新的分组");
		else if (openIdList.size() > 50)
			throw new WeiXinException("OpenID的列表大小超过50，无法批量移动用户到新的分组");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		JSONObject jsonObject = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.group.batchMoveUser.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		
    		jsonObject = new JSONObject();
    		jsonObject.put("openid_list", openIdList);
    		jsonObject.put("to_groupid", toGroupId);
    		
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
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【批量移动用户到新的分组失败】失败原因：").append(e.getMessage()).toString());
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