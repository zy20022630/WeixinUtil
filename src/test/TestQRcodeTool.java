package test;

import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.QRcodeMsgJson;
import com.zy.weixin.tool.QRcodeTool;

public class TestQRcodeTool {
	
	public static void main(String[] args) {
		//getToken();
		
		//createTempQRcode();
		
		//createQRcode();
		
		downloadQRcode1();
		
		downloadQRcode2();
	}
	
	static void getToken(){
		String appid = null;
		String appsecret = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		AccessTokenHelper helper = null;
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret)){
				System.out.println(helper.getAccessToken());
			}else{
				System.out.println("获取唯一凭证失败");
			}
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	static void createTempQRcode(){
		StringBuffer tmpBuffer = new StringBuffer();
		CommonReturnMsgJson errMsgJson = null;
		QRcodeMsgJson sucMsgJson = null;
		
		System.out.println("=============================================");
		System.out.println("创建临时二维码");
		System.out.println("---------------------------------------------");
		
		try {
			String accessToken = "hCIfjEy0oVTpsYI-6pyuTklG61xntcV2JqP_tpYad2HU4_oimSlyVsX5RPiQWy_UOTyAJJjbXVQvyGKqX3rDECjAIMCsXrYp-rTDLgGe8XE";
			Long sceneId = 100001L;
			Long expireSeconds = 60480L;
			Object object = QRcodeTool.getInstance().createTempQRcode(accessToken, sceneId, expireSeconds);
			if (object != null){
				if (object instanceof CommonReturnMsgJson){
					errMsgJson = (CommonReturnMsgJson)object;
					tmpBuffer.append("异常代码：").append(errMsgJson.getErrcode()).append("，异常原因：").append(errMsgJson.getErrmsg());
					System.out.println(tmpBuffer.toString());
				}
				if (object instanceof QRcodeMsgJson){
					sucMsgJson = (QRcodeMsgJson)object;
					tmpBuffer.append("有效时间为：").append(sucMsgJson.getExpire_seconds()).append("，Ticket为：").append(sucMsgJson.getTicket());
					System.out.println(tmpBuffer.toString());
				}
			}
/*
有效时间为：60480，Ticket为：gQEd8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL3NrTmtJaEhsUURJNVM3YjByRzFBAAIEFDFUVQMEQOwAAA==			
 */
			
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
	static void createQRcode(){
		StringBuffer tmpBuffer = new StringBuffer();
		CommonReturnMsgJson errMsgJson = null;
		QRcodeMsgJson sucMsgJson = null;
		
		System.out.println("=============================================");
		System.out.println("创建永久二维码");
		System.out.println("---------------------------------------------");
		
		try {
			String accessToken = "hCIfjEy0oVTpsYI-6pyuTklG61xntcV2JqP_tpYad2HU4_oimSlyVsX5RPiQWy_UOTyAJJjbXVQvyGKqX3rDECjAIMCsXrYp-rTDLgGe8XE";
			Long sceneId = 2L;
			String sceneStr = null;
			Object object = QRcodeTool.getInstance().createQRcode(accessToken, sceneId, sceneStr);
			if (object != null){
				if (object instanceof CommonReturnMsgJson){
					errMsgJson = (CommonReturnMsgJson)object;
					tmpBuffer.append("异常代码：").append(errMsgJson.getErrcode()).append("，异常原因：").append(errMsgJson.getErrmsg());
					System.out.println(tmpBuffer.toString());
				}
				if (object instanceof QRcodeMsgJson){
					sucMsgJson = (QRcodeMsgJson)object;
					tmpBuffer.append("有效时间为：").append(sucMsgJson.getExpire_seconds()).append("，Ticket为：").append(sucMsgJson.getTicket());
					System.out.println(tmpBuffer.toString());
				}
			}
/*
有效时间为：0，Ticket为：gQHo7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2dFTWVKb0xsQ2pKejdJYkExbTlBAAIEUDFUVQMEAAAAAA==			
 */
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
		
	}

	static void downloadQRcode1(){
		try {
			System.out.println("=============================================");
			System.out.println("下载临时二维码");
			System.out.println("---------------------------------------------");
			
			String ticket = "gQEd8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL3NrTmtJaEhsUURJNVM3YjByRzFBAAIEFDFUVQMEQOwAAA==";
			String filePath = "D:\\Temp\\test\\download\\临时二维码_100001.jpg";
			boolean isOK = QRcodeTool.getInstance().downloadQRcode(ticket, filePath);
			if (isOK)
				System.out.println("下载成功");
			else
				System.out.println("下载失败");
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
	static void downloadQRcode2(){
		try {
			System.out.println("=============================================");
			System.out.println("下载永久二维码");
			System.out.println("---------------------------------------------");
			
			String ticket = "gQHo7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2dFTWVKb0xsQ2pKejdJYkExbTlBAAIEUDFUVQMEAAAAAA==";
			String filePath = "D:\\Temp\\test\\download\\永久二维码_2.jpg";
			boolean isOK = QRcodeTool.getInstance().downloadQRcode(ticket, filePath);
			if (isOK)
				System.out.println("下载成功");
			else
				System.out.println("下载失败");
		} catch (WeiXinException e) {
			e.printStackTrace();
		}
	}
	
}