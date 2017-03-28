package com.zy.weixin.tool;

import java.util.List;

import com.zy.weixin.json.ItemMessageJson;
import com.zy.weixin.json.MusicMessageJson;
import com.zy.weixin.util.ToolUtil;

/**
 * 创建(被动)发送消息的工具类
 * @author zy20022630
 */
public class MessageBuilderTool {

	/**
	 * 获取整个消息XML字符串
	 * @param openId --String*-- 消息接收人(即关注人员)OpenID
	 * @param developId --String*-- 开发者微信号
	 * @param type --int*-- 消息类型(0-文本，1-图片，2-语音, 3-视频 , 4-音乐，5-图文)
	 * @param diffPartXmlMsg --String*-- 消息差异部分XML字符串
	 * @return null 或 完整消息的XML格式字符串
	 */
	public static String getWholeXmlMessage(String openId, String developId, int type, String diffPartXmlMsg){
		if (ToolUtil.isStrEmpty(openId) || ToolUtil.isStrEmpty(developId) || ToolUtil.isStrEmpty(diffPartXmlMsg))
			return null;
		if (type < 0 || type > 5)
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<xml>")
				 .append(getCommonPartXmlMsg(openId, developId, type))
				 .append(diffPartXmlMsg)
				 .append("</xml>");
		String tmpStr = tmpBuffer.toString();

		//清空
		tmpBuffer = null;

		return tmpStr;
	}

	/**
	 * 创建【文本消息】的差异部分XML字符串
	 * @param content --String*-- 回复的消息内容
	 * @return null 或 XML格式字符串
	 */
	public static String getTextDiffPartXmlMsg(String content){
		if (ToolUtil.isStrEmpty(content))
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<Content><![CDATA[").append(content).append("]]></Content>");
		String tmpStr = tmpBuffer.toString();

		//清空
		tmpBuffer = null;
		
		return tmpStr;
/*
<Content><![CDATA[你好]]></Content>
 */
	}
	
	/**
	 * 创建【图片消息】的差异部分XML字符串<BR/>
	 * @param mediaId --String*-- 图片多媒体文件ID
	 * @return null 或 XML格式字符串
	 */
	public static String getImageDiffPartXmlMsg(String mediaId){
		if (ToolUtil.isStrEmpty(mediaId))
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<Image>")
				 .append("<MediaId><![CDATA[").append(mediaId).append("]]></MediaId>")
				 .append("</Image>");
		String tmpStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;

		return tmpStr;
/*
<Image>
<MediaId><![CDATA[media_id]]></MediaId>
</Image>
*/
	}
	
	/**
	 * 创建【语音消息】的差异部分XML字符串<BR/>
	 * @param mediaId --String*-- 语音多媒体文件ID
	 * @return null 或 XML格式字符串
	 */
	public static String getVoiceDiffPartXmlMsg(String mediaId){
		if (ToolUtil.isStrEmpty(mediaId))
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<Voice>")
				 .append("<MediaId><![CDATA[").append(mediaId).append("]]></MediaId>")
				 .append("</Voice>");
		String tmpStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;

		return tmpStr;
/*
<Voice>
<MediaId><![CDATA[media_id]]></MediaId>
</Voice>
 */
	}
	
	/**
	 * 创建【视频消息】的差异部分XML字符串<BR/>
	 * @param mediaId --String*-- 视频多媒体文件ID
	 * @param title --String-- 视频消息标题
	 * @param description --String-- 视频消息描述
	 * @return null 或 XML格式字符串
	 */
	public static String getVideoDiffPartXmlMsg(String mediaId, String title, String escription){
		if (ToolUtil.isStrEmpty(mediaId))
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<Video>");
		tmpBuffer.append("<MediaId><![CDATA[").append(mediaId).append("]]></MediaId>");
		if (!ToolUtil.isStrEmpty(title))
			tmpBuffer.append("<Title><![CDATA[").append(title).append("]]></Title>");
		if (!ToolUtil.isStrEmpty(escription))
			tmpBuffer.append("<Description><![CDATA[").append(escription).append("]]></Description>");
		tmpBuffer.append("</Video>");
		String tmpStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;

		return tmpStr;
/*
<Video>
<MediaId><![CDATA[media_id]]></MediaId>
<Title><![CDATA[title]]></Title>
<Description><![CDATA[description]]></Description>
</Video>
 */
	}
	
