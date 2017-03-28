package com.zy.weixin.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 * （无证书的）HTTPS的帮助类
 * @author zy20022630
 */
public class HttpsHelper {

	private HttpClient client;
	
	/**
	 * 响应超时时间：5000毫秒
	 */
	private static final int TIME_OUT = 5000;
	
	/**
	 * 默认的编码格式字符集：UTF-8
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";
	
    /**
     * (私有的)无参构造器
     */
    private HttpsHelper(){
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
    }
    
    //定义一个静态实例
	private static HttpsHelper instance = new HttpsHelper();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static HttpsHelper getInstance() {
		return instance;
	}
	
	private boolean isStrEmpty(String s){
		return (s == null || s.trim().length() == 0);
	}
	
	/**
	 * 模拟HTTPs的GET方法提交，返回响应信息字符串
	 * @param strURL --String*-- URL地址
	 * @param charset --String-- 编码字符集
	 * @return 响应信息字符串
	 * @throws Exception
	 */
	public String executeGetMethod(String strURL, String charset) throws Exception {
		String responseContent = null;
		if (isStrEmpty(strURL))
			return responseContent;
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
    	GetMethod getMethod = null;
    	int status = 0;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	String tmpStr = null;
    	
    	try {
    		getMethod = new GetMethod(strURL);
    		getMethod.getParams().setSoTimeout(TIME_OUT);
    		status = client.executeMethod(getMethod);
    		
    		if (status == HttpStatus.SC_OK) {
    			inputStreamReader = new InputStreamReader(getMethod.getResponseBodyAsStream(),(isStrEmpty(charset) ? DEFAULT_ENCODING : charset));
    			reader = new BufferedReader(inputStreamReader);
				tmpBuffer.delete(0, tmpBuffer.length());
	            while ((tmpStr = reader.readLine()) != null) {
	                tmpBuffer.append(tmpStr);
	            }
	            responseContent = tmpBuffer.toString();
    			
	            return responseContent;
    		} else 
    			throw new Exception(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		//清空
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
        	tmpStr = null;
    	}
	}
	
	/**
	 * 模拟HTTPs的POST方法提交，返回响应信息字符串
	 * @param strURL --String*-- URL地址
	 * @param postData --String*-- 要提交的数据字符串
	 * @param charset --String-- 编码字符集
	 * @return 响应信息字符串
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public String executePostMethod(String strURL, String postData, String charset) throws Exception {
		String responseContent = null;
		if (isStrEmpty(strURL) || isStrEmpty(postData))
			return responseContent;
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		PostMethod postMethod = null;
		int status = 0;
		InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	String tmpStr = null;
    	
		try {
			postMethod = new PostMethod(strURL);
			postMethod.setRequestBody(postData);
			postMethod.getParams().setSoTimeout(TIME_OUT);
			postMethod.getParams().setContentCharset(isStrEmpty(charset) ? DEFAULT_ENCODING : charset);
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				inputStreamReader = new InputStreamReader(postMethod.getResponseBodyAsStream(),(isStrEmpty(charset) ? DEFAULT_ENCODING : charset));
				reader = new BufferedReader(inputStreamReader);
				tmpBuffer.delete(0, tmpBuffer.length());
	            while ((tmpStr = reader.readLine()) != null) {
	                tmpBuffer.append(tmpStr);
	            }
	            responseContent = tmpBuffer.toString();
				
				
	            return responseContent;
			} else
				throw new Exception(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		//清空
    		tmpBuffer = null;
    		if (postMethod != null)
    			postMethod.releaseConnection();
    		postMethod = null;
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
        	tmpStr = null;
    	}
	}
	
	/**
	 * 模拟HTTPs的POST方法提交，返回响应信息字符串（特殊方法）
	 * @param strURL --String*-- URL地址
	 * @param postData --String*-- 要提交的数据字符串
	 * @param charset --String-- 编码字符集
	 * @return 响应信息字符串
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public List<String> specialExecutePostMethod(String strURL, String postData, String charset) throws Exception {
		List<String> responseContentList = null;
		if (isStrEmpty(strURL) || isStrEmpty(postData))
			return null;
		
		//定义临时变量
		PostMethod postMethod = null;
		int status = 0;
		InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
    	String tmpStr = null;
    	
		try {
			postMethod = new PostMethod(strURL);
			postMethod.setRequestBody(postData);
			postMethod.getParams().setSoTimeout(TIME_OUT);
			postMethod.getParams().setContentCharset(isStrEmpty(charset) ? DEFAULT_ENCODING : charset);
			status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				inputStreamReader = new InputStreamReader(postMethod.getResponseBodyAsStream(),(isStrEmpty(charset) ? DEFAULT_ENCODING : charset));
				reader = new BufferedReader(inputStreamReader);
				responseContentList = new ArrayList<String>();
	            while ((tmpStr = reader.readLine()) != null) {
	            	responseContentList.add(tmpStr);
	            }
	            return responseContentList;
			} else
				throw new Exception(new StringBuffer().append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		//清空
    		if (postMethod != null)
    			postMethod.releaseConnection();
    		postMethod = null;
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
        	tmpStr = null;
    	}
	}
	
	/**
	 * 从ServletInputStream中获取请求内容
	 * @param InputStreamFromHttpServletRequest --InputStream*-- 从客户请求读取到的二进制数据输入流
	 * @param charset --String-- 编码字符集
	 * @return null 或 请求内容
	 * @throws Exception
	 */
	public String getContentFromRequest(InputStream InputStreamFromHttpServletRequest, String charset) throws Exception {
		String requestContent = null;
		if (InputStreamFromHttpServletRequest == null)
			return requestContent;
		
		//定义临时变量
		StringBuffer tmpBuffer = new StringBuffer();
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		String tmpStr = null;
		try {
			inputStreamReader = new InputStreamReader(InputStreamFromHttpServletRequest, (isStrEmpty(charset) ? DEFAULT_ENCODING : charset));
			reader = new BufferedReader(inputStreamReader);
            while ((tmpStr = reader.readLine()) != null) {
                tmpBuffer.append(tmpStr);
            }
            requestContent = tmpBuffer.toString();
			
            return requestContent;
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		//清空
    		tmpBuffer = null;
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
        	tmpStr = null;
    	}
	}
	
}