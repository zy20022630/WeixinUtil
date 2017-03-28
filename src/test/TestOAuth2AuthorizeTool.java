package test;

import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.OAuth2AuthorizeJson;
import com.zy.weixin.json.OAuth2AuthorizeUserInfoJson;
import com.zy.weixin.tool.OAuth2AuthorizeTool;

public class TestOAuth2AuthorizeTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//test3();
		test1();
//		test6();
	}

	static void test1(){
		String appId = null;
		String redirectUrl = null;
		String scope = null;
		String state = null;
		String strURL = null;
		
		try {
			appId = "xxxxxxxxxxxxxxxxx";//微信公众平台测试账号KEY：xxxxxxxxxxxxxxxxx
			//redirectUrl = "http://dev.xxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/wechat/report/reportList.jsp";
			
			redirectUrl = "http://dev.xxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/wechat/allAboutMe/projectListOfJoin.jsp";
			
			scope = OAuth2AuthorizeTool.SCOPE_SNSAPI_BASE;
			scope = OAuth2AuthorizeTool.SCOPE_SNSAPI_USERINFO;
			state="ydb";
			
			strURL = OAuth2AuthorizeTool.getInstance().getAuthorizeUrl(appId, redirectUrl, scope, state);
			System.out.println(strURL);
			

			
//			appId = "";
//			redirectUrl = "";
//			scope = OAuth2AuthorizeTool.SCOPE_SNSAPI_USERINFO;
//			state="test1";
//			strURL = OAuth2AuthorizeTool.getInstance().getAuthorizeUrl(appId, redirectUrl, scope, state);
//			System.out.println(strURL);

		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
	static void test2(){
		String appid = null;
		String appsecret = null;
		String code = null;
		
		try {
			appid = "";
			appsecret = "";
			code = "";
			
			OAuth2AuthorizeJson oauth2AuthorizeJson = OAuth2AuthorizeTool.getInstance().getAccessTokenByCode(appid, appsecret, code);
			if (oauth2AuthorizeJson != null){
				System.out.println("access_token = " + oauth2AuthorizeJson.getAccess_token());
				System.out.println("expires_in = " + oauth2AuthorizeJson.getExpires_in());
				System.out.println("refresh_token = " + oauth2AuthorizeJson.getRefresh_token());
				System.out.println("openid = " + oauth2AuthorizeJson.getOpenid());
				System.out.println("scope = " + oauth2AuthorizeJson.getScope());
			} else 
				System.out.println("oauth2AuthorizeJson = null");
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
	static void test3(){
		String appId = null;
		String refreshToken = null;
		
		try {
			appId = "xxxxxxxxxxxxxxxxx";
			refreshToken = "OezXcEiiBSKSxW0eoylIeEdI1tp5hLipWiRT1WlThxcT6slQsQsywlGJjkUTc601kSFt-jDQsNN6PuuRnbh82DhdeVgQe-6kotLGYdNg0f4y6jbkXVpsXzZV1p2S1f45A_2ST7Mj9wGtsHMxcxEcHA";

/*
{
"openid":"ohOO1uH7WCmrLdVJXaRDzRblJ4SY",
"access_token":"OezXcEiiBSKSxW0eoylIeEdI1tp5hLipWiRT1WlThxcT6slQsQsywlGJjkUTc601z5DVT8Cp-ITn9TDVDUmhFoeL0aA3jKXquc1kVwoux23DlPlaFs_efWxHAsP9ujyTRaYyFhThVGpR3F7hzliBKg",
"expires_in":7200,
"refresh_token":"OezXcEiiBSKSxW0eoylIeEdI1tp5hLipWiRT1WlThxcT6slQsQsywlGJjkUTc601jWES6P2naVmUWGvERahNrvPSYSGU9cyCPffBCgcoy9kBqSEeuttlIoNsK1WwK3HY8SVp27vg-O55E3InNXbgGQ",
"scope":"snsapi_base,snsapi_userinfo,"
}
 */
			OAuth2AuthorizeJson oauth2AuthorizeJson = OAuth2AuthorizeTool.getInstance().getNewAccessTokenByRefresh(appId, refreshToken);
			if (oauth2AuthorizeJson != null){
				System.out.println("access_token = " + oauth2AuthorizeJson.getAccess_token());
				System.out.println("expires_in = " + oauth2AuthorizeJson.getExpires_in());
				System.out.println("refresh_token = " + oauth2AuthorizeJson.getRefresh_token());
				System.out.println("openid = " + oauth2AuthorizeJson.getOpenid());
				System.out.println("scope = " + oauth2AuthorizeJson.getScope());
			} else 
				System.out.println("oauth2AuthorizeJson = null");
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
	static void test4(){
		String appId = null;
		String refreshToken = null;
		String accessToken = null;
		String openId = null;
		
		try {
			appId = "xxxxxxxxxxxxxxxxx";
			refreshToken = "OezXcEiiBSKSxW0eoylIeEdI1tp5hLipWiRT1WlThxcT6slQsQsywlGJjkUTc601kSFt-jDQsNN6PuuRnbh82DhdeVgQe-6kotLGYdNg0f4y6jbkXVpsXzZV1p2S1f45A_2ST7Mj9wGtsHMxcxEcHA";

			OAuth2AuthorizeJson oauth2AuthorizeJson = OAuth2AuthorizeTool.getInstance().getNewAccessTokenByRefresh(appId, refreshToken);
			if (oauth2AuthorizeJson != null){
				System.out.println("access_token = " + oauth2AuthorizeJson.getAccess_token());
				System.out.println("expires_in = " + oauth2AuthorizeJson.getExpires_in());
				System.out.println("refresh_token = " + oauth2AuthorizeJson.getRefresh_token());
				System.out.println("openid = " + oauth2AuthorizeJson.getOpenid());
				System.out.println("scope = " + oauth2AuthorizeJson.getScope());
			} else {
				System.out.println("oauth2AuthorizeJson = null");
				return;
			}
			
			accessToken = oauth2AuthorizeJson.getAccess_token();
			openId = oauth2AuthorizeJson.getOpenid();
			
			OAuth2AuthorizeUserInfoJson userInfoJson = OAuth2AuthorizeTool.getInstance().getUserInfoByAccessToken(accessToken, openId, null);
			if (userInfoJson != null){
				System.out.println("openid = " + userInfoJson.getOpenid());
				System.out.println("nickname = " + userInfoJson.getNickname());
				System.out.println("sex = " + userInfoJson.getSex());
				System.out.println("province = " + userInfoJson.getProvince());
				System.out.println("city = " + userInfoJson.getCity());
				System.out.println("country = " + userInfoJson.getCountry());
				System.out.println("headimgurl = " + userInfoJson.getHeadimgurl());
				if (userInfoJson.getPrivilege() != null)
					System.out.println("privilege = " + userInfoJson.getPrivilege().size());
				else
					System.out.println("privilege is null");
				System.out.println("unionid = " + userInfoJson.getUnionid());
			} else 
				System.out.println("userInfoJson = null");
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
	static void test5(){
		String accessToken = null;
		String openId = null;
		
		try {
			accessToken = "";
			openId = "";
			
			boolean isOK = OAuth2AuthorizeTool.getInstance().checkAccessToken(accessToken, openId);
			System.out.println("isOK = " + isOK);
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
	static void test6(){
		String imgURL = "http://wx.qlogo.cn/mmopen/cnvTAZhAia1Oe4Ur8icw2ygVCwN3E9HAibHzA303S6svPDbrX6oKUWEupuWF9Qlhazjic1ZzM7B0T4OoTyeImfddcLl0tBJkTu0V/0";
		
		try {
			String prefixImgURL = imgURL.substring(0, imgURL.lastIndexOf("/") + 1);
			
			String dimImageURL = prefixImgURL.concat("132");
			String srcImageURL = prefixImgURL.concat("0");

			String toDimFilePath = "D:/Temp/PIC/test/dim11.jpg";
			String toSrcFilePath = "D:/Temp/PIC/test/src11.jpg";
			
			if (FileUtils.copyFileByURL(dimImageURL, toDimFilePath) && FileUtils.copyFileByURL(srcImageURL, toSrcFilePath)){
				System.out.println("OK");
			} else {
				System.out.println("ERR");
			}
			
			
//			URL dimURL = new URL(dimImageURL);
//			URL srcURL = new URL(srcImageURL);
//
//			File toDimFile = new File(toDimFilePath);
//			File toSrcFile = new File(toSrcFilePath);
//			
//			if (FileUtils.copyFileByURL(dimURL, toDimFile) && FileUtils.copyFileByURL(srcURL, toSrcFile)){
//				System.out.println("OK");
//			} else {
//				System.out.println("ERR");
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}