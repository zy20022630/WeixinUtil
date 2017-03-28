package test;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import com.zy.weixin.pay.WeixinPubPayTool;

public class TestWeixinPubPayTool {
	
	private static String appId = "xxxxxxxxxxxxxxxxxx";
	private static String partnerId = "xxxxxxxxxx";
	private static String partnerKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	
	private static String caCert = "C:/weixin/certificate/pub/rootca.pem";
	private static String partnerCert = "C:/weixin/certificate/pub/xxxxxxxxxx.p12";
	private static String partnerCertPasswd = "xxxxxxxxxxxxxxxxxxxxxx";
	
	public static void main(String[] args) {
		try {
			////测试：【统一下单】	OK
			//getWeixinPubPayRequestParamMapForJSAPI();

			////测试：【查询订单】	OK
			//queryOrder();
			
			////测试：【关闭订单】	OK
			//closeOrder();
			
			////测试：【申请退款】	OK
			//refund();
			
			////测试：【查询退款】	OK
			//queryRefund();
			
			////测试：【下载对账单】	OK
			//downloadbill();
			
			////测试：【发放代金券】	OK
			//sendCoupon();
			
			////测试：【查询代金券批次信息】	OK
			//queryCouponStock();
			
			////测试：【查询代金券信息】	OK
			//queryCouponsInfo();
			
			////测试：【发放普通红包】	OK
			//sendRedPack();
			
			////测试：【发放裂变红包】	OK
			//sendGroupRedPack();
			
			////测试：【查询红包记录】	OK
			//queryRedPack();
			
			////测试：【企业付款】	OK
			//transfers();
			
			////测试：【查询企业付款】	OK
			//getTransferInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试：【统一下单】
	 * @throws Exception
	 */
	static void getWeixinPubPayRequestParamMapForJSAPI() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String clientIP = null;
		String notifyUrl = null;
		String openid = null;
		String orderUID = null;
		String subject = null;
		String detail = null;
		String totalFee = null;
		
		clientIP = "192.168.1.1";
		notifyUrl = "http://www.xxxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/pay/weixin/pubPayNotifyUrl.jsp";
		openid = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		orderUID = "T1703271535151308453525";
		subject = "20170327预订[XXXXXXXXXXXXXXXX]1片XXXXXXXXXX";
		detail = "20170327预订[XXXXXXXXXXXXXXXX]1片XXXXXXXXXX：[室外][01]15:00-16:00";
		totalFee = "30.00";

		String result = weixinPubPayTool.getWeixinPubPayRequestParamMapForJSAPI(clientIP, notifyUrl, openid, orderUID, subject, detail, totalFee);
		System.out.println("result：" + result);
	}
	
	/**
	 * 测试：【查询订单】
	 * @throws Exception
	 */
	static void queryOrder() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String transaction_id = null;
		String out_trade_no = null;
		
		transaction_id = null;
		out_trade_no = "T1703271322368209517365";
		
		transaction_id = "4004411111111111111111111";
		out_trade_no = "T1703241138051308453531";
		
		transaction_id = null;
		out_trade_no = "T1703271535151308453525";
		
		SortedMap<String, String> resultMap = weixinPubPayTool.queryOrder(transaction_id, out_trade_no);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}
	
	/**
	 * 测试：【关闭订单】
	 * @throws Exception
	 */
	static void closeOrder() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String out_trade_no = null;
		
		out_trade_no = "T1703271322368209517365";
		//out_trade_no = "T1703270937101494309291";
		
