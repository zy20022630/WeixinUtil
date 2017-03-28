package com.zy.weixin.tool;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedMap;

import com.zy.weixin.util.ToolUtil;

/**
 * 微信JS-SDK的工具类
 * @author zy20022630
 */
public class JSSDKTool {
	
	private static final String ENCODING_SHA = "SHA-1";
	
	/**
	 * 根据参数，获取JS-SDK的签名
	 * @param dataMap --Map*-- 参数
	 * @return JS-SDK的签名
	 * @throws Exception 
	 */
	public static String getSignForJSSDK(SortedMap<String, String> dataMap) throws Exception{
		String sign = null;
		
		//定义临时变量
		Iterator<String> iterator = null;
		String key = null;
		String value = null;
		StringBuffer tmpBuffer = null;
		MessageDigest messageDigest = null;
        byte[] digest = null;
		try {
			if (dataMap == null || dataMap.isEmpty())
				return sign;
			
			tmpBuffer = new StringBuffer();
			iterator = dataMap.keySet().iterator();
			while (iterator.hasNext()){
				key = iterator.next();
				value = dataMap.get(key);
				tmpBuffer.append('&').append(key).append('=').append(value);
			}
			tmpBuffer.deleteCharAt(0);

			messageDigest = MessageDigest.getInstance(ENCODING_SHA);
			digest = messageDigest.digest(tmpBuffer.toString().getBytes());
			sign = ToolUtil.byteToStr(digest);
			sign = sign.toLowerCase();
			
			return sign;
		} catch (Exception e){
			throw new Exception("获取JS-SDK的签名失败");
		} finally {
			//清空
			iterator = null;
			key = null;
			value = null;
			tmpBuffer = null;
			messageDigest = null;
	        digest = null;
		}
	}

	/**
	 * 获取32位随机字符串
	 * @return 随机字符串
	 */
	public static String getNoncestr(){
		Random random = new Random();
		StringBuffer tmpBuffer = new StringBuffer();
		for (int i = 0;i < 32;i++){
			tmpBuffer.append(random.nextInt(10));
		}
		String returnStr = tmpBuffer.toString();
		
		random = null;
		tmpBuffer = null;
		
		return returnStr;
	}
	
	/**
	 * 获取指定位数的随机字符串
	 * @param bit --int-- 指定位数
	 * @return 随机字符串
	 */
	public static String getNoncestr(int bit){
		Random random = new Random();
		StringBuffer tmpBuffer = new StringBuffer();
		for (int i = 0;i < bit;i++){
			tmpBuffer.append(random.nextInt(10));
		}
		String returnStr = tmpBuffer.toString();
		
		random = null;
		tmpBuffer = null;
		
		return returnStr;
	}
	
	/**
	 * 获取Unix时间戳（从1970-01-01 00:00:00开始的秒数）
	 * @return Unix时间戳
	 */
	public static long getTimestamp(){
		return (System.currentTimeMillis() / 1000);
	}
	
}