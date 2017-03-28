package com.zy.weixin.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;

public class WeinxinUtil {
	
	/**
	 * 判断字符串是否为空（null、""、" "）
	 * @param s 指定字符串
	 * @return true表示为空
	 */
	public static boolean isStrEmpty(String s){
		return (s == null || s.trim().length() == 0);
	}
	
	/**
	 * 将字符串集合合成一个字符串
	 * @param contentList --List--以字符串为元素的集合
	 * @return 新的字符串
	 */
	public static String getStringFromList(List<String> contentList){
		if (contentList == null || contentList.isEmpty())
			return null;
		
		String content = null;
		StringBuilder tmpBuffer = new StringBuilder();
		for (String line : contentList)
			tmpBuffer.append(line);
		content = tmpBuffer.toString();
		
		tmpBuffer = null;
		
		return content;
	}
	
    /** 
     * （把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串，然后MD5加密后大写）生成签名
     * @param params --SortedMap*-- 需要排序并参与字符拼接的参数组
     * @param partnerKey --String*-- 商家模块密钥
     * @param characterEncoding  --String-- 编码字符集（默认UTF-8）
     * @return 拼接后字符串进行MD5加密得到签名
     */
	public static String getSignBycreateLinkString(SortedMap<String, String> params, String partnerKey, String characterEncoding) {
    	String prestr = "";
    	
    	if (params == null || params.isEmpty())
    		return prestr;
    	
    	Iterator<String> iterator = params.keySet().iterator();
    	String key = null;
    	String value = null;
    	StringBuffer tmpBuffer = new StringBuffer();
    	while (iterator.hasNext()){
    		key = iterator.next();
    		value = params.get(key);
    		if (!"sign".equals(key) && null != value && !"".equals(value)) {
    			tmpBuffer.append(key).append("=").append(value).append('&');
    		}
    	}
    	tmpBuffer.append("key=").append(partnerKey);
    	
    	prestr = MD5Util.MD5Encode(tmpBuffer.toString(), isStrEmpty(characterEncoding) ? "UTF-8" : characterEncoding).toUpperCase();
        
        //清空
    	iterator = null;
        key = null;
        value = null;
        tmpBuffer = null;

        return prestr;
    }
	
	/**
	 * 把Map中所有元素按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String getLinkStringFromMap(Map<String, String> params) {
		String prestr = "";
    	
    	if (params == null || params.isEmpty())
    		return prestr;
    	
    	StringBuffer tmpBuffer = new StringBuffer();
    	Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
    	Entry<String, String> entry = null;
        String key = null;
        String value = null;
        while (iterator.hasNext()){
        	entry = iterator.next();
        	key = entry.getKey();
        	value = entry.getValue();
        	if (value == null)
        		continue;
        	tmpBuffer.append("&").append(key).append("=").append(value);
        }
        
        if (tmpBuffer.length() > 0)
        	prestr = tmpBuffer.substring(1);
    	
        //清空
        tmpBuffer = null;
        iterator = null;
        entry = null;
        key = null;
        value = null;
        
        return prestr;
	}
	
    /** 
     * （把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串，然后MD5加密后大写）生成签名
     * @param params --SortedMap*-- 需要排序并参与字符拼接的参数组
     * @param partnerKey --String*-- 商家模块密钥
     * @param characterEncoding  --String-- 编码字符集（默认UTF-8）
     * @return 拼接后字符串进行MD5加密得到签名
     */
	public static String getSignBycreateLinkStringFromMap(SortedMap<String, Object> params, String partnerKey, String characterEncoding) {
    	String prestr = "";
    	
    	if (params == null || params.isEmpty())
    		return prestr;
    	
    	StringBuilder tmpBuilder = new StringBuilder();
    	Iterator<String> iterator = params.keySet().iterator();
    	String key = null;
    	Object object = null;
    	String value = null;
    	
    	while (iterator.hasNext()){
    		key = iterator.next();
    		if (key == null || "".equals(key))
    			continue;
    		
    		object = params.get(key);
    		if (object == null)
    			continue;
    		
    		value = object.toString();
    		if (value == null || "".equals(value))
    			continue;
    		
    		if ("sign".equals(key))
    			continue;
    		
    		tmpBuilder.append(key).append("=").append(value).append("&");
    	}
    	tmpBuilder.append("key=").append(partnerKey);
    	
    	prestr = MD5Util.MD5Encode(tmpBuilder.toString(), isStrEmpty(characterEncoding) ? "UTF-8" : characterEncoding).toUpperCase();
        
        //清空
    	tmpBuilder = null;
    	iterator = null;
        key = null;
        object = null;
        value = null;

        return prestr;
    }
	
	/**
	 * 生成请求XML格式的字符串
	 * @param params --SortedMap*-- 发起请求的参数组
	 * @return 请求XML格式的字符串
	 */
	public static String getXmlContentBycreateLinkStringFromMap(SortedMap<String, Object> params){
		String xmlContent = "";
    	
    	if (params == null || params.isEmpty())
    		return xmlContent;
    	
    	StringBuilder tmpBuilder = new StringBuilder();
    	Iterator<String> iterator = params.keySet().iterator();
    	String key = null;
    	Object object = null;
    	String value = null;
    	
    	tmpBuilder.append("<xml>");
    	while (iterator.hasNext()){
    		key = iterator.next();
    		if (key == null || "".equals(key))
    			continue;
    		
    		object = params.get(key);
    		if (object == null)
    			continue;
    		
    		value = object.toString();
    		if (value == null || "".equals(value))
    			continue;
    		
    		if (object instanceof String)
    			tmpBuilder.append("<").append(key).append("><![CDATA[").append(value).append("]]></").append(key).append(">");
    		else 
    			tmpBuilder.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
    	}
    	tmpBuilder.append("</xml>");
    	xmlContent = tmpBuilder.toString();
    	
    	//清空
    	tmpBuilder = null;
    	iterator = null;
        key = null;
        object = null;
        value = null;
		
		return xmlContent;
	}
	
	/**
	 * 获取32位随机字符串
	 * @return 随机字符串
	 */
	public static String getNoncestr(){
		return getNoncestr(32);
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