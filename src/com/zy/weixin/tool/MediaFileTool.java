package com.zy.weixin.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.protocol.Protocol;

import com.alibaba.fastjson.JSON;
import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.UploadFileJson;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeixinConfig;

/**
 * 微信多媒体文件上传与下载的工具类
 * @author zy20022630
 */
public class MediaFileTool {
	
	private HttpClient client;
	
    /**
     * (私有的)无参构造器
     */
	private MediaFileTool() {
		super();
		
		Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
		client = new HttpClient();
	}
	
    //定义一个静态实例
	private static MediaFileTool instance = new MediaFileTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static MediaFileTool getInstance() {
		return instance;
	}
	
	/**
	 * 上传多媒体文件
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param type --int*-- 多媒体文件类型：1-图片(image)、2-语音(voice)、3-视频(video)、4-缩略图(thumb)
	 * @param filePath --String*-- 源文件的绝对路径(绝对目录+文件名+.+后缀名)
	 * @return null 或 UploadFileJson对象 或 CommonReturnMsgJson对象
	 * @throws WeiXinException 
	 */
	public Object uploadFile(final String accessToken, int type, String filePath) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法上传多媒体文件");

		if (type < 1 || type > 4 || ToolUtil.isStrEmpty(filePath))
			throw new WeiXinException("没有设置参数，无法上传多媒体文件");
		
		/*
		 * 定义几个参数
		 */
		File file = null;
    	FilePart filePart = null;
    	Part[] parts = null;
    	MultipartRequestEntity multipartRequestEntity = null;
    	PostMethod postMethod = null;

		StringBuffer tmpBuffer = new StringBuffer();
    	int status = 0;
    	String tmpStr = null;
    	
    	CommonReturnMsgJson errorMsgJson = null;
    	UploadFileJson uploadFileJson = null;

    	try {
    		//效验文件信息
    		file = new File(filePath);
    		if (file == null || !file.exists() || !file.isFile() || !file.canRead())
    			throw new WeiXinException("传入的源文件信息有误，无法上传多媒体文件");

    		tmpStr = WeixinConfig.getConfig("weixin.mediaFile.upload.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		if (type == 1)		//1-图片(image)
    			tmpStr = tmpStr.replaceAll("TYPE", "image");
    		else if (type == 2)	//2-语音(voice)
    			tmpStr = tmpStr.replaceAll("TYPE", "voice");
    		else if (type == 3)	//3-视频(video)
    			tmpStr = tmpStr.replaceAll("TYPE", "video");
    		else if (type == 4)	//4-缩略图(thumb)
    			tmpStr = tmpStr.replaceAll("TYPE", "thumb");
    		
    		postMethod = new PostMethod(tmpStr);
    		filePart = new FilePart("media", file);
    		parts = new Part[]{filePart};
    		multipartRequestEntity = new MultipartRequestEntity(parts, postMethod.getParams());
    		postMethod.setRequestEntity(multipartRequestEntity);
			postMethod.getParams().setContentCharset(WeixinConfig.getConfig("weixin.url.encoding"));
			status = client.executeMethod(postMethod);
		
			if (status == HttpStatus.SC_OK) {
				tmpStr = postMethod.getResponseBodyAsString();

	            if (ToolUtil.isStrEmpty(tmpStr))
	            	throw new WeiXinException("响应中的返回值为空");

	            if (tmpStr.indexOf("errcode") != -1 && tmpStr.indexOf("errmsg") != -1){//表示失败
	            	errorMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
	            	return errorMsgJson;
	            }else{
	            	uploadFileJson = JSON.parseObject(tmpStr, UploadFileJson.class);
	            	return uploadFileJson;
	            }
			} else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用PostMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【上传多媒体文件失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			file = null;
			filePart = null;
			parts = null;
	    	multipartRequestEntity = null;
	    	if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
			tmpBuffer = null;
	    	tmpStr = null;
		}
	}
	
	/**
	 * 下载多媒体文件
	 * @param accessToken --String*-- 公众号的全局唯一票据(access_token)
	 * @param mediaId --String*-- 多媒体文件ID
	 * @param filePath --String*-- 存放目标文件的绝对路径(绝对目录+文件名+.+后缀名)
	 * @return true表示下载成功
	 * @throws WeiXinException 
	 */
	public boolean downloadFile(final String accessToken, String mediaId, String filePath) throws WeiXinException{
		if (ToolUtil.isStrEmpty(accessToken))
			throw new WeiXinException("没有设置access_token，无法下载多媒体文件");

		if (ToolUtil.isStrEmpty(mediaId) || ToolUtil.isStrEmpty(filePath))
			throw new WeiXinException("没有设置参数，无法下载多媒体文件");


		StringBuffer tmpBuffer = new StringBuffer();
    	int status = 0;
    	String tmpStr = null;
    	GetMethod getMethod = null;
    	CommonReturnMsgJson rtnMsgJson = null;
    	InputStream inputStream = null;
    	File file = null;
    	FileOutputStream fileOutputStream = null;
    	byte[] b = null;
    	int j = 0;

    	try {
    		//效验
    		file = new File(filePath);
    		if (file == null || file.isDirectory())
    			throw new WeiXinException("传入的存放目标文件的绝对路径有误，无法下载多媒体文件");

    		if (file.exists())
    			file.delete();
    		
    		tmpStr = WeixinConfig.getConfig("weixin.mediaFile.download.address");
    		tmpStr = tmpStr.replaceAll("ACCESS_TOKEN", accessToken);
    		tmpStr = tmpStr.replaceAll("MEDIA_ID", mediaId);
    		
    		getMethod = new GetMethod(tmpStr);
			status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				if (getMethod.getResponseHeader("Content-disposition") == null){//没有下载成功
					tmpStr = getMethod.getResponseBodyAsString();
					rtnMsgJson = JSON.parseObject(tmpStr, CommonReturnMsgJson.class);
					if (rtnMsgJson != null){
						throw new WeiXinException(rtnMsgJson.getErrmsg());
					}
					return false;
				}else{//表示下载成功
					inputStream = getMethod.getResponseBodyAsStream();
					fileOutputStream = new FileOutputStream(file);
					b = new byte[1024];
					j = 0;
					while ((j = inputStream.read(b)) != -1) {
						fileOutputStream.write(b, 0, j);
					}
					fileOutputStream.flush();
					return true;
				}
			}else
				throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("调用GetMethod时返回的HttpStatus为").substring(status).toString());
    	} catch (Exception e) {
    		throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【下载多媒体文件失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			//清空
			tmpBuffer = null;
	    	tmpStr = null;
	    	if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
	    	rtnMsgJson = null;
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
				} finally{
					inputStream = null;
				}
			}
			file = null;
			if (fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
				} finally{
					fileOutputStream = null;
				}
			}
			b = null;
		}
	}
	
}