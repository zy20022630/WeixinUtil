package test;

import java.util.List;

import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.tool.ServerIPTool;

public class TestServerIPTool {
	
	/**
	 * @param args	OK
	 */
	public static void main(String[] args) {
		test1();
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
			
			List<String> dataList = ServerIPTool.getInstance().gainServerIpList(accessToken);
			if (dataList == null)
				System.out.println("dataList = null");
			else {
				System.out.println("dataList.size() = " + dataList.size());
				for (String s : dataList){
					System.out.println(s);
				}
			}
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
}