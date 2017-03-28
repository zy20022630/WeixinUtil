package test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.MenuJson;
import com.zy.weixin.json.MenuListJson;
import com.zy.weixin.json.QueryMenuJson;
import com.zy.weixin.tool.MenuTool;
import com.zy.weixin.tool.OAuth2AuthorizeTool;

public class TestMenu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		test1();
//		test3();
//		test2();
//		createMenu();
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
			
			QueryMenuJson queryMenuJson = MenuTool.getInstance().getMenuList(accessToken);
			if (queryMenuJson != null)
				System.out.println(JSON.toJSONString(queryMenuJson));
			else
				System.out.println("createMenuJson = null");
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
			
			boolean isOK = MenuTool.getInstance().createMenu(accessToken, createMenu());
			System.out.println(isOK);
			
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
			
			boolean isOK = MenuTool.getInstance().deleteMenu(accessToken);
			System.out.println(isOK);
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	static MenuListJson createTestMenu(){
		
		List<MenuJson> menuList = null;
		MenuJson menuJson = null;
		List<MenuJson> subMenuList = null;
		MenuJson subMenuJson = null;
		
		menuList = new ArrayList<MenuJson>();
		
		//第一个一级菜单
		subMenuList = new ArrayList<MenuJson>();
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("scancode_waitmsg");
		subMenuJson.setName("扫码带提示");
		subMenuJson.setKey("rselfmenu_0_0");
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("scancode_push");
		subMenuJson.setName("扫码推事件");
		subMenuJson.setKey("rselfmenu_0_1");
		subMenuList.add(subMenuJson);
		
		menuJson = new MenuJson();
		menuJson.setName("扫码");
		menuJson.setSub_button(subMenuList);
		menuList.add(menuJson);

		//第二个一级菜单
		subMenuList = new ArrayList<MenuJson>();
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("pic_sysphoto");
		subMenuJson.setName("系统拍照发图");
		subMenuJson.setKey("rselfmenu_1_0");
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("pic_photo_or_album");
		subMenuJson.setName("拍照或者相册发图");
		subMenuJson.setKey("rselfmenu_1_1");
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("pic_weixin");
		subMenuJson.setName("微信相册发图");
		subMenuJson.setKey("rselfmenu_1_2");
		subMenuList.add(subMenuJson);
		
		menuJson = new MenuJson();
		menuJson.setName("发图");
		menuJson.setSub_button(subMenuList);
		menuList.add(menuJson);
		
		//第三个一级菜单		
		menuJson = new MenuJson();
		menuJson.setType("location_select");
		menuJson.setName("发送位置");
		menuJson.setKey("rselfmenu_2_0");
		menuList.add(menuJson);

		
		MenuListJson createMenuJson = new MenuListJson();
		createMenuJson.setButton(menuList);
		
		String strJSON = JSON.toJSONString(createMenuJson);
		System.out.println(strJSON);
		
		return createMenuJson;
	}
	
	static MenuListJson createMenu(){
		List<MenuJson> menuList = null;
		MenuJson menuJson = null;
		List<MenuJson> subMenuList = null;
		MenuJson subMenuJson = null;
		
		menuList = new ArrayList<MenuJson>();
		
		//第一个一级菜单
		subMenuList = new ArrayList<MenuJson>();
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("联系方式");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("关于团队");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		menuJson = new MenuJson();
		menuJson.setName("官网");
		menuJson.setSub_button(subMenuList);
		menuList.add(menuJson);

		//第二个一级菜单
		subMenuList = new ArrayList<MenuJson>();
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("产品介绍");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("ios下载");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("安卓下载");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		try {
			String appid = "xxxxxxxxxxxxxxxx";
			//TODO
			//TODO
			subMenuJson = new MenuJson();
			subMenuJson.setType("view");
			subMenuJson.setName("支付一");
			String redirectUrl = "http://dev.xxxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/test/test1.jsp?key9=value9&key5=value5&key2=value2&key7=value7";
			subMenuJson.setUrl(OAuth2AuthorizeTool.getInstance().getAuthorizeUrl(appid, redirectUrl, OAuth2AuthorizeTool.SCOPE_SNSAPI_BASE, null));
			//subMenuJson.setUrl(redirectUrl);
			subMenuJson.setSub_button(new ArrayList<MenuJson>());
			subMenuList.add(subMenuJson);
			
			System.out.println(subMenuJson.getUrl());
			
			subMenuJson = new MenuJson();
			subMenuJson.setType("view");
			subMenuJson.setName("支付二");
			redirectUrl = "http://dev.xxxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/test/test2.jsp?key9=value9&key5=value5&key2=value2&key7=value7&showwxpaytitle=1";
			//subMenuJson.setUrl(OAuth2AuthorizeTool.getInstance().getAuthorizeUrl(appid, redirectUrl, OAuth2AuthorizeTool.SCOPE_SNSAPI_USERINFO, "test32"));
			subMenuJson.setUrl(redirectUrl);
			subMenuJson.setSub_button(new ArrayList<MenuJson>());
			subMenuList.add(subMenuJson);
			
			System.out.println(subMenuJson.getUrl());
			
			//TODO
			//TODO
		} catch (Exception e) {
			e.printStackTrace();
		}

		menuJson = new MenuJson();
		menuJson.setName("产品");
		menuJson.setSub_button(subMenuList);
		menuList.add(menuJson);
		
		//第三个一级菜单		
		subMenuList = new ArrayList<MenuJson>();
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("最新活动");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("客服");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("click");
		subMenuJson.setName("论坛");
		subMenuJson.setKey("#");
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		try {
			String appid = "xxxxxxxxxxxxxxxx";
			//TODO
			//TODO
			subMenuJson = new MenuJson();
			subMenuJson.setType("view");
			subMenuJson.setName("base");
			String redirectUrl = "http://dev.xxxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/test/test1.jsp?key9=value9&key5=value5&key2=value2&key7=value7&showwxpaytitle=1";
			subMenuJson.setUrl(OAuth2AuthorizeTool.getInstance().getAuthorizeUrl(appid, redirectUrl, OAuth2AuthorizeTool.SCOPE_SNSAPI_BASE, null));
			subMenuJson.setSub_button(new ArrayList<MenuJson>());
			subMenuList.add(subMenuJson);
			
			System.out.println(subMenuJson.getUrl());
			
			subMenuJson = new MenuJson();
			subMenuJson.setType("view");
			subMenuJson.setName("userinfo");
			redirectUrl = "http://dev.xxxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/test/test2.jsp?key9=value9&key5=value5&key2=value2&key7=value7&showwxpaytitle=1";
			subMenuJson.setUrl(OAuth2AuthorizeTool.getInstance().getAuthorizeUrl(appid, redirectUrl, OAuth2AuthorizeTool.SCOPE_SNSAPI_USERINFO, "test32"));
			subMenuJson.setSub_button(new ArrayList<MenuJson>());
			subMenuList.add(subMenuJson);
			
			System.out.println(subMenuJson.getUrl());
			
			//TODO
			//TODO
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		menuJson = new MenuJson();
		menuJson.setName("互动");
		menuJson.setSub_button(subMenuList);
		menuList.add(menuJson);

		
		MenuListJson createMenuJson = new MenuListJson();
		createMenuJson.setButton(menuList);
		
		String strJSON = JSON.toJSONString(createMenuJson);
		System.out.println(strJSON);
		
		return createMenuJson;
	}
	
	/*
{
    "menu": 
{
    "button": [
        {
            "name": "官网",
            "sub_button": [
                {
                    "key": "#",
                    "name": "联系方式",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "关于团队",
                    "sub_button": [],
                    "type": "click"
                }
            ]
        },
        {
            "name": "产品",
            "sub_button": [
                {
                    "key": "#",
                    "name": "产品介绍",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "ios下载",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "安卓下载",
                    "sub_button": [],
                    "type": "click"
                }
            ]
        },
        {
            "name": "互动",
            "sub_button": [
                {
                    "key": "#",
                    "name": "最新活动",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "客服",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "论坛",
                    "sub_button": [],
                    "type": "click"
                }
            ]
        }
    ]
}
    
}
	 */
/*
{
    "button": [
        {
            "name": "官网",
            "sub_button": [
                {
                    "key": "#",
                    "name": "联系方式",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "关于团队",
                    "sub_button": [],
                    "type": "click"
                }
            ]
        },
        {
            "name": "产品",
            "sub_button": [
                {
                    "key": "#",
                    "name": "产品介绍",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "ios下载",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "安卓下载",
                    "sub_button": [],
                    "type": "click"
                }
            ]
        },
        {
            "name": "互动",
            "sub_button": [
                {
                    "key": "#",
                    "name": "最新活动",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "客服",
                    "sub_button": [],
                    "type": "click"
                },
                {
                    "key": "#",
                    "name": "论坛",
                    "sub_button": [],
                    "type": "click"
                }
            ]
        }
    ]
}
 */
}