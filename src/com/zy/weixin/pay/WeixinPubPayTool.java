package com.zy.weixin.pay;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import com.alibaba.fastjson.JSONObject;
import com.zy.weixin.common.HttpsHelper;
import com.zy.weixin.common.SpecialHttpsHelper;
import com.zy.weixin.util.ToolUtil;
import com.zy.weixin.util.WeinxinUtil;
import com.zy.weixin.util.WeixinConfig;
import com.zy.weixin.util.XMLUtil;

/**
 * 微信公众号的支付工具类
 * @author zy20022630
 */
public class WeixinPubPayTool {

	//微信公众号的第三方用户唯一凭证appid
	private String appId = null;
	
	//微信支付的商户号
	private String partnerId = null;
	
	//微信支付的商户密钥
	private String partnerKey = null;
	
	//CA证书（rootca.pem）文件在磁盘上的绝对路径
	private String caCert = null;
	
	//商户证书（[partnerid].p12）文件在磁盘上的绝对路径
	private String partnerCert = null;
	
	//商户证书密码
	private String partnerCertPasswd = null;
	
    /**
     * (私有的)无参构造器
     */
	private WeixinPubPayTool() {
		super();
	}
	
    //定义一个静态实例
	private static WeixinPubPayTool instance = new WeixinPubPayTool();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static WeixinPubPayTool getInstance() {
		return instance;
	}
	
	/**
	 * 设置商家的相关基本信息
	 * @param appId --String*-- 微信公众号的第三方用户唯一凭证appid
	 * @param partnerId --String*-- 微信支付的商户号
	 * @param partnerKey --String*-- 微信支付的商户密钥
	 * @throws Exception
	 */
	public void setPartnerInfo(String appId, String partnerId, String partnerKey) throws Exception {
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("非空参数存在空值");
		
		this.appId = appId;
		this.partnerId = partnerId;
		this.partnerKey = partnerKey;
	}
	
	/**
	 * 设置商家的证书相关信息
	 * @param caCert --String*-- CA证书（rootca.pem）文件在磁盘上的绝对路径
	 * @param partnerCert --String*-- 商户证书（[partnerid].p12）文件在磁盘上的绝对路径
	 * @param partnerCertPasswd --String*-- 商户证书密码
	 * @throws Exception
	 */
	public void setPartnerCertInfo(String caCert, String partnerCert, String partnerCertPasswd) throws Exception {
		if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
			throw new Exception("非空参数存在空值");
		
		this.caCert = caCert;
		this.partnerCert = partnerCert;
		this.partnerCertPasswd = partnerCertPasswd;
	}

