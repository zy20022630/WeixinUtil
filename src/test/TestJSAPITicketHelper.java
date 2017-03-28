package test;

import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.JSAPITicketHelper;
import com.zy.weixin.common.WeiXinException;

public class TestJSAPITicketHelper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test1();
	}

	static void test1(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		String jsapi_ticket = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			System.out.println("accessToken = " + accessToken);
			
			if (accessToken != null){
				if (JSAPITicketHelper.getInstance().getCredential(accessToken)){
					jsapi_ticket = JSAPITicketHelper.getInstance().getJSAPITicket();
				}
			}

			System.out.println("jsapi_ticket = " + jsapi_ticket);
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}