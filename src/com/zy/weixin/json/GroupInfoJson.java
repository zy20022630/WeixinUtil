package com.zy.weixin.json;

/**
 * 获取分组信息JSON
 * @author zy20022630
 */
public class GroupInfoJson {
	
	private long id;	//分组id，由微信分配 
	private String name;	//分组名字，UTF8编码
	private long count;	//分组内用户数量
	
	public GroupInfoJson() {
		super();
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}