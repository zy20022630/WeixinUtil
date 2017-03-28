package test;

import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CommonReturnMsgJson;
import com.zy.weixin.json.UploadFileJson;
import com.zy.weixin.tool.MediaFileTool;

public class TestMediaFileTool {

	/**
	 * @param args	OK
	 */
	public static void main(String[] args) {
		//test1();
		//test2();
	}
	
	static void test1(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		Integer type = null;
		String filePath = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		type = 1;
		filePath = "D:/Temp/test/weichat/upload/11.jpg";
/*
{"type":"image","media_id":"kSGqY0TQchCLubAqo8eLud5I8p1ec0vXEvV5DfXaXai1bYeu13X433xkX-FDjEsC","created_at":1422524329}

上传文件成功，type为image，created_at为1422524329，media_id为kSGqY0TQchCLubAqo8eLud5I8p1ec0vXEvV5DfXaXai1bYeu13X433xkX-FDjEsC，thumb_media_id为：null	
 */
		
//		type = 2;
//		filePath = "D:/Temp/test/weichat/upload/22.amr";
/*
{"type":"voice","media_id":"xWhAYyXRd7g4Rlsw_KOAJD1MhIFa52n95Hyhi5PjbnAMWqymo-vEkKKGFmRLkerH","created_at":1422524352}

上传文件成功，type为voice，created_at为1422524352，media_id为xWhAYyXRd7g4Rlsw_KOAJD1MhIFa52n95Hyhi5PjbnAMWqymo-vEkKKGFmRLkerH，thumb_media_id为：null
 */
		
//		type = 3;
//		filePath = "D:/Temp/test/weichat/upload/33.mp4";
/*
{"type":"video","media_id":"3_IG_O8EupnuWoovwMoKDTfTLo-rsmBk2SYiVsZ8flSRmzDVrKGZMYL-jzu_ttSz","created_at":1422524382}

上传文件成功，type为video，created_at为1422524382，media_id为3_IG_O8EupnuWoovwMoKDTfTLo-rsmBk2SYiVsZ8flSRmzDVrKGZMYL-jzu_ttSz，thumb_media_id为：null	
 */
		
//		type = 4;
//		filePath = "D:/Temp/test/weichat/upload/44.jpg";
/*
{"type":"thumb","thumb_media_id":"iPQvmjeLxC6-oVmCrtOkW7LBlSOIutFplOrD-zNf0SSn0XEkixrK2KCFhrbMGHQz","created_at":1422524272}

上传文件成功，type为thumb，created_at为1422524272，media_id为null，thumb_media_id为：iPQvmjeLxC6-oVmCrtOkW7LBlSOIutFplOrD-zNf0SSn0XEkixrK2KCFhrbMGHQz
 */
		
		Object object = null;
		CommonReturnMsgJson errorMsgJson = null;
		UploadFileJson uploadFileJson = null;
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			if (accessToken != null)
				object = MediaFileTool.getInstance().uploadFile(accessToken, type, filePath);
			
			if (object != null){
				if (object instanceof CommonReturnMsgJson)	//上传失败
					errorMsgJson = (CommonReturnMsgJson)object;
				else if (object instanceof UploadFileJson)	//上传成功
					uploadFileJson = (UploadFileJson)object;
			}
			
			StringBuffer tmpBuffer = new StringBuffer();
			if (uploadFileJson != null){
				tmpBuffer.delete(0, tmpBuffer.length()).append("上传文件成功，")
						 .append("type为").append(uploadFileJson.getType()).append("，")
						 .append("created_at为").append(uploadFileJson.getCreated_at()).append("，")
						 .append("media_id为").append(uploadFileJson.getMedia_id()).append("，")
						 .append("thumb_media_id为：").append(uploadFileJson.getThumb_media_id());
				System.out.println(tmpBuffer.toString());
			} else if (errorMsgJson != null){
				tmpBuffer.delete(0, tmpBuffer.length()).append("上传文件失败，")
						 .append("errcode为：").append(errorMsgJson.getErrcode()).append("，")
						 .append("errmsg为：").append(errorMsgJson.getErrmsg());
				System.out.println(tmpBuffer.toString());
			}
			
			
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
		
		String mediaId = null;
		String filePath = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

		boolean isOK = false;
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			if (accessToken != null){
				mediaId = "kSGqY0TQchCLubAqo8eLud5I8p1ec0vXEvV5DfXaXai1bYeu13X433xkX-FDjEsC";
				filePath = "D:/Temp/test/weichat/down/11.jpg";
				isOK = MediaFileTool.getInstance().downloadFile(accessToken, mediaId, filePath);
				System.out.println("下载[图片]多媒体文件是否成功：" + isOK);
			}
			
			if (accessToken != null){
				mediaId = "xWhAYyXRd7g4Rlsw_KOAJD1MhIFa52n95Hyhi5PjbnAMWqymo-vEkKKGFmRLkerH";
				filePath = "D:/Temp/test/weichat/down/22.amr";
				isOK = MediaFileTool.getInstance().downloadFile(accessToken, mediaId, filePath);
				System.out.println("下载[语音]多媒体文件是否成功：" + isOK);
			}
			
			if (accessToken != null){
				//请注意，视频文件不支持下载
				mediaId = "3_IG_O8EupnuWoovwMoKDTfTLo-rsmBk2SYiVsZ8flSRmzDVrKGZMYL-jzu_ttSz";
				filePath = "D:/Temp/test/weichat/down/33.mp4";
				isOK = MediaFileTool.getInstance().downloadFile(accessToken, mediaId, filePath);
				System.out.println("下载[视频]多媒体文件是否成功：" + isOK);
			}
			
			if (accessToken != null){
				mediaId = "iPQvmjeLxC6-oVmCrtOkW7LBlSOIutFplOrD-zNf0SSn0XEkixrK2KCFhrbMGHQz";
				filePath = "D:/Temp/test/weichat/down/44.jpg";
				isOK = MediaFileTool.getInstance().downloadFile(accessToken, mediaId, filePath);
				System.out.println("下载[缩略图]多媒体文件是否成功：" + isOK);
			}
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}