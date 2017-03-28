package com.zy.weixin.json;


/**
 * 获取关注者列表成功时的JSON
 * @author zy20022630
 */
public class UserListJson {
	
	private long total;			//关注该公众账号的总用户数
	private long count;			//拉取的OPENID个数，最大值为10000
	private UserOpenIdJson data;//列表数据，OPENID的列表
	private String next_openid;	//拉取列表的后一个用户的OPENID
	
	public UserListJson() {
		super();
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public UserOpenIdJson getData() {
		return data;
	}

	public void setData(UserOpenIdJson data) {
		this.data = data;
	}

	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}