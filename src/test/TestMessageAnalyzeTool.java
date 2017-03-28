package test;

import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.tool.MessageAnalyzeTool;

public class TestMessageAnalyzeTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String strXML = "<xml><ToUserName><![CDATA[gh_xxxxxxxxxxxxxxxxxxx]]></ToUserName><FromUserName><![CDATA[ohOO1uI51iOqXvJK-leF_XsFDs9Q]]></FromUserName><CreateTime>1470199779</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[TEMPLATESENDJOBFINISH]]></Event><MsgID>408650192</MsgID><Status><![CDATA[success]]></Status></xml>";
			Object object = MessageAnalyzeTool.parseReceiveMessage(strXML);
			System.out.println(object);
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}

}
