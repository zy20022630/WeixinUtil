package test;

import java.util.List;

import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.UserInfoJson;
import com.zy.weixin.json.UserListJson;
import com.zy.weixin.tool.UserTool;

public class TestUserTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
			
			String next_openid = "";
			UserListJson userListJson = UserTool.getInstance().getUserList(accessToken, next_openid);
			
			if (userListJson == null)
				System.out.println("userListJson = null");
			else {
				System.out.println("getTotal() = " + userListJson.getTotal());
				System.out.println("getCount() = " + userListJson.getCount());
				System.out.println("getNext_openid() = " + userListJson.getNext_openid());
				List<String> openidList = userListJson.getData().getOpenid();
				for (String openid : openidList){
					System.out.println(openid);
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
			
			String openId = null;
			String language = null;
			UserInfoJson userInfoJson = null;
			
			openId = "oJyoXuMgIMR-cZJ7eYNMiybqumF8";
			userInfoJson = UserTool.getInstance().gainUserInfo(accessToken, openId, language);
			if (userInfoJson == null)
				System.out.println("userInfoJson = null");
			else {
				System.out.println("subscribe = " + userInfoJson.getSubscribe());
				System.out.println("openid = " + userInfoJson.getOpenid());
				System.out.println("nickname = " + userInfoJson.getNickname());
				System.out.println("sex = " + userInfoJson.getSex());
				System.out.println("language = " + userInfoJson.getLanguage());
				System.out.println("city = " + userInfoJson.getCity());
				System.out.println("province = " + userInfoJson.getProvince());
				System.out.println("country = " + userInfoJson.getCountry());
				System.out.println("headimgurl = " + userInfoJson.getHeadimgurl());
				System.out.println("subscribe_time = " + userInfoJson.getSubscribe_time());
				System.out.println("unionid = " + userInfoJson.getUnionid());
				System.out.println("remark = " + userInfoJson.getRemark());
				System.out.println("groupid = " + userInfoJson.getGroupid());
			}
			
			openId = "oJyoXuEs9XJTbl7n84mMBlI12XIY";
			userInfoJson = UserTool.getInstance().gainUserInfo(accessToken, openId, language);
			if (userInfoJson == null)
				System.out.println("userInfoJson = null");
			else {
				System.out.println("subscribe = " + userInfoJson.getSubscribe());
				System.out.println("openid = " + userInfoJson.getOpenid());
				System.out.println("nickname = " + userInfoJson.getNickname());
				System.out.println("sex = " + userInfoJson.getSex());
				System.out.println("language = " + userInfoJson.getLanguage());
				System.out.println("city = " + userInfoJson.getCity());
				System.out.println("province = " + userInfoJson.getProvince());
				System.out.println("country = " + userInfoJson.getCountry());
				System.out.println("headimgurl = " + userInfoJson.getHeadimgurl());
				System.out.println("subscribe_time = " + userInfoJson.getSubscribe_time());
				System.out.println("unionid = " + userInfoJson.getUnionid());
				System.out.println("remark = " + userInfoJson.getRemark());
				System.out.println("groupid = " + userInfoJson.getGroupid());
			}
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static void test12(){
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
			
			String next_openid = "";
			UserListJson userListJson = UserTool.getInstance().getUserList(accessToken, next_openid);
			
			if (userListJson == null)
				System.out.println("userListJson = null");
			else {
				System.out.println("getTotal() = " + userListJson.getTotal());
				System.out.println("getCount() = " + userListJson.getCount());
				System.out.println("getNext_openid() = " + userListJson.getNext_openid());
				List<String> openidList = userListJson.getData().getOpenid();
				UserInfoJson userInfoJson = null;
				for (String openid : openidList){
					userInfoJson = UserTool.getInstance().gainUserInfo(accessToken, openid, null);
					if (userInfoJson == null)
						continue;
					
					if ("平".equals(userInfoJson.getNickname()) || "一路向前".equals(userInfoJson.getNickname())){
						System.out.println("subscribe = " + userInfoJson.getSubscribe());
						System.out.println("openid = " + userInfoJson.getOpenid());
						System.out.println("nickname = " + userInfoJson.getNickname());
						System.out.println("sex = " + userInfoJson.getSex());
						System.out.println("language = " + userInfoJson.getLanguage());
						System.out.println("city = " + userInfoJson.getCity());
						System.out.println("province = " + userInfoJson.getProvince());
						System.out.println("country = " + userInfoJson.getCountry());
						System.out.println("headimgurl = " + userInfoJson.getHeadimgurl());
						System.out.println("subscribe_time = " + userInfoJson.getSubscribe_time());
						System.out.println("unionid = " + userInfoJson.getUnionid());
						System.out.println();
					}
				}
			}
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	static void test3(){
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
			
			String openId = "ohOO1uGEOey0mXMqnNW5jJ7XgPpM";
			String remark = "小伍";
			boolean isOK = UserTool.getInstance().updateUserRemark(accessToken, openId, remark);
			
			System.out.println("isOK = " + isOK);
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}