		boolean isOK = weixinPubPayTool.closeOrder(out_trade_no);
		System.out.println("isOK = " + isOK);
	}

	/**
	 * 测试：【申请退款】
	 * @throws Exception
	 */
	static void refund() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String transaction_id = null;
		String out_trade_no = null;
		String out_refund_no = null;
		long total_fee = 0;
		long refund_fee = 0;
		
		transaction_id = "4004411111111111111111111";
		out_trade_no = "T1703241138051308453531";
		out_refund_no = "2017032711111111111";
		total_fee = 1000;
		refund_fee = 1000;
		
		SortedMap<String, String> resultMap = weixinPubPayTool.refund(transaction_id, out_trade_no, out_refund_no, total_fee, refund_fee);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}
	
	/**
	 * 测试：【查询退款】
	 * @throws Exception
	 */
	static void queryRefund() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String transaction_id = null;
		String out_trade_no = null;
		String out_refund_no = null;
		String refund_id = null;
		
		transaction_id = "4004411111111111111111111";
		out_trade_no = "T1703241138051308453531";
		out_refund_no = "2017032711111111111";
		refund_id = "200123200120170327011111111111111";
		
		SortedMap<String, String> resultMap = weixinPubPayTool.queryRefund(transaction_id, out_trade_no, out_refund_no, refund_id);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}
	
	/**
	 * 测试：【下载对账单】
	 * @throws Exception
	 */
	static void downloadbill() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String bill_date = null;
		String bill_type = null;
		
		bill_date = "20170326";
		bill_type = "ALL";
		
		List<String> contentList = weixinPubPayTool.downloadbill(bill_date, bill_type);
		if (contentList == null)
			System.out.println("contentList is null");
		else if (contentList.isEmpty())
			System.out.println("contentList is empty");
		else{
			System.out.println("------------contentList元素------------");
			for (String lineContent : contentList){
				System.out.println(lineContent);
			}
		}
	}
	
	/**
	 * 测试：【发放代金券】
	 * @throws Exception
	 */
	static void sendCoupon() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String coupon_stock_id = null;
		String partner_trade_no = null;
		String openid = null;
		
		partner_trade_no = weixinPubPayTool.getPartnerTradeNo();
		coupon_stock_id = "1451111";//代金券批次号：1451111
		openid = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		SortedMap<String, String> resultMap = weixinPubPayTool.sendCoupon(coupon_stock_id, partner_trade_no, openid);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}
	
	/**
	 * 测试：【查询代金券批次信息】
	 * @throws Exception
	 */
	static void queryCouponStock() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String coupon_stock_id = "1451111";//代金券批次号：1451111
		
		SortedMap<String, String> resultMap = weixinPubPayTool.queryCouponStock(coupon_stock_id);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}

	/**
	 * 测试：【查询代金券信息】
	 * @throws Exception
	 */
	static void queryCouponsInfo() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String coupon_id = null;
		String openid = null;
		String stock_id = null;
		
		coupon_id = "1000000000";
		openid = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		stock_id = "1451111";//代金券批次号：1451111
		
		SortedMap<String, String> resultMap = weixinPubPayTool.queryCouponsInfo(coupon_id, openid, stock_id);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}

	/**
	 * 测试：【发放普通红包】
	 * @throws Exception
	 */
	static void sendRedPack() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String mch_billno = null;
		String send_name = null;
		String re_openid = null;
		Integer total_amount = null;
		String wishing = null;
		String client_ip = null;
		String act_name = null;
		String remark = null;
		String scene_id = null;
		
		mch_billno = weixinPubPayTool.getPartnerTradeNo();
		send_name = "XXXXXXXXXXXXXXXX";
		re_openid = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		total_amount = 100;
		wishing = "感谢您参加猜灯谜活动，祝您元宵节快乐！";
		client_ip = "192.168.1.1";//
		act_name = "猜灯谜抢红包活动";
		remark = "猜越多得越多，快来抢！";
		scene_id = "PRODUCT_2";
		
		SortedMap<String, String> resultMap = weixinPubPayTool.sendRedPack(mch_billno, send_name, re_openid, total_amount, wishing, client_ip, act_name, remark, scene_id);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}

	/**
	 * 测试：【发放裂变红包】
	 * @throws Exception
	 */
	static void sendGroupRedPack() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String mch_billno = null;
		String send_name = null;
		String re_openid = null;
		Integer total_amount = null;
		Integer total_num = null;
		String wishing = null;
		String act_name = null;
		String remark = null;
		String scene_id = null;
		
		mch_billno = weixinPubPayTool.getPartnerTradeNo();
		send_name = "XXXXXXXXXXXXXX";
		re_openid = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		total_amount = 300;
		total_num = 3;
		wishing = "感谢您参加猜灯谜活动，祝您元宵节快乐！";
		act_name = "猜灯谜抢红包活动";
		remark = "猜越多得越多，快来抢！";
		scene_id = "PRODUCT_2";

		SortedMap<String, String> resultMap = weixinPubPayTool.sendGroupRedPack(mch_billno, send_name, re_openid, total_amount, total_num, wishing, act_name, remark, scene_id);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}

	/**
	 * 测试：【查询红包记录】
	 * @throws Exception
	 */
	static void queryRedPack() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String mch_billno = "1000000000201703241712511111";
		
		SortedMap<String, String> resultMap = weixinPubPayTool.queryRedPack(mch_billno);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}

	/**
	 * 测试：【企业付款】
	 * @throws Exception
	 */
	static void transfers() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		
		String partner_trade_no = null;
		String openid = null;
		String check_name = null;
		String re_user_name = null;
		Integer amount = null;
		String desc = null;
		String spbill_create_ip = null;
		
		partner_trade_no = weixinPubPayTool.getPartnerTradeNo();
		openid = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";//
		check_name = "FORCE_CHECK";
		re_user_name = "XXXX";
		amount = 100;
		desc = "接口测试2";
		spbill_create_ip = "192.168.1.1";

		SortedMap<String, String> resultMap = weixinPubPayTool.transfers(partner_trade_no, openid, check_name, re_user_name, amount, desc, spbill_create_ip);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}

	/**
	 * 测试：【查询企业付款】
	 * @throws Exception
	 */
	static void getTransferInfo() throws Exception {
		WeixinPubPayTool weixinPubPayTool = getWeixinPubPayToolInstance();
		String partner_trade_no = "1000000000201703241725511111";
		
		SortedMap<String, String> resultMap = weixinPubPayTool.getTransferInfo(partner_trade_no);
		if (resultMap == null)
			System.out.println("resultMap is null");
		else if (resultMap.isEmpty())
			System.out.println("resultMap is empty");
		else{
			System.out.println("------------resultMap元素------------");
			Iterator<String> iterator = resultMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()){
				key = iterator.next();
				value = resultMap.get(key);
				System.out.println(((key == null) ? "null" : key) + "=" + ((value == null) ? "null" : value));
			}
			key = null;
			value = null;
		}
	}
	
	
	/**
	 * 获取 微信公众号的支付工具类 的对象
	 */
	static WeixinPubPayTool getWeixinPubPayToolInstance() throws Exception {
		WeixinPubPayTool weixinPubPayTool = WeixinPubPayTool.getInstance();
		weixinPubPayTool.setPartnerInfo(appId, partnerId, partnerKey);
		weixinPubPayTool.setPartnerCertInfo(caCert, partnerCert, partnerCertPasswd);
		return weixinPubPayTool;
	}
	
}