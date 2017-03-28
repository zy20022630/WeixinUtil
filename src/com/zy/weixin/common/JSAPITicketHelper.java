package com.zy.weixin.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.zy.weixin.json.GetJSAPITicketJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * (单)公众帐号操作jsapi_ticket（公众号用于调用微信JS-SDK接口的临时票据）的帮助类
 * @author zy20022630
 */
public class JSAPITicketHelper {

	private HttpClient client;
	
	private String access_token = null;		//获取到的凭证
    
    /*
     * 正确的Json返回结果：
     */
	private String ticket;		//公众号用于调用微信JS接口的临时票据
    private int expires_in = 0;	//凭证有效期（单位：秒）
    
    /*
     * 响应的Json结果：
     */
	private int errcode;
	private String errmsg;

    /**
     * (私有的)无参构造器
     */
    private JSAPITicketHelper(){
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
    
    //定义一个静态实例
	private static JSAPITicketHelper instance = new JSAPITicketHelper();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static JSAPITicketHelper getInstance() {
		return instance;
	}
	
	/**
	 * 获取jsapi_ticket操作，失败会重复请求获取
	 * @param access_token --String*-- 动态凭证
	 * @return true表示成功获取jsapi_ticket
	 * @throws WeiXinException
	 */
    public boolean getCredential(String access_token) throws WeiXinException{
    	if (ToolUtil.isStrEmpty(access_token))
    		throw new WeiXinException("非空参数存在空值");

    	StringBuffer tmpBuffer = null;

    	try {
    		this.access_token = access_token;
        	
    		//重新获取凭证
    		if (imitateGetCredential())
    			return true;
    		else{
    			tmpBuffer = new StringBuffer();
    			throw new WeiXinException(tmpBuffer.append("【获取jsapi_ticket失败】【错误代码：").append(this.errcode).append("】【错误信息：").append(this.errmsg).append("】").toString());
    		}
    	} finally {
        	//清空
        	tmpBuffer = null;
    	}
    }
	
    /**
     * 获取有效的jsapi_ticket
     * @return null或jsapi_ticket
     */
    public String getJSAPITicket(){
    	if (ToolUtil.isStrEmpty(this.ticket))
    		return null;
    	
    	return this.ticket;
    }
    
    /**
     * 获取有效期
     * @return null或jsapi_ticket
     */
    public int getExpires_in(){
    	return this.expires_in;
    }
    
    /**
     * 清空jsapi_ticket
     */
    public void clearJSAPITicket(){
    	this.access_token = null;
    	this.ticket = null;
    	this.expires_in = 0;
		this.errcode = 0;
		this.errmsg = null;
    }
    
    /**
     * 重新获取jsapi_ticket
     * @return true表示成功获取
     * @throws WeiXinException
     */
    private boolean imitateGetCredential() throws WeiXinException {
    	StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	String tmpStr = null;
    	int status = 0;
    	GetJSAPITicketJson sucessJson = null;
    	
        try {
        	/*
        	 * 参数重置
        	 */
        	this.ticket = null;
        	this.expires_in = 0;
			this.errcode = 0;
			this.errmsg = null;
			
			/*
			 * 调用URL方法
			 */
			tmpStr = WeixinConfig.getConfig("weixin.js_sdk.jsapi_ticket.address");
			tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", this.access_token);
			getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			
			/*
			 * 处理http的返回结果
			 */
			if (status == HttpStatus.SC_OK) {
				tmpStr = getMethod.getResponseBodyAsString();
				
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");

	            sucessJson = JSON.parseObject(tmpStr, GetJSAPITicketJson.class);
	            if (sucessJson != null){
	            	if (sucessJson.getErrcode() == 0){
	            		this.errcode = sucessJson.getErrcode();
	            		this.errmsg = sucessJson.getErrmsg();
	            		this.ticket = sucessJson.getTicket();
						this.expires_in = sucessJson.getExpires_in();
						
						return true;
	            	} else {
	            		this.errcode = sucessJson.getErrcode();
	            		this.errmsg = sucessJson.getErrmsg();
	            	}
	            }
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
			
			//程序到此，肯定是失败了
			return false;
        } catch (Exception e) {
        	throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【获取jsapi_ticket失败】失败原因：").append(e.getMessage()).toString());
        } finally {
        	tmpBuffer = null;
        	if (getMethod != null)
        		getMethod.releaseConnection();
        	getMethod = null;
        	tmpStr = null;
        	sucessJson = null;
        }
    }
	
}