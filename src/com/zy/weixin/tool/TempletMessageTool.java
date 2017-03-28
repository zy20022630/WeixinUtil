package com.zy.weixin.tool;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.TempletMessageJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信模板消息的工具类
 * @author zy20022630
 */
public class TempletMessageTool {
	
	private HttpClient client;
	
    /**
     * (私有的)无参构造器
     */
	private TempletMessageTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static TempletMessageTool instance = new TempletMessageTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static TempletMessageTool getInstance() {
		return instance;
	}
	
	/**
	 * 发送模板消息
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param openid --String*-- 微信用户OPENID
	 * @param template_id --String*-- 模板ID
	 * @param url --String-- 链接地址
	 * @param topcolor --String-- TOP颜色
	 * @param jsonData --JSONObject-- json格式的参数信息字符串
	 * @return 响应字符串
	 * @throws WeiXinException
	 */
	@SuppressWarnings("deprecation")
	public String send(final String accessToken, String openid, String template_id, String url, String topcolor, JSONObject jsonData) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法发送模板消息");
		
		if (ToolUtil.isStrEmpty(openid))
			throw new WeiXinException("没有设置参数[openid]，无法发送模板消息");
		
		if (ToolUtil.isStrEmpty(template_id))
			throw new WeiXinException("没有设置参数[template_id]，无法发送模板消息");
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		JSONObject jsonObject = null;
    	PostMethod postMethod = null;
    	int status = 0;
    	String tmpStr = null;
    	
    	try {
    		tmpStr = WeixinConfig.getConfig("weixin.templet.message.send.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);

    		jsonObject = new JSONObject();
    		jsonObject.put("touser", openid);
    		jsonObject.put("template_id", template_id);
    		if (!ToolUtil.isStrEmpty(url))
    			jsonObject.put("url", url);
    		if (!ToolUtil.isStrEmpty(topcolor))
    			jsonObject.put("topcolor", topcolor);
    		if (jsonData != null)
    			jsonObject.put("data", jsonData);

    		postMethod = new PostMethod(tmpStr);
			postMethod.setRequestBody(jsonObject.toJSONString());
			postMethod.getParams().setContentCharset(WeixinConfig.getConfig("weixin.url.encoding"));
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				tmpStr = postMethod.getResponseBodyAsString();
				
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
	            
	            return tmpStr;
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【发送模板消息】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			jsonObject = null;
			if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
	    	tmpStr = null;
    	}
	}
	
	/**
	 * 格式化（发送模板消息的响应信息）字符串
	 * @param response --String*-- （发送模板消息的响应信息）字符串
	 * @return TempletMessageJson对象
	 * @throws WeiXinException
	 */
	public TempletMessageJson parseObjectForResponseOfSend(String response) throws WeiXinException{
		if (ToolUtil.isStrEmpty(response))
			throw new WeiXinException("响应中的返回值为空");
		return JSON.parseObject(response, TempletMessageJson.class);
	}
	
}