package com.zy.weixin.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.GetCredentialJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * (单)公众帐号操作access token的帮助类
 * @author zy20022630
 */
public class AccessTokenHelper {
	
	private HttpClient client;
	
    private String appid;		//第三方用户唯一凭证
    private String appsecret;	//第三方用户唯一凭证密钥，既appsecret
    
    /*
     * 正确的Json返回结果：
     */
    private String access_token = null;		//获取到的凭证
    private int expires_in = 0;				//凭证有效期（单位：秒）
    
    /*
     * 错误的Json返回示例:
     */
    private int errcode = 0;
    private String errmsg = null;
    
    /**
     * (私有的)无参构造器
     */
    private AccessTokenHelper(){
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
    
    //定义一个静态实例
	private static AccessTokenHelper instance = new AccessTokenHelper();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static AccessTokenHelper getInstance() {
		return instance;
	}
	
	/**
	 * 获取凭证操作，失败会重复请求获取
	 * @param appid --String*-- 第三方用户唯一凭证
	 * @param appsecret --String*-- 第三方用户唯一凭证密钥
	 * @return true表示成功获取凭证
	 * @throws WeiXinException
	 */
    public boolean getCredential(String appid, String appsecret) throws WeiXinException{
    	if (ToolUtil.isStrEmpty(appid) || ToolUtil.isStrEmpty(appsecret))
    		throw new WeiXinException("非空参数存在空值");

    	StringBuffer tmpBuffer = null;
    	
    	try {
	    	this.appid = appid;
	    	this.appsecret = appsecret;
        	
    		//重新获取凭证
    		if (imitateGetCredential())
    			return true;
    		else{
    			tmpBuffer = new StringBuffer();
    			throw new WeiXinException(tmpBuffer.append("【获取凭证失败】【错误代码：").append(this.errcode).append("】【错误信息：").append(this.errmsg).append("】").toString());
    		}
    	} finally {
        	//清空
        	tmpBuffer = null;
    	}
    }
    
    /**
     * 获取有效的凭证
     * @return null或凭证字符串
     */
    public String getAccessToken(){
    	if (ToolUtil.isStrEmpty(this.access_token))
    		return null;
    	
    	return this.access_token;
    }
    
    /**
     * 获取有效期
     * @return null或jsapi_ticket
     */
    public int getExpires_in(){
    	return this.expires_in;
    }
    
    /**
     * 清空凭证
     */
    public void clearAccessToken(){
    	this.appid = null;
    	this.appsecret = null;
		this.access_token = null;
		this.expires_in = 0;
		this.errcode = 0;
		this.errmsg = null;
    }
	
    /**
     * 重新获取凭证
     * @return true表示成功获取凭证
     * @throws WeiXinException
     */
    private boolean imitateGetCredential() throws WeiXinException {
    	StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	String tmpStr = null;
    	int status = 0;
    	GetCredentialJson sucessJson = null;
    	CommonReturnMsgJson errorJson = null;
    	
        try {
        	/*
        	 * 参数重置
        	 */
        	this.access_token = null;
        	this.expires_in = 0;
			this.errcode = 0;
			this.errmsg = null;
			
			/*
			 * 调用URL方法
			 */
			tmpStr = WeixinConfig.getConfig("weixin.access_token.address");
			tmpStr = tmpStr.replaceAll("APPID", this.appid);
			tmpStr = tmpStr.replaceAll("APPSECRET", this.appsecret);
			getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			
			/*
			 * 处理http的返回结果
			 */
			if (status == HttpStatus.SC_OK) {
				tmpStr = getMethod.getResponseBodyAsString();
				
	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");
	            
	            if (tmpStr.indexOf("access_token") != -1 && tmpStr.indexOf("expires_in") != -1){//成功
					sucessJson = JSON.parseObject(tmpStr, GetCredentialJson.class);
					if (sucessJson != null){
						this.access_token = sucessJson.getAccess_token();
						this.expires_in = sucessJson.getExpires_in();

						return true;
					}
				}else{//失败
					errorJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
					if (errorJson != null){
						this.errcode = errorJson.getErrcode();
						this.errmsg = errorJson.getErrmsg();
					}
				}
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
			
			//程序到此，肯定是失败了
			return false;
        } catch (Exception e) {
        	throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【获取凭证失败】失败原因：").append(e.getMessage()).toString());
        } finally {
        	tmpBuffer = null;
        	if (getMethod != null)
        		getMethod.releaseConnection();
        	getMethod = null;
        	tmpStr = null;
        	sucessJson = null;
        	errorJson = null;
        }
    }
    
}