	/**
	 * 【统一下单】根据参数，拼接微信公众号JSAPI支付接口的请求参数Map
	 * @param clientIP --String-- 网页支付提交用户端ip（默认值192.168.1.1）
	 * @param notifyUrl --String*-- 异步接收微信支付结果通知的回调地址（通知url必须为外网可访问的url，不能携带参数）
	 * @param openid --String*-- 微信用户openid
	 * @param orderUID --String*-- 订单唯一凭证编号（系统生成）
	 * @param subject --String*-- 订单描述
	 * @param detail --String-- 订单详情
	 * @param totalFee --String*-- 订单金额（单位：元）
	 * @return null 或 请求参数的JSON字符串（KEY有appId、timeStamp、nonceStr、package、signType、paySign）
	 * @throws Exception
	 */
	public String getWeixinPubPayRequestParamMapForJSAPI(String clientIP, String notifyUrl, String openid, String orderUID, String subject, String detail, String totalFee) throws Exception{
		String resultJson = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		int totalfee = 0;
		SortedMap<String, String> tmpMap1 = null;
		String sign = null;
		StringBuilder tmpBuilder = null;
		String postData = null;
		String responseContent = null;
		SortedMap<String, String> tmpMap2 = null;
		String prepayId = null;
		String strPackage = null;
		String timeStamp = null;
		String signType = null;
		JSONObject jsonObject = null;
		
		try {
			//判断参数是否为空
			if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
				throw new Exception("商家相关信息存在空值");
			if (ToolUtil.isStrEmpty(notifyUrl))
				throw new Exception("非空参数存在空值");
			if (ToolUtil.isStrEmpty(openid) || ToolUtil.isStrEmpty(orderUID) || ToolUtil.isStrEmpty(subject) || ToolUtil.isStrEmpty(totalFee))
				throw new Exception("非空参数存在空值");
			
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			totalfee = (int)(Double.parseDouble(totalFee) * 100);//获取订单金额（单位：分）
			
			//创建数据集合以生成签名
			tmpMap1 = new TreeMap<String, String>();
			tmpMap1.put("appid", appId);//公众账号ID
			tmpMap1.put("mch_id", partnerId);//商户号
			//tmpMap1.put("device_info", null);//设备号
			tmpMap1.put("nonce_str", nonceStr);//随机字符串 	nonce_str 	是 	String(32)
			tmpMap1.put("sign", sign);	//签名
			tmpMap1.put("body", ToolUtil.getNewStringByIntercept(subject, 40));//商品描述 	body 	是 	String(128)
			if (!ToolUtil.isStrEmpty(detail))
				tmpMap1.put("detail", ToolUtil.getNewStringByIntercept(detail, 2700));//商品详情 	detail 	否 	String(8192)
			//tmpMap1.put("attach", null);//附加数据
			tmpMap1.put("out_trade_no", orderUID);//商户订单号 	out_trade_no 	是 	String(32)
			//tmpMap1.put("fee_type", "CNY");//货币类型
			tmpMap1.put("total_fee", String.valueOf(totalfee));//总金额
			tmpMap1.put("spbill_create_ip", !ToolUtil.isStrEmpty(clientIP) ? clientIP : "192.168.1.1");//终端IP
			//tmpMap1.put("time_start", null);//交易起始时间
			//tmpMap1.put("time_expire", null);//交易结束时间
			//tmpMap1.put("goods_tag", null);//商品标记
			tmpMap1.put("notify_url", notifyUrl);//通知地址 	notify_url 	是 	String(256) 	
			tmpMap1.put("trade_type", "JSAPI");//交易类型
			//tmpMap1.put("product_id", null);//商品ID
			tmpMap1.put("openid", openid);//用户标识
			sign = WeinxinUtil.getSignBycreateLinkString(tmpMap1, partnerKey, encoding);
			
			//创建POST提交的数据XML字符串
			tmpBuilder = new StringBuilder();
			tmpBuilder.append("<xml>");
			tmpBuilder.append("<appid><![CDATA[").append(appId).append("]]></appid>");//公众账号ID
			tmpBuilder.append("<mch_id><![CDATA[").append(partnerId).append("]]></mch_id>");//商户号
			//tmpBuilder.append("<device_info><![CDATA[]]></device_info>");//设备号
			tmpBuilder.append("<nonce_str><![CDATA[").append(nonceStr).append("]]></nonce_str>");//随机字符串
			tmpBuilder.append("<sign><![CDATA[").append(sign).append("]]></sign>");//签名
			tmpBuilder.append("<body><![CDATA[").append(ToolUtil.getNewStringByIntercept(subject, 40)).append("]]></body>");//商品描述 	body 	是 	String(128)
			if (!ToolUtil.isStrEmpty(detail))
				tmpBuilder.append("<detail><![CDATA[").append(ToolUtil.getNewStringByIntercept(detail, 2700)).append("]]></detail>");//商品详情 	detail 	否 	String(8192)
			//tmpBuilder.append("<attach><![CDATA[]]></attach>");//附加数据
			tmpBuilder.append("<out_trade_no><![CDATA[").append(orderUID).append("]]></out_trade_no>");//商户订单号
			//tmpBuilder.append("<fee_type><![CDATA[CNY]]></fee_type>");//货币类型
			tmpBuilder.append("<total_fee><![CDATA[").append(totalfee).append("]]></total_fee>");//总金额
			tmpBuilder.append("<spbill_create_ip><![CDATA[").append(!ToolUtil.isStrEmpty(clientIP) ? clientIP : "192.168.1.1").append("]]></spbill_create_ip>");//终端IP
			//tmpBuilder.append("<time_start><![CDATA[]]></time_start>");//交易起始时间
			//tmpBuilder.append("<time_expire><![CDATA[]]></time_expire>");//交易结束时间
			//tmpBuilder.append("<goods_tag><![CDATA[]]></goods_tag>");//商品标记
			tmpBuilder.append("<notify_url><![CDATA[").append(notifyUrl).append("]]></notify_url>");//通知地址
			tmpBuilder.append("<trade_type><![CDATA[JSAPI]]></trade_type>");//交易类型
			//tmpBuilder.append("<product_id><![CDATA[]]></product_id>");//商品ID
			tmpBuilder.append("<openid><![CDATA[").append(openid).append("]]></openid>");//用户标识
			tmpBuilder.append("</xml>");
			postData = tmpBuilder.toString();
			
			//模拟HTTPs的POST方法提交以获取响应信息字符串
			responseContent = HttpsHelper.getInstance().executePostMethod(WeixinConfig.getConfig("weixinpub.pay.urlForUnifiedorder"), postData, encoding);
			
			//若响应内容有值则进行XML解析
			if (!WeinxinUtil.isStrEmpty(responseContent))
				tmpMap2 = XMLUtil.doXMLParse(responseContent);
			if (tmpMap2 == null || tmpMap2.isEmpty())
				return resultJson;
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(tmpMap2.get("return_code")))
				throw new Exception(tmpBuilder.delete(0, tmpBuilder.length()).append("【统一下单】调用接口失败：").append(tmpMap2.get("return_msg")).toString());
			
			//生成响应内容的签名并进行效验
			sign = WeinxinUtil.getSignBycreateLinkString(tmpMap2, partnerKey, encoding);
			if (!sign.equals(tmpMap2.get("sign")))
				throw new Exception("【统一下单】响应信息中的签名验证失败");

			//验证业务是否执行成功
			if (!"SUCCESS".equals(tmpMap2.get("result_code")))
				throw new Exception(tmpBuilder.delete(0, tmpBuilder.length()).append("【统一下单】业务执行失败，错误代码为").append(tmpMap2.get("err_code")).append("，错误描述：").append(tmpMap2.get("err_code_des")).toString());

			//至此，则表示业务执行成功
			prepayId = tmpMap2.get("prepay_id");
			if (WeinxinUtil.isStrEmpty(prepayId))
				throw new Exception(tmpBuilder.delete(0, tmpBuilder.length()).append("【统一下单】业务数据prepay_id抓取失败").toString());
			
			//获取时间戳
			timeStamp = String.valueOf(WeinxinUtil.getTimestamp());
			//获取随机字符串
			nonceStr = WeinxinUtil.getNoncestr();
			//获取订单详情扩展字符串
			strPackage = tmpBuilder.delete(0, tmpBuilder.length()).append("prepay_id=").append(prepayId).toString();
			//设置签名算法
			signType = "MD5";
			//获取签名
			sign = null;
			tmpMap1.clear();
			tmpMap1.put("appId", appId);//公众账号ID
			tmpMap1.put("timeStamp", timeStamp);//时间戳
			tmpMap1.put("nonceStr", nonceStr);//随机字符串
			tmpMap1.put("package", strPackage);//订单详情扩展字符串
			tmpMap1.put("signType", signType);//签名算法
			//tmpMap1.put("paySign", sign);//签名
			sign = WeinxinUtil.getSignBycreateLinkString(tmpMap1, partnerKey, encoding);
			
			//开始封装数据
			jsonObject = new JSONObject();
			jsonObject.put("appId", appId);//公众号id
			jsonObject.put("timeStamp", timeStamp);//时间戳
			jsonObject.put("nonceStr", nonceStr);//随机字符串
			jsonObject.put("package", strPackage);//订单详情扩展字符串
			jsonObject.put("signType", signType);//签名方式
			jsonObject.put("paySign", sign);//签名
			resultJson = jsonObject.toJSONString();
			
			//返回数据
			return resultJson;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (tmpMap1 != null)
				tmpMap1.clear();
			tmpMap1 = null;
			sign = null;
			tmpBuilder = null;
			postData = null;
			responseContent = null;
			if (tmpMap2 != null)
				tmpMap2.clear();
			tmpMap2 = null;
			prepayId = null;
			strPackage = null;
			timeStamp = null;
			signType = null;
			jsonObject = null;
		}
	}
	
	/**
	 * 【查询订单】
	 * @param transaction_id --String-- 微信订单号（二选一）
	 * @param out_trade_no --String-- 商户订单号（二选一）
	 * @return null 或 响应内容Map（业务执行成功）
	 * @throws Exception
	 */
	public SortedMap<String, String> queryOrder(String transaction_id, String out_trade_no) throws Exception {
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(transaction_id) && WeinxinUtil.isStrEmpty(out_trade_no))
			throw new Exception("非空参数存在空值");
		
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, String> paramMap = null;
		String sign = null;
		StringBuilder tmpBuilder = null;
		String requestContent = null;
		String responseContent = null;
		
		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//创建数据集合以生成签名
			paramMap = new TreeMap<String, String>();
			paramMap.put("appid", appId);//公众账号ID
			paramMap.put("mch_id", partnerId);//商户号
			if (!WeinxinUtil.isStrEmpty(transaction_id))
				paramMap.put("transaction_id", transaction_id);//微信订单号
			if (!WeinxinUtil.isStrEmpty(out_trade_no))
				paramMap.put("out_trade_no", out_trade_no);//商户订单号
			paramMap.put("nonce_str", nonceStr);//随机字符串
			paramMap.put("sign", "");//签名
			sign = WeinxinUtil.getSignBycreateLinkString(paramMap, partnerKey, encoding);
			
			//创建POST提交的数据XML字符串
			tmpBuilder = new StringBuilder();
			tmpBuilder.append("<xml>");
			tmpBuilder.append("<appid><![CDATA[").append(appId).append("]]></appid>");//公众账号ID
			tmpBuilder.append("<mch_id><![CDATA[").append(partnerId).append("]]></mch_id>");//商户号
			if (!WeinxinUtil.isStrEmpty(transaction_id))
				tmpBuilder.append("<transaction_id><![CDATA[").append(transaction_id).append("]]></transaction_id>");//微信订单号
			if (!WeinxinUtil.isStrEmpty(out_trade_no))
				tmpBuilder.append("<out_trade_no><![CDATA[").append(out_trade_no).append("]]></out_trade_no>");//商户订单号
			tmpBuilder.append("<nonce_str><![CDATA[").append(nonceStr).append("]]></nonce_str>");//随机字符串
			tmpBuilder.append("<sign><![CDATA[").append(sign).append("]]></sign>");//签名
			tmpBuilder.append("</xml>");
			requestContent = tmpBuilder.toString();
			
			//模拟HTTPs的POST方法提交以获取响应信息字符串
			responseContent = HttpsHelper.getInstance().executePostMethod(WeixinConfig.getConfig("weixinpub.pay.urlForOrderquery"), requestContent, encoding);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询订单】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//生成响应内容的签名并进行效验
			if (!checkResponseSign(dataMap)){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询订单】响应信息中的签名验证失败");
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询订单】业务执行失败，错误代码为").append(dataMap.get("err_code")).append("，错误描述：").append(dataMap.get("err_code_des"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//返回
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			tmpBuilder = null;
			requestContent = null;
			responseContent = null;
		}
	}
	
	/**
	 * 【关闭订单】
	 * @param out_trade_no --String*-- 商户订单号
	 * @return true表示操作成功
	 * @throws Exception
	 */
	public boolean closeOrder(String out_trade_no) throws Exception {
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(out_trade_no))
			throw new Exception("非空参数存在空值");
		
		boolean isOK = false;

		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, String> paramMap = null;
		String sign = null;
		StringBuilder tmpBuilder = null;
		String requestContent = null;
		String responseContent = null;
		SortedMap<String, String> dataMap = null;
		
		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//创建数据集合以生成签名
			paramMap = new TreeMap<String, String>();
			paramMap.put("appid", appId);//公众账号ID
			paramMap.put("mch_id", partnerId);//商户号
			paramMap.put("out_trade_no", out_trade_no);//商户订单号
			paramMap.put("nonce_str", nonceStr);//随机字符串
			paramMap.put("sign", "");//签名
			sign = WeinxinUtil.getSignBycreateLinkString(paramMap, partnerKey, encoding);
			
			//创建POST提交的数据XML字符串
			tmpBuilder = new StringBuilder();
			tmpBuilder.append("<xml>");
			tmpBuilder.append("<appid><![CDATA[").append(appId).append("]]></appid>");//公众账号ID
			tmpBuilder.append("<mch_id><![CDATA[").append(partnerId).append("]]></mch_id>");//商户号
			tmpBuilder.append("<out_trade_no><![CDATA[").append(out_trade_no).append("]]></out_trade_no>");//商户订单号
			tmpBuilder.append("<nonce_str><![CDATA[").append(nonceStr).append("]]></nonce_str>");//随机字符串
			tmpBuilder.append("<sign><![CDATA[").append(sign).append("]]></sign>");//签名
			tmpBuilder.append("</xml>");
			requestContent = tmpBuilder.toString();
			
			//模拟HTTPs的POST方法提交以获取响应信息字符串
			responseContent = HttpsHelper.getInstance().executePostMethod(WeixinConfig.getConfig("weixinpub.pay.urlForCloseorder"), requestContent, encoding);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【关闭订单】调用接口失败：").append(dataMap.get("return_msg"));
				throw new Exception(tmpBuilder.toString());
			}
			
			//生成响应内容的签名并进行效验
			if (!checkResponseSign(dataMap)){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【关闭订单】响应信息中的签名验证失败");
				throw new Exception(tmpBuilder.toString());
			}
			
			//判断是否业务执行成功
			isOK = "SUCCESS".equals(dataMap.get("result_code"));
			
			//返回
			return isOK;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			tmpBuilder = null;
			requestContent = null;
			responseContent = null;
			if (dataMap != null)
				dataMap.clear();
			dataMap = null;
		}
	}
	
	/**
	 * 【申请退款】获取申请退款的（XML格式的）请求内容（申请退款的第一部分）
	 * @param transaction_id --String-- 微信订单号（二选一）
	 * @param out_trade_no --String-- 商户订单号（二选一）
	 * @param out_refund_no --String*-- 商户退款单号
	 * @param total_fee --long*-- 总金额（单位：分）
	 * @param refund_fee --long*-- 退款金额（单位：分）
	 * @return null 或 （XML格式的）请求内容
	 * @throws Exception
	 */
	public String getRequestContentForRefund(String transaction_id, String out_trade_no, String out_refund_no, long total_fee, long refund_fee) throws Exception {
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if ((WeinxinUtil.isStrEmpty(transaction_id) && WeinxinUtil.isStrEmpty(out_trade_no)) || WeinxinUtil.isStrEmpty(out_refund_no))
			throw new Exception("非空参数存在空值");
		else if (total_fee <= 0 || refund_fee <= 0 || total_fee < refund_fee)
			throw new Exception("参数中金额取值范围有误");
		
		String requestContent = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, String> paramMap = null;
		String sign = null;
		StringBuilder tmpBuilder = null;

		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//创建数据集合以生成签名
			paramMap = new TreeMap<String, String>();
			paramMap.put("appid", appId);//公众账号ID
			paramMap.put("mch_id", partnerId);//商户号
			//paramMap.put("device_info", null);//设备号
			paramMap.put("nonce_str", nonceStr);//随机字符串
			paramMap.put("sign", "");//签名
			if (!WeinxinUtil.isStrEmpty(transaction_id))
				paramMap.put("transaction_id", transaction_id);//微信订单号
			if (!WeinxinUtil.isStrEmpty(out_trade_no))
				paramMap.put("out_trade_no", out_trade_no);//商户订单号
			paramMap.put("out_refund_no", out_refund_no);//商户退款单号
			paramMap.put("total_fee", String.valueOf(total_fee));//总金额
			paramMap.put("refund_fee", String.valueOf(refund_fee));//退款金额
			//paramMap.put("refund_fee_type", "CNY");//货币种类
			paramMap.put("op_user_id", partnerId);//操作员帐号, 默认为商户号
			sign = WeinxinUtil.getSignBycreateLinkString(paramMap, partnerKey, encoding);
			
			//创建POST提交的数据XML字符串
			tmpBuilder = new StringBuilder();
			tmpBuilder.append("<xml>");
			tmpBuilder.append("<appid><![CDATA[").append(appId).append("]]></appid>");//公众账号ID
			tmpBuilder.append("<mch_id><![CDATA[").append(partnerId).append("]]></mch_id>");//商户号
			//tmpBuilder.append("<device_info><![CDATA[]]></device_info>");//设备号
			tmpBuilder.append("<nonce_str><![CDATA[").append(nonceStr).append("]]></nonce_str>");//随机字符串
			tmpBuilder.append("<sign><![CDATA[").append(sign).append("]]></sign>");//签名
			if (!WeinxinUtil.isStrEmpty(transaction_id))
				tmpBuilder.append("<transaction_id><![CDATA[").append(transaction_id).append("]]></transaction_id>");//微信订单号
			if (!WeinxinUtil.isStrEmpty(out_trade_no))
				tmpBuilder.append("<out_trade_no><![CDATA[").append(out_trade_no).append("]]></out_trade_no>");//商户订单号
			tmpBuilder.append("<out_refund_no><![CDATA[").append(out_refund_no).append("]]></out_refund_no>");//商户退款单号
			tmpBuilder.append("<total_fee><![CDATA[").append(total_fee).append("]]></total_fee>");//总金额
			tmpBuilder.append("<refund_fee><![CDATA[").append(refund_fee).append("]]></refund_fee>");//退款金额
			//tmpBuilder.append("<refund_fee_type><![CDATA[]]></refund_fee_type>");//货币种类
			tmpBuilder.append("<op_user_id><![CDATA[").append(partnerId).append("]]></op_user_id>");//操作员帐号, 默认为商户号
			tmpBuilder.append("</xml>");
			requestContent = tmpBuilder.toString();
			
			return requestContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【申请退款】调用申请退款的（XML格式的）请求内容，返回响应信息（申请退款的第二部分）
	 * @param requestContent --String*-- （XML格式的）请求内容
	 * @return null 或 响应信息
	 * @throws Exception
	 */
	public String getResponseContentAfterCallRefund(String requestContent) throws Exception {
		if (WeinxinUtil.isStrEmpty(requestContent))
			throw new Exception("非空参数存在空值");
		if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
			throw new Exception("商家证书相关信息存在空值");
		
		String responseContent = null;
		
		//定义临时变量
		String encoding = null;
		SpecialHttpsHelper httpsHelper = null;
		SSLContext sslContext = null;
		
		try {
			//获取编码格式
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			
			//创建（带证书的）HTTPS的帮助类对象
			httpsHelper = SpecialHttpsHelper.getInstance();
			
			//获取SSLContext对象
			sslContext = httpsHelper.getSSLContextByFiles(caCert, partnerCert, partnerCertPasswd);
			
			//调用请求，返回响应信息
			if (sslContext != null)
				responseContent = httpsHelper.doPost(WeixinConfig.getConfig("weixinpub.pay.urlForRefund"), requestContent, encoding, sslContext);

			if (WeinxinUtil.isStrEmpty(responseContent))
				throw new Exception("无响应内容");
			
			return responseContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			httpsHelper = null;
			sslContext = null;
		}
	}
	
	/**
	 * 【申请退款】（申请退款的完整版）
	 * @param transaction_id --String-- 微信订单号（二选一）
	 * @param out_trade_no --String-- 商户订单号（二选一）
	 * @param out_refund_no --String*-- 商户退款单号
	 * @param total_fee --long*-- 总金额（单位：分）
	 * @param refund_fee --long*-- 退款金额（单位：分）
	 * @return null 或 响应内容Map（业务执行成功）
	 * @throws Exception
	 */
	public SortedMap<String, String> refund(String transaction_id, String out_trade_no, String out_refund_no, long total_fee, long refund_fee) throws Exception {
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		
		try {
			if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
				throw new Exception("商家相关信息存在空值");
			if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
				throw new Exception("商家证书相关信息存在空值");
			
			//获取请求中的XML格式数据
			requestContent = getRequestContentForRefund(transaction_id, out_trade_no, out_refund_no, total_fee, refund_fee);
			
			//执行申请退款,并获取响应内容
			responseContent = getResponseContentAfterCallRefund(requestContent);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.append("【退款申请】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//生成响应内容的签名并进行效验
			if (!checkResponseSign(dataMap)){
				tmpBuilder.append("【退款申请】响应信息中的签名验证失败");
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				return dataMap;
			}
			
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【退款查询】
	 * @param transaction_id --String-- 微信订单号（四选一）
	 * @param out_trade_no --String-- 商户订单号（四选一）
	 * @param out_refund_no --String-- 商户退款单号（四选一）
	 * @param refund_id --String-- 微信退款单号（四选一）
	 * @return null 或 响应内容Map（业务执行成功）
	 * @throws Exception
	 */
	public SortedMap<String, String> queryRefund(String transaction_id, String out_trade_no, String out_refund_no, String refund_id) throws Exception {
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(transaction_id) && WeinxinUtil.isStrEmpty(out_trade_no) && WeinxinUtil.isStrEmpty(out_refund_no) && WeinxinUtil.isStrEmpty(refund_id))
			throw new Exception("非空参数存在空值");
		
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, String> paramMap = null;
		String sign = null;
		StringBuilder tmpBuilder = null;
		String requestContent = null;
		String responseContent = null;
		
		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//创建数据集合以生成签名
			paramMap = new TreeMap<String, String>();
			paramMap.put("appid", appId);//公众账号ID
			paramMap.put("mch_id", partnerId);//商户号
			//paramMap.put("device_info", null);//设备号
			paramMap.put("nonce_str", nonceStr);//随机字符串
			paramMap.put("sign", "");//签名
			if (!WeinxinUtil.isStrEmpty(transaction_id))
				paramMap.put("transaction_id", transaction_id);//微信订单号
			if (!WeinxinUtil.isStrEmpty(out_trade_no))
				paramMap.put("out_trade_no", out_trade_no);//商户订单号
			if (!WeinxinUtil.isStrEmpty(out_refund_no))
				paramMap.put("out_refund_no", out_refund_no);//商户退款单号
			if (!WeinxinUtil.isStrEmpty(refund_id))
				paramMap.put("refund_id", refund_id);//微信退款单号
			sign = WeinxinUtil.getSignBycreateLinkString(paramMap, partnerKey, encoding);
			
			//创建POST提交的数据XML字符串
			tmpBuilder = new StringBuilder();
			tmpBuilder.append("<xml>");
			tmpBuilder.append("<appid><![CDATA[").append(appId).append("]]></appid>");//公众账号ID
			tmpBuilder.append("<mch_id><![CDATA[").append(partnerId).append("]]></mch_id>");//商户号
			//tmpBuilder.append("<device_info><![CDATA[]]></device_info>");//设备号
			tmpBuilder.append("<nonce_str><![CDATA[").append(nonceStr).append("]]></nonce_str>");//随机字符串
			tmpBuilder.append("<sign><![CDATA[").append(sign).append("]]></sign>");//签名
			if (!WeinxinUtil.isStrEmpty(transaction_id))
				tmpBuilder.append("<transaction_id><![CDATA[").append(transaction_id).append("]]></transaction_id>");//微信订单号
			if (!WeinxinUtil.isStrEmpty(out_trade_no))
				tmpBuilder.append("<out_trade_no><![CDATA[").append(out_trade_no).append("]]></out_trade_no>");//商户订单号
			if (!WeinxinUtil.isStrEmpty(out_refund_no))
				tmpBuilder.append("<out_refund_no><![CDATA[").append(out_refund_no).append("]]></out_refund_no>");//商户退款单号
			if (!WeinxinUtil.isStrEmpty(refund_id))
				tmpBuilder.append("<refund_id><![CDATA[").append(refund_id).append("]]></refund_id>");//微信退款单号
			tmpBuilder.append("</xml>");
			requestContent = tmpBuilder.toString();
			
			//模拟HTTPs的POST方法提交以获取响应信息字符串
			responseContent = HttpsHelper.getInstance().executePostMethod(WeixinConfig.getConfig("weixinpub.pay.urlForRefundquery"), requestContent, encoding);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【退款查询】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//生成响应内容的签名并进行效验
			if (!checkResponseSign(dataMap)){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【退款查询】响应信息中的签名验证失败");
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【退款查询】业务执行失败，错误代码为").append(dataMap.get("err_code")).append("，错误描述：").append(dataMap.get("err_code_des"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//返回
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			tmpBuilder = null;
			requestContent = null;
			responseContent = null;
		}
	}
	
	/**
	 * 【下载对账单】
	 * @param bill_date --String*-- 对账单日起（格式：yyyyMMdd）
	 * @param bill_type --String-- 账单类型（ALL、SUCCESS、REFUND，默认值ALL）
	 * @return null 或 响应内容列表（查询成功时）
	 * @throws Exception
	 */
	public List<String> downloadbill(String bill_date, String bill_type) throws Exception {
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(bill_date))
			throw new Exception("非空参数存在空值");
		
		List<String> responseContentList = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, String> paramMap = null;
		String sign = null;
		StringBuilder tmpBuilder = null;
		String requestContent = null;
		String responseContent = null;
		SortedMap<String, String> dataMap = null;
		
		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//创建数据集合以生成签名
			paramMap = new TreeMap<String, String>();
			paramMap.put("appid", appId);//公众账号ID
			paramMap.put("mch_id", partnerId);//商户号
			//paramMap.put("device_info", null);//设备号
			paramMap.put("nonce_str", nonceStr);//随机字符串
			paramMap.put("sign", "");//签名
			paramMap.put("bill_date", bill_date);//对账单日起
			paramMap.put("bill_type", WeinxinUtil.isStrEmpty(bill_type) ? "ALL" : bill_type);//账单类型
			sign = WeinxinUtil.getSignBycreateLinkString(paramMap, partnerKey, encoding);
			
			//创建POST提交的数据XML字符串
			tmpBuilder = new StringBuilder();
			tmpBuilder.append("<xml>");
			tmpBuilder.append("<appid><![CDATA[").append(appId).append("]]></appid>");//公众账号ID
			tmpBuilder.append("<mch_id><![CDATA[").append(partnerId).append("]]></mch_id>");//商户号
			//tmpBuilder.append("<device_info><![CDATA[]]></device_info>");//设备号
			tmpBuilder.append("<nonce_str><![CDATA[").append(nonceStr).append("]]></nonce_str>");//随机字符串
			tmpBuilder.append("<sign><![CDATA[").append(sign).append("]]></sign>");//签名
			tmpBuilder.append("<bill_date><![CDATA[").append(bill_date).append("]]></bill_date>");//对账单日起
			tmpBuilder.append("<bill_type><![CDATA[").append(WeinxinUtil.isStrEmpty(bill_type) ? "ALL" : bill_type).append("]]></bill_type>");//微信订单号
			tmpBuilder.append("</xml>");
			requestContent = tmpBuilder.toString();
			
			//模拟HTTPs的POST方法提交以获取响应信息字符串
			responseContentList = HttpsHelper.getInstance().specialExecutePostMethod(WeixinConfig.getConfig("weixinpub.pay.urlForDownloadbill"), requestContent, encoding);
			if (responseContentList == null || responseContentList.isEmpty())
				throw new Exception("无响应内容");
			
			//判断接口是否失败
			responseContent = responseContentList.get(0);
			if (responseContent.indexOf("return_code") != -1){
				dataMap = XMLUtil.doXMLParse(WeinxinUtil.getStringFromList(responseContentList));
				tmpBuilder.delete(0, tmpBuilder.length()).append("【下载对账单】调用接口失败：").append(dataMap.get("return_msg"));
				throw new Exception(tmpBuilder.toString());
			}
			
			//返回对账单信息
			return responseContentList;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			tmpBuilder = null;
			requestContent = null;
			responseContent = null;
			if (dataMap != null)
				dataMap.clear();
			dataMap = null;
		}
	}
	
	/**
	 * 【发放代金券】获取发放代金券的（XML格式的）请求内容（发放代金券的第一部分）
	 * @param coupon_stock_id --String*-- 代金券批次id（商户系统创建代金券后获得）
	 * @param partner_trade_no --String*-- 商户此次发放凭据号（由getPartnerTradeNo()方法获取）
	 * @param openid --String*-- 用户openid
	 * @return null 或 （XML格式的）请求内容
	 * @throws Exception
	 */
	public String getRequestContentForSendCoupon(String coupon_stock_id, String partner_trade_no, String openid) throws Exception{
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(coupon_stock_id) || WeinxinUtil.isStrEmpty(partner_trade_no) || WeinxinUtil.isStrEmpty(openid))
			throw new Exception("非空参数存在空值");

		String requestContent = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;

		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//收集参数
			paramMap = new TreeMap<String, Object>();
			paramMap.put("coupon_stock_id", coupon_stock_id);	//代金券批次id		coupon_stock_id		是	1757								String		代金券批次id
			paramMap.put("openid_count", Integer.valueOf(1));	//openid记录数	openid_count		是	1									int			openid记录数（目前支持num=1）
			paramMap.put("partner_trade_no", partner_trade_no);	//商户单据号		partner_trade_no	是	1000009820141203515766				String		商户此次发放凭据号（格式：商户id+日期+流水号），商户侧需保持唯一性
			paramMap.put("openid", openid);						//用户openid		openid				是	onqOjjrXT-776SpHnfexGm1_P7iE		String		Openid信息
			paramMap.put("appid", appId);						//公众账号ID		appid				是	wx5edab3bdfba3dc1c					String(32)	微信分配的公众账号ID（企业号corpid即为此appId）
			paramMap.put("mch_id", partnerId);					//商户号			mch_id				是	10000098							String(32)	微信支付分配的商户号
			paramMap.put("op_user_id", partnerId);				//操作员			op_user_id			否	10000098							String(32)	操作员帐号, 默认为商户号 可在商户平台配置操作员对应的api权限
			//paramMap.put("device_info", "");					//设备号			device_info			否	 									String(32)	微信支付分配的终端设备号
			paramMap.put("nonce_str", nonceStr);				//随机字符串		nonce_str			是	1417574675							String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");							//签名			sign				是	841B3002FE2220C87A2D08ABD8A8F791	String(32)	签名，具体参见3.2.1
			paramMap.put("version", "1.0");						//协议版本			version				否	1.0									String(32)	默认1.0
			paramMap.put("type", "XML");						//协议类型			type				否	XML									String(32)	XML【目前仅支持默认XML】
			
			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);

			return requestContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
		}
	}
	
	/**
	 * 【发放代金券】调用发放代金券的（XML格式的）请求内容，返回响应信息（发放代金券的第二部分）
	 * @param requestContent --String*-- （XML格式的）请求内容
	 * @return null 或 响应信息
	 */
	public String getResponseContentAfterSendCoupon(String requestContent) throws Exception{
		if (WeinxinUtil.isStrEmpty(requestContent))
			throw new Exception("非空参数存在空值");
		if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
			throw new Exception("商家证书相关信息存在空值");
		
		String responseContent = null;
		
		//定义临时变量
		String encoding = null;
		SpecialHttpsHelper httpsHelper = null;
		SSLContext sslContext = null;
		
		try {
			//获取编码格式
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			
			//创建（带证书的）HTTPS的帮助类对象
			httpsHelper = SpecialHttpsHelper.getInstance();
			
			//获取SSLContext对象
			sslContext = httpsHelper.getSSLContextByFiles(caCert, partnerCert, partnerCertPasswd);
			
			//调用请求，返回响应信息
			if (sslContext != null)
				responseContent = httpsHelper.doPost(WeixinConfig.getConfig("weixinpub.pay.urlForSendCoupon"), requestContent, encoding, sslContext);
			
			if (WeinxinUtil.isStrEmpty(responseContent))
				throw new Exception("无响应内容");
			
			return responseContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			httpsHelper = null;
			sslContext = null;
		}
	}
	
	/**
	 * 【发放代金券】
	 * @param coupon_stock_id --String*-- 代金券批次id（商户系统创建代金券后获得）
	 * @param partner_trade_no --String*-- 商户此次发放凭据号（格式：商户id+日期+流水号），商户侧需保持唯一性
	 * @param openid --String*-- 用户openid
	 * @return null 或 返回结果集
	 * @throws Exception
	 */
	public SortedMap<String, String> sendCoupon(String coupon_stock_id, String partner_trade_no, String openid)throws Exception{
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		
		try {
			if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
				throw new Exception("商家相关信息存在空值");
			if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
				throw new Exception("商家证书相关信息存在空值");
			
			//获取请求中的XML格式数据
			requestContent = getRequestContentForSendCoupon(coupon_stock_id, partner_trade_no, openid);

			//获取响应内容
			responseContent = getResponseContentAfterSendCoupon(requestContent);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){//SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
				tmpBuilder.append("【发放代金券】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.append("【发放代金券】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){//业务结果
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				return dataMap;
			}
			
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【查询代金券批次信息】
	 * @param coupon_stock_id --String*-- 代金券批次id（商户系统创建代金券后获得）
	 * @return null 或 返回结果集
	 * @throws Exception
	 */
	public SortedMap<String, String> queryCouponStock(String coupon_stock_id)throws Exception{
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(coupon_stock_id))
			throw new Exception("非空参数存在空值");
		
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;

		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//收集参数
			paramMap = new TreeMap<String, Object>();
			paramMap.put("coupon_stock_id", coupon_stock_id);	//代金券批次id		coupon_stock_id	是	1757								String		代金券批次id
			paramMap.put("appid", appId);						//公众账号ID		appid			是	wx5edab3bdfba3dc1c					String(32)	微信分配的公众账号ID（企业号corpid即为此appId）
			paramMap.put("mch_id", partnerId);					//商户号			mch_id			是	10000098							String(32)	微信支付分配的商户号
			paramMap.put("op_user_id", partnerId);				//操作员			op_user_id		否	10000098							String(32)	操作员帐号, 默认为商户号 可在商户平台配置操作员对应的api权限
			//paramMap.put("device_info", "");					//设备号			device_info		否	 									String(32)	微信支付分配的终端设备号
			paramMap.put("nonce_str", nonceStr);				//随机字符串		nonce_str		是	1417574675							String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");							//签名			sign			是	841B3002FE2220C87A2D08ABD8A8F791	String(32)	签名，详见签名生成算法
			paramMap.put("version", "1.0");						//协议版本			version			否	1.0									String(32)	默认1.0
			paramMap.put("type", "XML");						//协议类型			type			否	XML									String(32)	XML【目前仅支持默认XML】
			
			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);
			
			//模拟HTTPs的POST方法提交以获取响应信息字符串
			responseContent = HttpsHelper.getInstance().executePostMethod(WeixinConfig.getConfig("weixinpub.pay.urlForQueryCouponStock"), requestContent, encoding);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询代金券批次信息】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.delete(0, tmpBuilder.length()).append("【查询代金券批次信息】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询代金券批次信息】业务执行失败，错误代码为").append(dataMap.get("err_code")).append("，错误描述：").append(dataMap.get("err_code_des"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//返回
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【查询代金券信息】
	 * @param coupon_id --String*-- 代金券id
	 * @param openid --String*-- 用户在商户appid下的唯一标识
	 * @param stock_id --String*-- 代金劵对应的批次号（商户系统创建代金券后获得）
	 * @return null 或 返回结果集
	 * @throws Exception
	 */
	public SortedMap<String, String> queryCouponsInfo(String coupon_id, String openid, String stock_id)throws Exception{
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(coupon_id) || WeinxinUtil.isStrEmpty(openid) || WeinxinUtil.isStrEmpty(stock_id))
			throw new Exception("非空参数存在空值");
		
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		
		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();

			//收集参数
			paramMap = new TreeMap<String, Object>();
			paramMap.put("coupon_id", coupon_id);	//代金券id	coupon_id		是	1565								String		代金券id
			paramMap.put("openid", openid);			//用户标识		openid			是	onqOjjrXT-776SpHnfexGm1_P7iE		String		用户在商户appid下的唯一标识
			paramMap.put("appid", appId);			//公众账号ID	appid			是	wx5edab3bdfba3dc1c					String(32)	微信分配的公众账号ID
			paramMap.put("mch_id", partnerId);		//商户号		mch_id			是	10000098							String(32)	微信支付分配的商户号
			paramMap.put("stock_id", stock_id);		//批次号		stock_id		是	58818								String(32)	代金劵对应的批次号
			paramMap.put("op_user_id", partnerId);	//操作员		op_user_id		否	10000098							String(32)	操作员帐号, 默认为商户号 可在商户平台配置操作员对应的api权限
			//paramMap.put("device_info", "");		//设备号		device_info		否	 									String(32)	微信支付分配的终端设备号
			paramMap.put("nonce_str", nonceStr);	//随机字符串	nonce_str		是	1417574675							String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");				//签名		sign			是	841B3002FE2220C87A2D08ABD8A8F791	String(32)	签名，详见签名生成算法
			paramMap.put("version", "1.0");			//协议版本		version			否	1.0									String(32)	默认1.0
			paramMap.put("type", "XML");			//协议类型		type			否	XML									String(32)	XML【目前仅支持默认XML】

			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);
			
			//模拟HTTPs的POST方法提交以获取响应信息字符串
			responseContent = HttpsHelper.getInstance().executePostMethod(WeixinConfig.getConfig("weixinpub.pay.urlForQueryCouponsInfo"), requestContent, encoding);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询代金券信息】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.delete(0, tmpBuilder.length()).append("【查询代金券信息】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询代金券信息】业务执行失败，错误代码为").append(dataMap.get("err_code")).append("，错误描述：").append(dataMap.get("err_code_des"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//返回
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【发放普通红包】获取发放普通红包的（XML格式的）请求内容（发放普通红包的第一部分）
	 * @param mch_billno --String*-- 商户订单号（由getPartnerTradeNo()方法获取）
	 * @param send_name --String*-- 商户名称送者名称）（String(32)）
	 * @param re_openid --String*-- 用户openid（接受红包的用户在wxappid下的openid）
	 * @param total_amount --Integer*-- 付款金额（单位：分）（最少100）
	 * @param wishing --String*-- 红包祝福语（String(128)）
	 * @param client_ip --String*-- 调用接口的机器Ip地址（String(15)）
	 * @param act_name --String*-- 活动名称（String(32)）
	 * @param remark --String*-- 备注信息（String(256)）
	 * @param scene_id --String-- 发放红包使用场景（红包金额大于200时必传）（PRODUCT_1:商品促销、PRODUCT_2:抽奖、PRODUCT_3:虚拟物品兑奖 、PRODUCT_4:企业内部福利、PRODUCT_5:渠道分润、PRODUCT_6:保险回馈、PRODUCT_7:彩票派奖、PRODUCT_8:税务刮奖）
	 * @return null 或 （XML格式的）请求内容
	 * @throws Exception
	 */
	public String getRequestContentForSendRedPack(String mch_billno, String send_name, String re_openid, Integer total_amount, String wishing, String client_ip, String act_name, String remark, String scene_id) throws Exception{
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(mch_billno) || WeinxinUtil.isStrEmpty(send_name) || WeinxinUtil.isStrEmpty(re_openid) || total_amount == null || WeinxinUtil.isStrEmpty(wishing) || WeinxinUtil.isStrEmpty(client_ip) || WeinxinUtil.isStrEmpty(act_name) || WeinxinUtil.isStrEmpty(remark))
			throw new Exception("非空参数存在空值");
		if (total_amount.intValue() <= 0)
			throw new Exception("参数[付款金额]取值范围有误");
		if (total_amount.intValue() >= 20000 && WeinxinUtil.isStrEmpty(scene_id))
			throw new Exception("参数[发放红包使用场景id]为空值");

		String requestContent = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;

		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//创建数据集合以生成签名
			paramMap = new TreeMap<String, Object>();
			paramMap.put("nonce_str", nonceStr);			//随机字符串		nonce_str		是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");						//签名			sign			是	C380BEC2BFD727A4B6845133519F3AD6	String(32)	详见签名生成算法
			paramMap.put("mch_billno", mch_billno);			//商户订单号		mch_billno		是	10000098201411111234567890			String(28)	商户订单号（每个订单号必须唯一）组成：mch_id+yyyymmdd+10位一天内不能重复的数字。接口根据商户订单号支持重入，如出现超时可再调用。
			paramMap.put("mch_id", partnerId);				//商户号			mch_id			是	10000098							String(32)	微信支付分配的商户号
			paramMap.put("wxappid", appId);					//公众账号appid	wxappid			是	wx8888888888888888					String(32)	微信分配的公众账号ID（企业号corpid即为此appId）。接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）。
			paramMap.put("send_name", send_name);			//商户名称			send_name		是	天虹百货								String(32)	红包发送者名称
			paramMap.put("re_openid", re_openid);			//用户openid		re_openid		是	oxTWIuGaIt6gTKsQRLau2M0yL16E		String(32)	接受红包的用户用户在wxappid下的openid
			paramMap.put("total_amount", total_amount);		//付款金额			total_amount	是	1000								int			付款金额，单位分
			paramMap.put("total_num", Integer.valueOf(1));	//红包发放总人数		total_num		是	1									int			红包发放总人数total_num=1
			paramMap.put("wishing", wishing);				//红包祝福语		wishing			是	感谢您参加猜灯谜活动，祝您元宵节快乐！			String(128)	红包祝福语
			paramMap.put("client_ip", client_ip);			//Ip地址			client_ip		是	192.168.0.1							String(15)	调用接口的机器Ip地址
			paramMap.put("act_name", act_name);				//活动名称			act_name		是	猜灯谜抢红包活动							String(32)	活动名称
			paramMap.put("remark", remark);					//备注			remark			是	猜越多得越多，快来抢！						String(256)	备注信息
			if (!WeinxinUtil.isStrEmpty(scene_id))
				paramMap.put("scene_id", scene_id);			//场景id			scene_id		否	PRODUCT_8							String(32)	发放红包使用场景，红包金额大于200时必传
															//																					PRODUCT_1:商品促销
															//																					PRODUCT_2:抽奖
															//																					PRODUCT_3:虚拟物品兑奖 
															//																					PRODUCT_4:企业内部福利
															//																					PRODUCT_5:渠道分润
															//																					PRODUCT_6:保险回馈
															//																					PRODUCT_7:彩票派奖
															//																					PRODUCT_8:税务刮奖
			//paramMap.put("risk_info", "");				//活动信息			risk_info		否										String(128)	
															//																					posttime:用户操作的时间戳
															//																					mobile:业务系统账号的手机号，国家代码-手机号。不需要+号
															//																					deviceid :mac 地址或者设备唯一标识 
															//																					clientversion :用户操作的客户端版本
															//																					把值为非空的信息用key=value进行拼接，再进行urlencode
															//																					urlencode(posttime=xx& mobile =xx&deviceid=xx)
			//paramMap.put("consume_mch_id", "");			//资金授权商户号		consume_mch_id	否	1222000096							String(32)	资金授权商户号服务商替特约商户发放时使用
			
			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);

			return requestContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
		}
	}
	
	/**
	 * 【发放普通红包】调用发放普通红包的（XML格式的）请求内容，返回响应信息（发放普通红包的第二部分）
	 * @param requestContent --String*-- （XML格式的）请求内容
	 * @return null 或 响应信息
	 * @throws Exception
	 */
	public String getResponseContentAfterSendRedPack(String requestContent) throws Exception{
		if (WeinxinUtil.isStrEmpty(requestContent))
			throw new Exception("非空参数存在空值");
		if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
			throw new Exception("商家证书相关信息存在空值");
		
		String responseContent = null;
		
		//定义临时变量
		String encoding = null;
		SpecialHttpsHelper httpsHelper = null;
		SSLContext sslContext = null;
		
		try {
			//获取编码格式
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			
			//创建（带证书的）HTTPS的帮助类对象
			httpsHelper = SpecialHttpsHelper.getInstance();
			
			//获取SSLContext对象
			sslContext = httpsHelper.getSSLContextByFiles(caCert, partnerCert, partnerCertPasswd);
			
			//调用请求，返回响应信息
			if (sslContext != null)
				responseContent = httpsHelper.doPost(WeixinConfig.getConfig("weixinpub.pay.urlForSendRedPack"), requestContent, encoding, sslContext);
			if (WeinxinUtil.isStrEmpty(responseContent))
				throw new Exception("无响应内容");
			
			return responseContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			httpsHelper = null;
			sslContext = null;
		}
	}
	
	/**
	 * 【发放普通红包】
	 * @param mch_billno --String*-- 商户订单号（由getPartnerTradeNo()方法获取）
	 * @param send_name --String*-- 商户名称（红包发送者名称）（String(32)）
	 * @param re_openid --String*-- 用户openid（接受红包的用户在wxappid下的openid）
	 * @param total_amount --Integer*-- 付款金额（单位：分）（每个红包的平均金额必须在1.00元到200.00元之间）
	 * @param wishing --String*-- 红包祝福语（String(128)）
	 * @param client_ip --String*-- 调用接口的机器Ip地址（String(15)）
	 * @param act_name --String*-- 活动名称（String(32)）
	 * @param remark --String*-- 备注信息（String(256)）
	 * @param scene_id --String-- 发放红包使用场景（红包金额大于200时必传）（PRODUCT_1:商品促销、PRODUCT_2:抽奖、PRODUCT_3:虚拟物品兑奖 、PRODUCT_4:企业内部福利、PRODUCT_5:渠道分润、PRODUCT_6:保险回馈、PRODUCT_7:彩票派奖、PRODUCT_8:税务刮奖）
	 * @return null 或 返回结果集
	 * @throws Exception
	 */
	public SortedMap<String, String> sendRedPack(String mch_billno, String send_name, String re_openid, Integer total_amount, String wishing, String client_ip, String act_name, String remark, String scene_id)throws Exception{
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		
		try {
			if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
				throw new Exception("商家相关信息存在空值");
			
			//获取请求中的XML格式数据
			requestContent = getRequestContentForSendRedPack(mch_billno, send_name, re_openid, total_amount, wishing, client_ip, act_name, remark, scene_id);

			//获取响应内容
			responseContent = getResponseContentAfterSendRedPack(requestContent);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){//SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
				tmpBuilder.append("【发放普通红包】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.append("【发放普通红包】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){//业务结果
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				return dataMap;
			}
			
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【发放裂变红包】获取发放裂变红包的（XML格式的）请求内容（发放裂变红包的第一部分）
	 * @param mch_billno --String*-- 商户订单号（由getPartnerTradeNo()方法获取）
	 * @param send_name --String*-- 商户名称送者名称）（String(32)）
	 * @param re_openid --String*-- 用户openid（接收红包的种子用户[首个用户]用户在wxappid下的openid）
	 * @param total_amount --Integer*-- 红包发放总金额，即一组红包金额总和，包括分享者的红包和裂变的红包，单位分（每个红包的平均金额必须在1.00元到200.00元之间）
	 * @param total_num --Integer*-- 红包发放总人数，即总共有多少人可以领到该组红包（包括分享者）（必须介于(包括)3到20之间）
	 * @param wishing --String*-- 红包祝福语（String(128)）
	 * @param act_name --String*-- 活动名称（String(32)）
	 * @param remark --String*-- 备注（String(256)）
	 * @param scene_id --String-- 发放红包使用场景（PRODUCT_1:商品促销、PRODUCT_2:抽奖、PRODUCT_3:虚拟物品兑奖 、PRODUCT_4:企业内部福利、PRODUCT_5:渠道分润、PRODUCT_6:保险回馈、PRODUCT_7:彩票派奖、PRODUCT_8:税务刮奖）
	 * @return null 或 （XML格式的）请求内容
	 * @throws Exception
	 */
	public String getRequestContentForSendGroupRedPack(String mch_billno, String send_name, String re_openid, Integer total_amount, Integer total_num, String wishing, String act_name, String remark, String scene_id) throws Exception{
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(mch_billno) || WeinxinUtil.isStrEmpty(send_name) || WeinxinUtil.isStrEmpty(re_openid) || total_amount == null || total_num == null || WeinxinUtil.isStrEmpty(wishing) || WeinxinUtil.isStrEmpty(act_name) || WeinxinUtil.isStrEmpty(remark))
			throw new Exception("非空参数存在空值");
		if (total_amount.intValue() <= 0)
			throw new Exception("参数[总金额]取值范围有误");
		if (total_num.intValue() <= 0)
			throw new Exception("参数[红包发放总人数]取值范围有误");

		String requestContent = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;

		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();

			//创建数据集合以生成签名
			paramMap = new TreeMap<String, Object>();
			paramMap.put("nonce_str", nonceStr);			//随机字符串		nonce_str		是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");						//签名			sign			是	C380BEC2BFD727A4B6845133519F3AD6	String(32)	详见签名生成算法
			paramMap.put("mch_billno", mch_billno);			//商户订单号		mch_billno		是	10000098201411111234567890			String(28)	商户订单号（每个订单号必须唯一）组成：mch_id+yyyymmdd+10位一天内不能重复的数字。接口根据商户订单号支持重入，如出现超时可再调用。
			paramMap.put("mch_id", partnerId);				//商户号			mch_id			是	10000098							String(32)	微信支付分配的商户号
			paramMap.put("wxappid", appId);					//公众账号appid	wxappid			是	wx8888888888888888					String(32)	微信分配的公众账号ID（企业号corpid即为此appId）。接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）。
			paramMap.put("send_name", send_name);			//商户名称			send_name		是	天虹百货								String(32)	红包发送者名称
			paramMap.put("re_openid", re_openid);			//用户openid		re_openid		是	oxTWIuGaIt6gTKsQRLau2M0yL16E		String(32)	接受红包的用户用户在wxappid下的openid
			paramMap.put("total_amount", total_amount);		//总金额			total_amount	是	1000								int			红包发放总金额，即一组红包金额总和，包括分享者的红包和裂变的红包，单位分
			paramMap.put("total_num", total_num);			//红包发放总人数		total_num		是	3									int			红包发放总人数，即总共有多少人可以领到该组红包（包括分享者）
			paramMap.put("amt_type", "ALL_RAND");			//红包金额设置方式	amt_type		是	ALL_RAND							String(32)	红包金额设置方式 ALL_RAND—全部随机,商户指定总金额和红包发放总人数，由微信支付随机计算出各红包金额
			paramMap.put("wishing", wishing);				//红包祝福语		wishing			是	感谢您参加猜灯谜活动，祝您元宵节快乐！			String(128)	红包祝福语
			paramMap.put("act_name", act_name);				//活动名称			act_name		是	猜灯谜抢红包活动							String(32)	活动名称
			paramMap.put("remark", remark);					//备注			remark			是	猜越多得越多，快来抢！						String(256)	备注信息
			if (!WeinxinUtil.isStrEmpty(scene_id))
				paramMap.put("scene_id", scene_id);			//场景id			scene_id		否	PRODUCT_8							String(32)	发放红包使用场景，红包金额大于200时必传
															//																					PRODUCT_1:商品促销
															//																					PRODUCT_2:抽奖
															//																					PRODUCT_3:虚拟物品兑奖 
															//																					PRODUCT_4:企业内部福利
															//																					PRODUCT_5:渠道分润
															//																					PRODUCT_6:保险回馈
															//																					PRODUCT_7:彩票派奖
															//																					PRODUCT_8:税务刮奖
			//paramMap.put("risk_info", "");				//活动信息			risk_info		否										String(128)	
															//																					posttime:用户操作的时间戳
															//																					mobile:业务系统账号的手机号，国家代码-手机号。不需要+号
															//																					deviceid :mac 地址或者设备唯一标识 
															//																					clientversion :用户操作的客户端版本
															//																					把值为非空的信息用key=value进行拼接，再进行urlencode
															//																					urlencode(posttime=xx& mobile =xx&deviceid=xx)
			//paramMap.put("consume_mch_id", "");			//资金授权商户号		consume_mch_id	否	1222000096							String(32)	资金授权商户号服务商替特约商户发放时使用
			
			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);

			return requestContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
		}
	}
	
	/**
	 * 【发放裂变红包】调用发放裂变红包的（XML格式的）请求内容，返回响应信息（发放裂变红包的第二部分）
	 * @param requestContent --String*-- （XML格式的）请求内容
	 * @return null 或 响应信息
	 * @throws Exception
	 */
	public String getResponseContentAfterSendGroupRedPack(String requestContent) throws Exception{
		if (WeinxinUtil.isStrEmpty(requestContent))
			throw new Exception("非空参数存在空值");
		if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
			throw new Exception("商家证书相关信息存在空值");
		
		String responseContent = null;
		
		//定义临时变量
		String encoding = null;
		SpecialHttpsHelper httpsHelper = null;
		SSLContext sslContext = null;
		
		try {
			//获取编码格式
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			
			//创建（带证书的）HTTPS的帮助类对象
			httpsHelper = SpecialHttpsHelper.getInstance();
			
			//获取SSLContext对象
			sslContext = httpsHelper.getSSLContextByFiles(caCert, partnerCert, partnerCertPasswd);
			
			//调用请求，返回响应信息
			if (sslContext != null)
				responseContent = httpsHelper.doPost(WeixinConfig.getConfig("weixinpub.pay.urlForSendGroupRedPack"), requestContent, encoding, sslContext);
			if (WeinxinUtil.isStrEmpty(responseContent))
				throw new Exception("无响应内容");
			
			return responseContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			httpsHelper = null;
			sslContext = null;
		}
	}
	
	/**
	 * 【发放裂变红包】
	 * @param mch_billno --String*-- 商户订单号（由getPartnerTradeNo()方法获取）
	 * @param send_name --String*-- 商户名称送者名称）（String(32)）
	 * @param re_openid --String*-- 用户openid（接收红包的种子用户[首个用户]用户在wxappid下的openid）
	 * @param total_amount --Integer*-- 红包发放总金额，即一组红包金额总和，包括分享者的红包和裂变的红包，单位分（每个红包的平均金额必须在1.00元到200.00元之间）
	 * @param total_num --Integer*-- 红包发放总人数，即总共有多少人可以领到该组红包（包括分享者）（必须介于(包括)3到20之间）
	 * @param wishing --String*-- 红包祝福语（String(128)）
	 * @param act_name --String*-- 活动名称（String(32)）
	 * @param remark --String*-- 备注（String(256)）
	 * @param scene_id --String-- 发放红包使用场景（PRODUCT_1:商品促销、PRODUCT_2:抽奖、PRODUCT_3:虚拟物品兑奖 、PRODUCT_4:企业内部福利、PRODUCT_5:渠道分润、PRODUCT_6:保险回馈、PRODUCT_7:彩票派奖、PRODUCT_8:税务刮奖）
	 * @return null 或 响应信息
	 * @throws Exception
	 */
	public SortedMap<String, String> sendGroupRedPack(String mch_billno, String send_name, String re_openid, Integer total_amount, Integer total_num, String wishing, String act_name, String remark, String scene_id)throws Exception{
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		
		try {
			if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
				throw new Exception("商家相关信息存在空值");
			if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
				throw new Exception("商家证书相关信息存在空值");
			
			//获取请求中的XML格式数据
			requestContent = getRequestContentForSendGroupRedPack(mch_billno, send_name, re_openid, total_amount, total_num, wishing, act_name, remark, scene_id);

			//获取响应内容
			responseContent = getResponseContentAfterSendGroupRedPack(requestContent);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){//SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
				tmpBuilder.append("【发放裂变红包】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.append("【发放裂变红包】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){//业务结果
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				return dataMap;
			}
			
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【查询红包记录】
	 * @param mch_billno --String*-- 商户订单号（由getPartnerTradeNo()方法获取）
	 * @return null 或 响应内容Map（业务执行成功）
	 * @throws Exception
	 */
	public SortedMap<String, String> queryRedPack(String mch_billno)throws Exception{
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		SpecialHttpsHelper httpsHelper = null;
		SSLContext sslContext = null;
		
		try {
			if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
				throw new Exception("商家相关信息存在空值");
			if (ToolUtil.isStrEmpty(mch_billno))
				throw new Exception("非空参数存在空值");
			if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
				throw new Exception("商家证书相关信息存在空值");
			
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();
			
			//收集参数
			paramMap = new TreeMap<String, Object>();
			paramMap.put("nonce_str", nonceStr);	//随机字符串	nonce_str	是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");				//签名		sign		是	C380BEC2BFD727A4B6845133519F3AD6	String(32)	详见签名生成算法
			paramMap.put("mch_billno", mch_billno);	//商户订单号	mch_billno	是	10000098201411111234567890			String(28)	商户发放红包的商户订单号
			paramMap.put("mch_id", partnerId);		//商户号		mch_id		是	10000098							String(32)	微信支付分配的商户号
			paramMap.put("appid", appId);			//Appid		appid		是	wxe062425f740d30d8					String(32)	微信分配的公众账号ID（企业号corpid即为此appId），接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）。
			paramMap.put("bill_type", "MCHT");		//订单类型		bill_type	是	MCHT								String(32)	MCHT:通过商户订单号获取红包信息。

			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);

			//创建（带证书的）HTTPS的帮助类对象
			httpsHelper = SpecialHttpsHelper.getInstance();
			
			//获取SSLContext对象
			sslContext = httpsHelper.getSSLContextByFiles(caCert, partnerCert, partnerCertPasswd);
			
			//调用请求，返回响应信息
			if (sslContext != null)
				responseContent = httpsHelper.doPost(WeixinConfig.getConfig("weixinpub.pay.urlForGetRedPackInfo"), requestContent, encoding, sslContext);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询红包记录】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.delete(0, tmpBuilder.length()).append("【查询红包记录】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询红包记录】业务执行失败，错误代码为").append(dataMap.get("err_code")).append("，错误描述：").append(dataMap.get("err_code_des"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//返回
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
			httpsHelper = null;
			sslContext = null;
		}
	}
	
	/**
	 * 【企业付款】获取企业付款的（XML格式的）请求内容（企业付款的第一部分）
	 * @param partner_trade_no --String*-- 商户订单号（由getPartnerTradeNo()方法获取）
	 * @param openid --String*-- 用户openid（商户appid下，某用户的openid）
	 * @param check_name --String*-- 校验用户姓名选项（NO_CHECK：不校验真实姓名；FORCE_CHECK：强校验真实姓名[未实名认证的用户会校验失败，无法转账]；OPTION_CHECK：针对已实名认证的用户才校验真实姓名[未实名认证用户不校验，可以转账成功]）
	 * @param re_user_name --String-- 收款用户真实姓名（如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填）
	 * @param amount --Integer*-- 企业付款金额（单位：分）（金额区间在1.00元到20000.00元之间）
	 * @param desc --String*-- 企业付款描述信息
	 * @param spbill_create_ip --String*-- 调用接口的机器Ip地址（String(32)）
	 * @return null 或 （XML格式的）请求内容
	 * @throws Exception
	 */
	public String getRequestContentForTransfers(String partner_trade_no, String openid, String check_name, String re_user_name, Integer amount, String desc, String spbill_create_ip) throws Exception{
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(partner_trade_no) || WeinxinUtil.isStrEmpty(openid) || WeinxinUtil.isStrEmpty(check_name) || amount == null || WeinxinUtil.isStrEmpty(desc) || WeinxinUtil.isStrEmpty(spbill_create_ip))
			throw new Exception("非空参数存在空值");
		else if (amount.intValue() <= 0)
			throw new Exception("参数[金额]取值范围有误");
		else if (!"NO_CHECK".equals(check_name) && !"FORCE_CHECK".equals(check_name) && !"OPTION_CHECK".equals(check_name))
			throw new Exception("参数[校验用户姓名选项]取值范围有误");
		else if (("FORCE_CHECK".equals(check_name) || "OPTION_CHECK".equals(check_name)) && WeinxinUtil.isStrEmpty(re_user_name))
			throw new Exception("参数[收款用户真实姓名]存在空值");

		String requestContent = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;

		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();

			//创建数据集合以生成签名
			paramMap = new TreeMap<String, Object>();
			paramMap.put("mch_appid", appId);					//公众账号appid	mch_appid			是	wx8888888888888888					String		微信分配的公众账号ID（企业号corpid即为此appId）
			paramMap.put("mchid", partnerId);					//商户号			mchid				是	1900000109							String(32)	微信支付分配的商户号
			//paramMap.put("device_info", "");					//设备号			device_info			否	013467007045764						String(32)	微信支付分配的终端设备号
			paramMap.put("nonce_str", nonceStr);				//随机字符串		nonce_str			是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");							//签名			sign				是	C380BEC2BFD727A4B6845133519F3AD6	String(32)	签名，详见签名算法
			paramMap.put("partner_trade_no", partner_trade_no);	//商户订单号		partner_trade_no	是	10000098201411111234567890			String		商户订单号，需保持唯一性(只能是字母或者数字，不能包含有符号)
			paramMap.put("openid", openid);						//用户openid		openid				是	oxTWIuGaIt6gTKsQRLau2M0yL16E		String		商户appid下，某用户的openid
			paramMap.put("check_name", check_name);				//校验用户姓名选项	check_name			是	OPTION_CHECK						String		NO_CHECK：不校验真实姓名 
													 			//																						FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账） 
																//																						OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
			if (!WeinxinUtil.isStrEmpty(re_user_name))
				paramMap.put("re_user_name", re_user_name);		//收款用户姓名		re_user_name		可选	马花花								String		收款用户真实姓名。 如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填用户真实姓名
			paramMap.put("amount", amount);						//金额			amount				是	10099								int			企业付款金额，单位为分
			paramMap.put("desc", desc);							//企业付款描述信息	desc				是	理赔									String		企业付款操作说明信息。必填。
			paramMap.put("spbill_create_ip", spbill_create_ip);	//Ip地址			spbill_create_ip	是	192.168.0.1							String(32)	调用接口的机器Ip地址

			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);

			return requestContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
		}
	}
	
	/**
	 * 【企业付款】调用企业付款的（XML格式的）请求内容，返回响应信息（企业付款的第二部分）
	 * @param requestContent --String*-- （XML格式的）请求内容
	 * @return null 或 响应信息
	 */
	public String getResponseContentAfterTransfers(String requestContent) throws Exception{
		if (WeinxinUtil.isStrEmpty(requestContent))
			throw new Exception("非空参数存在空值");
		if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
			throw new Exception("商家证书相关信息存在空值");
		
		String responseContent = null;
		
		//定义临时变量
		String encoding = null;
		SpecialHttpsHelper httpsHelper = null;
		SSLContext sslContext = null;
		
		try {
			//获取编码格式
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			
			//创建（带证书的）HTTPS的帮助类对象
			httpsHelper = SpecialHttpsHelper.getInstance();
			
			//获取SSLContext对象
			sslContext = httpsHelper.getSSLContextByFiles(caCert, partnerCert, partnerCertPasswd);
			
			//调用请求，返回响应信息
			if (sslContext != null)
				responseContent = httpsHelper.doPost(WeixinConfig.getConfig("weixinpub.pay.urlForTransfers"), requestContent, encoding, sslContext);
			if (WeinxinUtil.isStrEmpty(responseContent))
				throw new Exception("无响应内容");
			
			return responseContent;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			httpsHelper = null;
			sslContext = null;
		}
	}
	
	/**
	 * 【企业付款】（用于企业向微信用户个人付款 ）
	 * @param partner_trade_no --String*-- 商户订单号（由getPartnerTradeNo()方法获取）
	 * @param openid --String*-- 用户openid（商户appid下，某用户的openid）
	 * @param check_name --String*-- 校验用户姓名选项（NO_CHECK：不校验真实姓名；FORCE_CHECK：强校验真实姓名[未实名认证的用户会校验失败，无法转账]；OPTION_CHECK：针对已实名认证的用户才校验真实姓名[未实名认证用户不校验，可以转账成功]）
	 * @param re_user_name --String-- 收款用户真实姓名（如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填）
	 * @param amount --Integer*-- 企业付款金额（单位：分）（金额区间在1.00元到20000.00元之间）
	 * @param desc --String*-- 企业付款描述信息
	 * @param spbill_create_ip --String*-- 调用接口的机器Ip地址（String(32)）
	 * @return null 或 返回结果集
	 * @throws Exception
	 */
	public SortedMap<String, String> transfers(String partner_trade_no, String openid, String check_name, String re_user_name, Integer amount, String desc, String spbill_create_ip)throws Exception{
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		
		try {
			if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
				throw new Exception("商家相关信息存在空值");
			if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
				throw new Exception("商家证书相关信息存在空值");
			
			//获取请求中的XML格式数据
			requestContent = getRequestContentForTransfers(partner_trade_no, openid, check_name, re_user_name, amount, desc, spbill_create_ip);
			
			//获取响应内容
			responseContent = getResponseContentAfterTransfers(requestContent);
			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){//SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
				tmpBuilder.append("【企业付款】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.append("【企业付款】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}
			
			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){//业务结果
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				return dataMap;
			}
			
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
		}
	}
	
	/**
	 * 【查询企业付款】（只支持查询30天内的订单）
	 * @param partner_trade_no --String*-- 商户调用企业付款API时使用的商户订单号
	 * @return null 或 返回结果集
	 * @throws Exception
	 */
	public SortedMap<String, String> getTransferInfo(String partner_trade_no)throws Exception{
		if (ToolUtil.isStrEmpty(appId) || ToolUtil.isStrEmpty(partnerId) || ToolUtil.isStrEmpty(partnerKey))
			throw new Exception("商家相关信息存在空值");
		if (WeinxinUtil.isStrEmpty(partner_trade_no))
			throw new Exception("非空参数存在空值");
		if (ToolUtil.isStrEmpty(caCert) || ToolUtil.isStrEmpty(partnerCert) || ToolUtil.isStrEmpty(partnerCertPasswd))
			throw new Exception("商家证书相关信息存在空值");
		
		SortedMap<String, String> dataMap = null;
		
		//定义临时变量
		String encoding = null;
		String nonceStr = null;
		SortedMap<String, Object> paramMap = null;
		String sign = null;
		String requestContent = null;
		String responseContent = null;
		StringBuilder tmpBuilder = null;
		SpecialHttpsHelper httpsHelper = null;
		SSLContext sslContext = null;
		
		try {
			//获取参数
			encoding = WeixinConfig.getConfig("weixinpub.pay.encoding");
			nonceStr = WeinxinUtil.getNoncestr();

			//收集参数
			paramMap = new TreeMap<String, Object>();
			paramMap.put("nonce_str", nonceStr);				//随机字符串		nonce_str			是		5K8264ILTKCH16CQ2502SI8ZNMTM67VS	String(32)	随机字符串，不长于32位
			paramMap.put("sign", "");							//签名			sign				是		C380BEC2BFD727A4B6845133519F3AD6	String(32)	生成签名方式查看3.2.1节
			paramMap.put("partner_trade_no", partner_trade_no);	//商户订单号		partner_trade_no	是		10000098201411111234567890			String(28)	商户调用企业付款API时使用的商户订单号
			paramMap.put("mch_id", partnerId);					//商户号			mch_id				是		10000098							String(32)	微信支付分配的商户号
			paramMap.put("appid", appId);						//Appid			appid				是		wxe062425f740d30d8					String(32)	商户号的appid
			
			//生成签名
			sign = WeinxinUtil.getSignBycreateLinkStringFromMap(paramMap, partnerKey, encoding);
			
			//生成XML格式的请求内容
			paramMap.put("sign", sign);
			requestContent = WeinxinUtil.getXmlContentBycreateLinkStringFromMap(paramMap);
			
			//创建（带证书的）HTTPS的帮助类对象
			httpsHelper = SpecialHttpsHelper.getInstance();
			
			//获取SSLContext对象
			sslContext = httpsHelper.getSSLContextByFiles(caCert, partnerCert, partnerCertPasswd);
			
			//调用请求，返回响应信息
			if (sslContext != null)
				responseContent = httpsHelper.doPost(WeixinConfig.getConfig("weixinpub.pay.urlForGetTransferInfo"), requestContent, encoding, sslContext);

			if (!WeinxinUtil.isStrEmpty(responseContent))
				dataMap = XMLUtil.doXMLParse(responseContent);
			if (dataMap == null || dataMap.isEmpty())
				throw new Exception("无响应内容");
			
			tmpBuilder = new StringBuilder();
			
			//验证接口是否调用成功
			if (!"SUCCESS".equals(dataMap.get("return_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询企业付款】调用接口失败：").append(dataMap.get("return_msg"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			////生成响应内容的签名并进行效验
			//if (!checkResponseSign(dataMap)){
			//	tmpBuilder.delete(0, tmpBuilder.length()).append("【查询企业付款】响应信息中的签名验证失败");
			//	if (dataMap != null)
			//		dataMap.clear();
			//	dataMap = null;
			//	throw new Exception(tmpBuilder.toString());
			//}

			//验证业务是否执行成功
			if (!"SUCCESS".equals(dataMap.get("result_code"))){
				tmpBuilder.delete(0, tmpBuilder.length()).append("【查询企业付款】业务执行失败，错误代码为").append(dataMap.get("err_code")).append("，错误描述：").append(dataMap.get("err_code_des"));
				if (dataMap != null)
					dataMap.clear();
				dataMap = null;
				throw new Exception(tmpBuilder.toString());
			}
			
			//返回
			return dataMap;
		} catch (Exception e){
			throw e;
		} finally {
			//清空
			encoding = null;
			nonceStr = null;
			if (paramMap != null)
				paramMap.clear();
			paramMap = null;
			sign = null;
			requestContent = null;
			responseContent = null;
			tmpBuilder = null;
			httpsHelper = null;
			sslContext = null;
		}
	}

	/**
	 * 验证响应内容的签名是否正确合法
	 * @param paramMap --SortedMap*-- 参数Map
	 * @return 验证结果，true表示合法
	 */
	public boolean checkResponseSign(SortedMap<String, String> paramMap){
		boolean checkOK = false;
		if (paramMap == null || paramMap.isEmpty())
			return checkOK;
		
		String sign = WeinxinUtil.getSignBycreateLinkString(paramMap, partnerKey, null);
		if (sign != null && sign.equals(paramMap.get("sign")))
			checkOK = true;
		sign = null;
		
		return checkOK;
	}
	
	/**
	 * 获取发放代金券、发放红包、企业付款给个人的商户订单号（28位字符）
	 *  说明：
	 * 	1、每个订单号必须唯一
	 * 	2、组成： mch_id+yyyymmdd+10位一天内不能重复的数字。
	 * 	3、接口根据商户订单号支持重入， 如出现超时可再调用。
	 * @return 商户订单号
	 */
	public String getPartnerTradeNo(){
		StringBuilder tmpBuilder = new StringBuilder();
		tmpBuilder.append(partnerId);//mch_id
		tmpBuilder.append(ToolUtil.getCurrentDate(ToolUtil.TIME_FORMAT_FOURTEEN));//yyyyMMddHHmmss
		tmpBuilder.append(ToolUtil.getRandomNumber(4));//4位随机数字串
		String trade_no = tmpBuilder.toString();
		
		tmpBuilder = null;
		
		return trade_no;
	}
	
}