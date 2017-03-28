package com.zy.weixin.json;

import java.util.List;

/**
 * 创建自定义菜单JSON
 * @author zy20022630
 */
public class MenuListJson {

	private List<MenuJson> button;//一级菜单数组，个数应为1~3个
	
	public MenuListJson() {
		super();
	}

	public List<MenuJson> getButton() {
		return button;
	}

	public void setButton(List<MenuJson> button) {
		this.button = button;
	}
	
}