package com.zy.weixin.tool;

import java.security.MessageDigest;
import java.util.Arrays;

import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.util.ToolUtil;

/**
 * 验证消息真实性的工具类<br/>
 * 在开发者首次提交验证申请时，微信服务器将发送GET请求到填写的URL上，并且带上四个参数(signature、timestamp、nonce、echostr)，
 * 开发者通过对签名(即signature)的效验，来判断此条消息的真实性<br/>
 * 加密/校验流程如下：<br/>
 * 1. 将token、timestamp、nonce三个参数进行字典序排序<br/>
 * 2. 将三个参数字符串拼接成一个字符串进行sha1加密<br/>
 * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信<br/>
 * 开发者通过检验signature对请求进行校验。若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。
 * @author zy20022630
 */
public class CheckSignatureTool {
	
	private static final String ENCODING_SHA = "SHA-1";
	
	private static final String ERR_RTN_MSG = "error";
	
	/**
	 * 验证消息URL真实性
	 * @param token --String*-- 开发者填写的token(由开发者自行定义)
	 * @param signature --String*-- 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。(通过request.getParameter("signature")获取)
	 * @param timestamp --String*-- 时间戳(通过request.getParameter("timestamp")获取)
	 * @param nonce --String*-- 随机数(通过request.getParameter("nonce")获取)
	 * @param echostr --String*-- 随机字符串(通过request.getParameter("echostr")获取)
	 * @return 若验证成功则返回echostr；若效验失败则返回error
	 * @throws WeiXinException 
	 */
	public static String check(String token, String signature, String timestamp, String nonce, String echostr) throws WeiXinException{
		if (ToolUtil.isStrEmpty(token)
				|| ToolUtil.isStrEmpty(signature)
				|| ToolUtil.isStrEmpty(timestamp)
				|| ToolUtil.isStrEmpty(nonce)
				|| ToolUtil.isStrEmpty(echostr))
			throw new WeiXinException("没有设置参数，无法验证消息URL真实性");
		
		String[] array = null;
		StringBuffer tmpBuffer = null;
		MessageDigest messageDigest = null;
        byte[] digest = null;
        String tmpStr = null;
		
		try {
			tmpBuffer = new StringBuffer();
			
			array = new String[]{token, timestamp, nonce};
			Arrays.sort(array);

			for (int i = 0; i < array.length; i++) {
				tmpBuffer.append(array[i]);
			}
			
			messageDigest = MessageDigest.getInstance(ENCODING_SHA);
			digest = messageDigest.digest(tmpBuffer.toString().getBytes());
			tmpStr = ToolUtil.byteToStr(digest);
			
			if (signature.toUpperCase().equals(tmpStr))
				return echostr;
		} catch (Exception e) {
			throw new WeiXinException(tmpBuffer.delete(0, tmpBuffer.length()).append("【验证消息URL真实性失败】失败原因：").append(e.getMessage()).toString());
		} finally {
			array = null;
			tmpBuffer = null;
			messageDigest = null;
	        digest = null;
	        tmpStr = null;
		}
		
		return ERR_RTN_MSG;
	}
}