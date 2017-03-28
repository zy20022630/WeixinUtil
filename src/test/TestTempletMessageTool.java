package test;

import com.alibaba.fastjson.JSONObject;
import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.TempletMessageJson;
import com.zy.weixin.tool.TempletMessageTool;

public class TestTempletMessageTool {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test1();
		test2();
	}
	
	static void test1(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			String openid = "ohOO1uH7WCmrLdVJXaRDzRblJ4SY";
			openid = "ohOO1uCCKtAVybXMq8_bCBB5Uytk";
			
			String template_id = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
			String url = "http://www.xxxxxxxxxxxxxxxxx.com.cn";
			String topcolor = "#ff0000";

			JSONObject first_json = new JSONObject();
			first_json.put("value", "我们已收到您的货款，开始为您打包商品，请耐心等待");
			first_json.put("color", "#173177");
			
			JSONObject orderMoneySum_json = new JSONObject();
			orderMoneySum_json.put("value", "30.00元");
			orderMoneySum_json.put("color", "#173177");
			
			JSONObject orderProductName_json = new JSONObject();
			orderProductName_json.put("value", "我是商品名字");
			orderProductName_json.put("color", "#173177");
			
			JSONObject Remark_json = new JSONObject();
			Remark_json.put("value", "如有问题请致电xxxxxxxxxxxxxx或直接在微信留言，XXXXXXXXXXX将第一时间为您服务！");
			Remark_json.put("color", "#173177");
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("first", first_json);
			jsonObject.put("orderMoneySum", orderMoneySum_json);
			jsonObject.put("orderProductName", orderProductName_json);
			jsonObject.put("Remark", Remark_json);

			String response = TempletMessageTool.getInstance().send(accessToken, openid, template_id, url, topcolor, jsonObject);
			if (response == null)
				System.out.println("response = null");
			else if (response.length() == 0)
				System.out.println("response.length() = 0");
			else {
				System.out.println("response：" + response);
				
				TempletMessageJson templetMessageJson = TempletMessageTool.getInstance().parseObjectForResponseOfSend(response);
				if (templetMessageJson == null)
					System.out.println("templetMessageJson = null");
				else {
					System.out.println("errcode = " + templetMessageJson.getErrcode());
					System.out.println("errmsg = " + templetMessageJson.getErrmsg());
					System.out.println("msgid = " + templetMessageJson.getMsgid());
				}
			}
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static void test2(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			String openid = "ohOO1uH7WCmrLdVJXaRDzRblJ4SY";
			openid = "ohOO1uCCKtAVybXMq8_bCBB5Uytk";
			
			String template_id = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
			String url = "http://www.xxxxxxxxxxxxxxxxx.com.cn";
			String topcolor = "#ff0000";

			JSONObject first_json = new JSONObject();
			first_json.put("value", "您好，XXXXXXXXXXX因场地维修导致无法正常打球，故退款。");
			first_json.put("color", "#173177");
			
			JSONObject reason_json = new JSONObject();
			reason_json.put("value", "XXXXXXXX主动撤单");
			reason_json.put("color", "#173177");
			
			JSONObject refund_json = new JSONObject();
			refund_json.put("value", "70元");
			refund_json.put("color", "#173177");
			
			JSONObject remark_json = new JSONObject();
			remark_json.put("value", "如有问题请致电xxx-xxx-xxxx或直接在微信留言，XXXXXXXXXX将第一时间为您服务！");
			remark_json.put("color", "#173177");
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("first", first_json);
			jsonObject.put("reason", reason_json);
			jsonObject.put("refund", refund_json);
			jsonObject.put("remark", remark_json);

			String response = TempletMessageTool.getInstance().send(accessToken, openid, template_id, url, topcolor, jsonObject);
			if (response == null)
				System.out.println("response = null");
			else if (response.length() == 0)
				System.out.println("response.length() = 0");
			else {
				System.out.println("response：" + response);
				
				TempletMessageJson templetMessageJson = TempletMessageTool.getInstance().parseObjectForResponseOfSend(response);
				if (templetMessageJson == null)
					System.out.println("templetMessageJson = null");
				else {
					System.out.println("errcode = " + templetMessageJson.getErrcode());
					System.out.println("errmsg = " + templetMessageJson.getErrmsg());
					System.out.println("msgid = " + templetMessageJson.getMsgid());
				}
			}
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}