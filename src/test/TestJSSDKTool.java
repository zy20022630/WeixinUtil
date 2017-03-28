package test;

import java.util.SortedMap;
import java.util.TreeMap;

import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.JSAPITicketHelper;
import com.zy.weixin.tool.JSSDKTool;

public class TestJSSDKTool {

	public static void main(String[] args){
		test1();
	}
	
	static void test(){
		System.out.println(JSSDKTool.getNoncestr());
		System.out.println(JSSDKTool.getNoncestr(18));
		System.out.println(JSSDKTool.getTimestamp());
	}
	
	static void test1(){
		String noncestr = null;
		String jsapi_ticket = null;
		long timestamp = JSSDKTool.getTimestamp();
		String url = null;
		
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		SortedMap<String, String> dataMap = null;
		String sign = null;
		
		try {
			noncestr = JSSDKTool.getNoncestr();
			url = "http://dev.xxxxxxxxx.com.cn/XXXXXXX/jsp/user/test2.jsp";
			
			appid = "xxxxxxxxxxxxxxxxx";
			appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			if (accessToken != null){
				if (JSAPITicketHelper.getInstance().getCredential(accessToken)){
					jsapi_ticket = JSAPITicketHelper.getInstance().getJSAPITicket();
				}
			}
			
			if (jsapi_ticket == null){
				System.out.println("jsapi_ticket is null");
				return;	
			}

//			noncestr = "Wm3WZYTPz0wzccnW";
//			jsapi_ticket = "sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg";
//			timestamp = 1414587457;
//			url = "http://mp.weixin.qq.com?params=value";
			
			System.out.println("url = " + url);
			System.out.println("noncestr = " + noncestr);
			System.out.println("timestamp = " + timestamp);
			System.out.println("jsapi_ticket = " + jsapi_ticket);
			
			dataMap = new TreeMap<String, String>();
			dataMap.put("url", url);
			dataMap.put("noncestr", noncestr);
			dataMap.put("timestamp", String.valueOf(timestamp));
			dataMap.put("jsapi_ticket", jsapi_ticket);

			sign = JSSDKTool.getSignForJSSDK(dataMap);
			System.out.println("sign = " + sign);
		
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}