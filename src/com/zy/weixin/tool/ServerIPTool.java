package com.zy.weixin.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信服务器的IP地址的获取工具类
 * @author zy20022630
 */
public class ServerIPTool {

	private HttpClient client;
	
    /**
     * (私有的)无参构造器
     */
	private ServerIPTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static ServerIPTool instance = new ServerIPTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static ServerIPTool getInstance() {
		return instance;
	}
	
	/**
	 * 获取微信服务器的IP地址列表
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @return null 或 以IP地址为元素的List集合
	 * @throws WeiXinException
	 */
	public List<String> gainServerIpList(final String accessToken) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法获取微信服务器IP地址");
		
		List<String> dataList = null;
		
		StringBuffer tmpBuffer = new StringBuffer();
    	int status = 0;
    	String tmpStr = null;
    	GetMethod getMethod = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        
		try {
			tmpStr = WeixinConfig.getConfig("weixin.server.iplist.address");
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
	            else {
		            jsonObject = JSONObject.parseObject(tmpStr);
		            if (jsonObject != null)
		            	jsonArray = jsonObject.getJSONArray("ip_list");
		            if (jsonArray != null){
		            	int size = jsonArray.size();
		            	dataList = new ArrayList<String>();
			            for (int i = 0;i < size;i++){
			            	dataList.add(jsonArray.get(i).toString());
			            }
		            }
	            }
	            
	            if (rtnMsgJson != null)
	            	throw new WeiXinException(rtnMsgJson.getErrmsg());

	            return dataList;
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【上传多媒体文件失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
			tmpStr = null;
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
	    	jsonObject = null;
	        jsonArray = null;
		}
	}
	
}