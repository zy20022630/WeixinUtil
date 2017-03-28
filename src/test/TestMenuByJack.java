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

public class TestMenuByJack {

	public static void main(String[] args) {
		//queryMenuForWX();//查询菜单
		deleteMenuForWX();//删除菜单
		createMenuForWX();//创建菜单
		
		//createMenuDemo();
	}
	
	static void queryMenuForWX(){
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
	
	static void deleteMenuForWX(){
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

	static void createMenuForWX(){
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
			
			boolean isOK = MenuTool.getInstance().createMenu(accessToken, createMenuDemo());
			System.out.println(isOK);
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	static MenuListJson createMenuDemo(){
		List<MenuJson> menuList = null;
		MenuJson menuJson = null;
		List<MenuJson> subMenuList = null;
		MenuJson subMenuJson = null;
		
		menuList = new ArrayList<MenuJson>();
		
		//第一个一级菜单
		subMenuList = new ArrayList<MenuJson>();
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("view");
		subMenuJson.setName("我的订单");
		String myUrl = "http://dev.xxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/order/orderList.jsp";
		subMenuJson.setUrl(myUrl);
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		menuJson = new MenuJson();
		menuJson.setName("个人中心");
		menuJson.setSub_button(subMenuList);
		menuList.add(menuJson);

		//第二个一级菜单
		
		menuJson = new MenuJson();
		menuJson.setType("view");
		menuJson.setName("场馆预订");
		myUrl = "http://dev.xxxxxxxxxxxxxxxx.com.cn/XXXXXXXXX/jsp/stadium/stadiumList.jsp";
		menuJson.setUrl(myUrl);
		menuJson.setSub_button(new ArrayList<MenuJson>());
		menuList.add(menuJson);
		
		//第三个一级菜单		
		subMenuList = new ArrayList<MenuJson>();
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("view");
		subMenuJson.setName("体育用品");
		myUrl = "http://search.jd.com/Search?keyword=网球拍&enc=utf-8&suggest=1";
		subMenuJson.setUrl(myUrl);
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("view");
		subMenuJson.setName("客服");
		myUrl = "http://www.xxxxxxxxxxxxxxxx.com.cn/desk3.jsp";
		subMenuJson.setUrl(myUrl);
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("view");
		subMenuJson.setName("官网");
		myUrl = "http://www.xxxxxxxxxxxxxxxx.com.cn/";
		subMenuJson.setUrl(myUrl);
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		subMenuJson = new MenuJson();
		subMenuJson.setType("view");
		subMenuJson.setName("客户端");
		myUrl = "http://www.pgyer.com/xxxxxxxxxxxxxxx";
		subMenuJson.setUrl(myUrl);
		subMenuJson.setSub_button(new ArrayList<MenuJson>());
		subMenuList.add(subMenuJson);
		
		menuJson = new MenuJson();
		menuJson.setName("更多");
		menuJson.setSub_button(subMenuList);
		menuList.add(menuJson);

		
		MenuListJson createMenuJson = new MenuListJson();
		createMenuJson.setButton(menuList);
		
		String strJSON = JSON.toJSONString(createMenuJson);
		System.out.println(strJSON);
		
		return createMenuJson;
	}
}