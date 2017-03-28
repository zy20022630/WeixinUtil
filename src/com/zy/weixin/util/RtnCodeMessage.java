package com.zy.weixin.util;

import java.util.Map;
import java.util.HashMap;

/**
 * 全局返回码
 * @author zy20022630
 */
public class RtnCodeMessage {
	
	private static Map<Integer, String> messageMap = null;//KEY-回复码，VALUE-对应信息
	
	private static final String STR_EMPTY = "";
	
	static{
		if (messageMap == null){
			messageMap = new HashMap<Integer, String>();
			
			messageMap.put(Integer.valueOf(-1), "系统繁忙");
			messageMap.put(Integer.valueOf(0), "请求成功");
			messageMap.put(Integer.valueOf(40001), "获取access_token时AppSecret错误，或者access_token无效");
			messageMap.put(Integer.valueOf(40002), "不合法的凭证类型");
			messageMap.put(Integer.valueOf(40003), "不合法的OpenID");
			messageMap.put(Integer.valueOf(40004), "不合法的媒体文件类型");
			messageMap.put(Integer.valueOf(40005), "不合法的文件类型");
			messageMap.put(Integer.valueOf(40006), "不合法的文件大小");
			messageMap.put(Integer.valueOf(40007), "不合法的媒体文件id");
			messageMap.put(Integer.valueOf(40008), "不合法的消息类型");
			messageMap.put(Integer.valueOf(40009), "不合法的图片文件大小");
			messageMap.put(Integer.valueOf(40010), "不合法的语音文件大小");
			messageMap.put(Integer.valueOf(40011), "不合法的视频文件大小");
			messageMap.put(Integer.valueOf(40012), "不合法的缩略图文件大小");
			messageMap.put(Integer.valueOf(40013), "不合法的APPID");
			messageMap.put(Integer.valueOf(40014), "不合法的access_token");
			messageMap.put(Integer.valueOf(40015), "不合法的菜单类型");
			messageMap.put(Integer.valueOf(40016), "不合法的按钮个数");
			messageMap.put(Integer.valueOf(40017), "不合法的按钮个数");
			messageMap.put(Integer.valueOf(40018), "不合法的按钮名字长度");
			messageMap.put(Integer.valueOf(40019), "不合法的按钮KEY长度");
			messageMap.put(Integer.valueOf(40020), "不合法的按钮URL长度");
			messageMap.put(Integer.valueOf(40021), "不合法的菜单版本号");
			messageMap.put(Integer.valueOf(40022), "不合法的子菜单级数");
			messageMap.put(Integer.valueOf(40023), "不合法的子菜单按钮个数");
			messageMap.put(Integer.valueOf(40024), "不合法的子菜单按钮类型");
			messageMap.put(Integer.valueOf(40025), "不合法的子菜单按钮名字长度");
			messageMap.put(Integer.valueOf(40026), "不合法的子菜单按钮KEY长度");
			messageMap.put(Integer.valueOf(40027), "不合法的子菜单按钮URL长度");
			messageMap.put(Integer.valueOf(40028), "不合法的自定义菜单使用用户");
			messageMap.put(Integer.valueOf(40029), "不合法的oauth_code");
			messageMap.put(Integer.valueOf(40030), "不合法的refresh_token");
			messageMap.put(Integer.valueOf(40031), "不合法的openid列表");
			messageMap.put(Integer.valueOf(40032), "不合法的openid列表长度");
			messageMap.put(Integer.valueOf(40033), "不合法的请求字符，不能包含\\uxxxx格式的字符");
			messageMap.put(Integer.valueOf(40035), "不合法的参数");
			messageMap.put(Integer.valueOf(40038), "不合法的请求格式");
			messageMap.put(Integer.valueOf(40039), "不合法的URL长度");
			messageMap.put(Integer.valueOf(40050), "不合法的分组id");
			messageMap.put(Integer.valueOf(40051), "分组名字不合法");
			messageMap.put(Integer.valueOf(41001), "缺少access_token参数");
			messageMap.put(Integer.valueOf(41002), "缺少appid参数");
			messageMap.put(Integer.valueOf(41003), "缺少refresh_token参数");
			messageMap.put(Integer.valueOf(41004), "缺少secret参数");
			messageMap.put(Integer.valueOf(41005), "缺少多媒体文件数据");
			messageMap.put(Integer.valueOf(41006), "缺少media_id参数");
			messageMap.put(Integer.valueOf(41007), "缺少子菜单数据");
			messageMap.put(Integer.valueOf(41008), "缺少oauth code");
			messageMap.put(Integer.valueOf(41009), "缺少openid");
			messageMap.put(Integer.valueOf(42001), "access_token超时");
			messageMap.put(Integer.valueOf(42002), "refresh_token超时");
			messageMap.put(Integer.valueOf(42003), "oauth_code超时");
			messageMap.put(Integer.valueOf(43001), "需要GET请求");
			messageMap.put(Integer.valueOf(43002), "需要POST请求");
			messageMap.put(Integer.valueOf(43003), "需要HTTPS请求");
			messageMap.put(Integer.valueOf(43004), "需要接收者关注");
			messageMap.put(Integer.valueOf(43005), "需要好友关系");
			messageMap.put(Integer.valueOf(44001), "多媒体文件为空");
			messageMap.put(Integer.valueOf(44002), "POST的数据包为空");
			messageMap.put(Integer.valueOf(44003), "图文消息内容为空");
			messageMap.put(Integer.valueOf(44004), "文本消息内容为空");
			messageMap.put(Integer.valueOf(45001), "多媒体文件大小超过限制");
			messageMap.put(Integer.valueOf(45002), "消息内容超过限制");
			messageMap.put(Integer.valueOf(45003), "标题字段超过限制");
			messageMap.put(Integer.valueOf(45004), "描述字段超过限制");
			messageMap.put(Integer.valueOf(45005), "链接字段超过限制");
			messageMap.put(Integer.valueOf(45006), "图片链接字段超过限制");
			messageMap.put(Integer.valueOf(45007), "语音播放时间超过限制");
			messageMap.put(Integer.valueOf(45008), "图文消息超过限制 ");
			messageMap.put(Integer.valueOf(45009), "接口调用超过限制");
			messageMap.put(Integer.valueOf(45010), "创建菜单个数超过限制");
			messageMap.put(Integer.valueOf(45015), "回复时间超过限制");
			messageMap.put(Integer.valueOf(45016), "系统分组，不允许修改");
			messageMap.put(Integer.valueOf(45017), "分组名字过长");
			messageMap.put(Integer.valueOf(45018), "分组数量超过上限");
			messageMap.put(Integer.valueOf(46001), "不存在媒体数据");
			messageMap.put(Integer.valueOf(46002), "不存在的菜单版本");
			messageMap.put(Integer.valueOf(46003), "不存在的菜单数据");
			messageMap.put(Integer.valueOf(46004), "不存在的用户");
			messageMap.put(Integer.valueOf(47001), "解析JSON/XML内容错误");
			messageMap.put(Integer.valueOf(48001), "api功能未授权");
			messageMap.put(Integer.valueOf(50001), "用户未授权该api");
			messageMap.put(Integer.valueOf(61451), "参数错误(invalid parameter)");
			messageMap.put(Integer.valueOf(61452), "无效客服账号(invalid kf_account)");
			messageMap.put(Integer.valueOf(61453), "客服帐号已存在(kf_account exsited)");
			messageMap.put(Integer.valueOf(61454), "客服帐号名长度超过限制(仅允许10个英文字符，不包括@及@后的公众号的微信号)(invalid kf_acount length)");
			messageMap.put(Integer.valueOf(61455), "客服帐号名包含非法字符(仅允许英文+数字)(illegal character in kf_account)");
			messageMap.put(Integer.valueOf(61456), "客服帐号个数超过限制(10个客服账号)(kf_account count exceeded)");
			messageMap.put(Integer.valueOf(61457), "无效头像文件类型(invalid file type)");
			messageMap.put(Integer.valueOf(61450), "系统错误(system error)");
			messageMap.put(Integer.valueOf(61500), "日期格式错误");
			messageMap.put(Integer.valueOf(61501), "日期范围错误");
		}
	}
	
	/**
	 * 根据返回码，获取对应信息
	 * @param code --int*-- 全局返回码
	 * @return 信息字符串
	 */
	public static String getMessage(int code){
		if (messageMap == null)
			return STR_EMPTY;
		String message = messageMap.get(Integer.valueOf(code));
		if (message == null)
			return STR_EMPTY;
		return message;
	}
}