	/**
	 * 创建【音乐消息】的差异部分XML字符串<BR/>
	 * @param MusicMessageJson --MusicMessageJson*-- 音乐消息JSON对象
	 * @return null 或 XML格式字符串
	 */
	public static String getMusicDiffPartXmlMsg(MusicMessageJson musicMessageJson){
		if (musicMessageJson == null
				|| musicMessageJson.getMusicurl() == null
				|| musicMessageJson.getHqmusicurl() == null
				|| musicMessageJson.getThumb_media_id() == null)
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<Music>");
		if (!ToolUtil.isStrEmpty(musicMessageJson.getTitle()))
			tmpBuffer.append("<Title><![CDATA[").append(musicMessageJson.getTitle()).append("]]></Title>");
		if (!ToolUtil.isStrEmpty(musicMessageJson.getDescription()))
			tmpBuffer.append("<Description><![CDATA[").append(musicMessageJson.getDescription()).append("]]></Description>");
		tmpBuffer.append("<MusicUrl><![CDATA[").append(musicMessageJson.getMusicurl()).append("]]></MusicUrl>");
		tmpBuffer.append("<HQMusicUrl><![CDATA[").append(musicMessageJson.getHqmusicurl()).append("]]></HQMusicUrl>");
		tmpBuffer.append("<ThumbMediaId><![CDATA[").append(musicMessageJson.getThumb_media_id()).append("]]></ThumbMediaId>");
		tmpBuffer.append("</Music>");
		String tmpStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;

		return tmpStr;
/*
<Music>
<Title><![CDATA[TITLE]]></Title>
<Description><![CDATA[DESCRIPTION]]></Description>
<MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
<HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
<ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>
</Music>
 */
	}
	
	/**
	 * 创建【图文消息】的差异部分XML字符串<BR/>
	 * @param itemMessageList --List&lt;ItemMessageJson&gt;*-- 图文消息里的条目信息JSON对象列表
	 * @return null 或 XML格式字符串
	 */
	public static String getNewsDiffPartXmlMsg(List<ItemMessageJson> itemMessageList){
		int size = itemMessageList == null ? 0 : itemMessageList.size();
		if (size == 0 || size > 10)
			return null;

		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<ArticleCount>").append(size).append("</ArticleCount>");
		tmpBuffer.append("<Articles>");
		for (ItemMessageJson itemMessageJson : itemMessageList){
			if (itemMessageJson == null)
				continue;

			tmpBuffer.append("<item>");
			if (!ToolUtil.isStrEmpty(itemMessageJson.getTitle()))
				tmpBuffer.append("<Title><![CDATA[").append(itemMessageJson.getTitle()).append("]]></Title>");
			if (!ToolUtil.isStrEmpty(itemMessageJson.getDescription()))
				tmpBuffer.append("<Description><![CDATA[").append(itemMessageJson.getDescription()).append("]]></Description>");
			if (!ToolUtil.isStrEmpty(itemMessageJson.getPicurl()))
				tmpBuffer.append("<PicUrl><![CDATA[").append(itemMessageJson.getPicurl()).append("]]></PicUrl>");
			if (!ToolUtil.isStrEmpty(itemMessageJson.getUrl()))
				tmpBuffer.append("<Url><![CDATA[").append(itemMessageJson.getUrl()).append("]]></Url>");
			tmpBuffer.append("</item>");
		}
		tmpBuffer.append("</Articles>");
		String tmpStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;

		return tmpStr;
/*
<ArticleCount>2</ArticleCount>
<Articles>
<item>
<Title><![CDATA[title1]]></Title> 
<Description><![CDATA[description1]]></Description>
<PicUrl><![CDATA[picurl]]></PicUrl>
<Url><![CDATA[url]]></Url>
</item>
<item>
<Title><![CDATA[title]]></Title>
<Description><![CDATA[description]]></Description>
<PicUrl><![CDATA[picurl]]></PicUrl>
<Url><![CDATA[url]]></Url>
</item>
</Articles>
 */
	}
	
	/**
	 * 创建消息相同部分XML字符串
	 * @param openId --String*-- 消息接收人(即关注人员)OpenID
	 * @param developId --String*-- 开发者微信号
	 * @param type --int*-- 消息类型(0-文本，1-图片，2-语音, 3-视频 , 4-音乐，5-图文)
	 * @return XML格式字符串
	 */
	private static String getCommonPartXmlMsg(String openId, String developId, int type){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append("<ToUserName><![CDATA[").append(openId).append("]]></ToUserName>");
		tmpBuffer.append("<FromUserName><![CDATA[").append(developId).append("]]></FromUserName>");
		tmpBuffer.append("<CreateTime>").append(System.currentTimeMillis() / 1000).append("</CreateTime>");
		if (type == 0)		//0-文本
			tmpBuffer.append("<MsgType><![CDATA[text]]></MsgType>");
		else if (type == 1)	//1-图片
			tmpBuffer.append("<MsgType><![CDATA[image]]></MsgType>");
		else if (type == 2)	//2-语音
			tmpBuffer.append("<MsgType><![CDATA[voice]]></MsgType>");
		else if (type == 3)	//3-视频
			tmpBuffer.append("<MsgType><![CDATA[video]]></MsgType>");
		else if (type == 4)	//4-音乐
			tmpBuffer.append("<MsgType><![CDATA[music]]></MsgType>");
		else if (type == 5)	//5-图文
			tmpBuffer.append("<MsgType><![CDATA[news]]></MsgType>");
		
		String tmpStr = tmpBuffer.toString();

		//清空
		tmpBuffer = null;

		return tmpStr;
	}
}