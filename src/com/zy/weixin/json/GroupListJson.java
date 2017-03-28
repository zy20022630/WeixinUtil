package com.zy.weixin.json;

import java.util.List;

/**
 * 获取分组列表JSON
 * @author zy20022630
 */
public class GroupListJson {

	private List<GroupInfoJson> groups;

	public GroupListJson() {
		super();
	}

	public List<GroupInfoJson> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupInfoJson> groups) {
		this.groups = groups;
	}
}