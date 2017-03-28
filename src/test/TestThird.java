package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.zy.weixin.common.HTTPSSecureProtocolSocketFactory;

public class TestThird {

	public static void main(String[] args){
		String httpURL = null;
		String strToken = null;
		String encoding = null;
		String content = null;

		try {
			httpURL = "http://dev.xxxxxxxxxxx.com.cn/XXXXXXXXXXXX/servlet/CommonWeiXinServlet";
			strToken = "xxxxxxxxxxxxxxxxxxxxxxx";
			encoding = "UTF-8";

			Protocol.registerProtocol("https", new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443));
			HttpClient client = new HttpClient();
			
			if (TestThird.check(client, httpURL, strToken, encoding)){
				System.out.println("效验服务器成功");
								
				content = "<xml>" +
						"<ToUserName><![CDATA[gh_xxxxxxxxxxxx]]></ToUserName>" +
						"<FromUserName><![CDATA[oJyoXuMgIMR-cZJ7eYNMiybqumF8]]></FromUserName>" +
						"<CreateTime>1438162627</CreateTime>" +
						"<MsgType><![CDATA[text]]></MsgType>" +
						"<Content><![CDATA[subscribe]]></Content>" +
						"<MsgId>6176861406551125167</MsgId>" +
						"</xml>";

				System.out.println(TestThird.sendMessage(client, httpURL, content, encoding));

			} else {
				System.out.println("效验服务器失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	static String sendMessage(final HttpClient client, String httpURL, String content, String encoding) throws Exception{
		if (client == null || httpURL == null || content == null)
			throw new Exception("参数为空");
		else if (content.length() == 0)
			throw new Exception("内容为空");
		
		if (encoding == null)
			encoding = "UTF-8";
		
		//定义临时变量
		String result = null;
		StringBuffer tmpBuffer = null;
		PostMethod postMethod = null;
		String tmpStr = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;

		try {
			tmpBuffer = new StringBuffer();
			postMethod = new PostMethod(httpURL);
			postMethod.setRequestBody(content);
			postMethod.getParams().setContentCharset(encoding);
			if (client.executeMethod(postMethod) == HttpStatus.SC_OK){
				inputStreamReader = new InputStreamReader(postMethod.getResponseBodyAsStream(), encoding);
				reader = new BufferedReader(inputStreamReader);
				tmpBuffer.delete(0, tmpBuffer.length());
	            while ((tmpStr = reader.readLine()) != null) {
	                tmpBuffer.append(tmpStr);
	            }
	            result = tmpBuffer.toString();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			//清空
			tmpBuffer = null;
			if (postMethod != null)
				postMethod.releaseConnection();
			postMethod = null;
	    	tmpStr = null;
			if (inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
				} finally{
					inputStreamReader = null;
				}
			}
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				} finally{
					reader = null;
				}
			}
		}

		return result;
	}
	
	static boolean check(final HttpClient client, String httpURL, String strToken, String encoding) throws Exception{
		if (client == null || httpURL == null || strToken == null)
			throw new Exception("参数为空");
		
		//定义临时变量
		boolean checkOK = false;
		String token = null;
		String timestamp = null;
		String nonce = null;
		String echostr = null;
		String[] array = null;
		String signature = null;
		StringBuilder tmpBuffer = null;
		GetMethod getMethod = null;
		String tmpStr = null;
    	InputStreamReader inputStreamReader = null;
    	BufferedReader reader = null;
		String result = null;
		
		try {
			//获取参数
			//token、timestamp、echostr
			token = strToken;
			timestamp = String.valueOf(System.currentTimeMillis() / 1000);
			nonce = getNoncestr(32);
			echostr = getNoncestr(12);
			array = new String[]{token, timestamp, nonce};
			signature = getSignature(array);
			
			if (encoding == null)
				encoding = "UTF-8";
			
			//拼接URL
			tmpBuffer = new StringBuilder();
			tmpBuffer.append(httpURL);
			if (httpURL.indexOf("?") == -1)
				tmpBuffer.append("?");
			else
				tmpBuffer.append("&");
			tmpBuffer.append("signature=").append(signature);
			tmpBuffer.append("&");
			tmpBuffer.append("timestamp=").append(timestamp);
			tmpBuffer.append("&");
			tmpBuffer.append("nonce=").append(nonce);
			tmpBuffer.append("&");
			tmpBuffer.append("echostr=").append(echostr);

			//效验地址是否合格
			getMethod = new GetMethod(tmpBuffer.toString());
			if (client.executeMethod(getMethod) == HttpStatus.SC_OK){
				inputStreamReader = new InputStreamReader(getMethod.getResponseBodyAsStream(), encoding);
				reader = new BufferedReader(inputStreamReader);
				tmpBuffer.delete(0, tmpBuffer.length());
	            while ((tmpStr = reader.readLine()) != null) {
	                tmpBuffer.append(tmpStr);
	            }
	            result = tmpBuffer.toString();
				checkOK = echostr.equals(result);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			//清空
			token = null;
			timestamp = null;
			nonce = null;
			echostr = null;
			array = null;
			signature = null;
			tmpBuffer = null;
			if (getMethod != null)
				getMethod.releaseConnection();
			getMethod = null;
	    	tmpStr = null;
			if (inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
				} finally{
					inputStreamReader = null;
				}
			}
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				} finally{
					reader = null;
				}
			}
			result = null;
		}
		
		return checkOK;
	}
	
	/**
	 * 获取指定位数的随机字符串
	 * @param bit --int-- 指定位数
	 * @return 随机字符串
	 */
	static String getNoncestr(int bit){
		Random random = new Random();
		StringBuilder tmpBuffer = new StringBuilder();
		
		for (int i = 0;i < bit;i++){
			tmpBuffer.append(random.nextInt(10));
		}
		String returnStr = tmpBuffer.toString();
		
		random = null;
		tmpBuffer = null;
		
		return returnStr;
	}
	
	static String getSignature(String[] array) throws Exception{
		String signature = null;
		if (array == null)
			return signature;

		StringBuilder tmpBuffer = null;
		MessageDigest messageDigest = null;
		byte[] digest = null;
		
		try {
			Arrays.sort(array);
			tmpBuffer = new StringBuilder();
			for (int i = 0; i < array.length; i++) {
				tmpBuffer.append(array[i]);
			}
			
			messageDigest = MessageDigest.getInstance("SHA-1");
			digest = messageDigest.digest(tmpBuffer.toString().getBytes());
			signature = byteToStr(digest);
		} catch (Exception e) {
			throw e;
		} finally {
			tmpBuffer = null;
			messageDigest = null;
			digest = null;
		}
		
		return signature;
	}
	
	/**
	 * 将字节数组转换为十六进制字符串
	 * @param bytearray --byte[]*-- 字节数组
	 * @return 十六进制字符串
	 */
    static String byteToStr(byte[] bytearray) {
    	StringBuilder strDigest = new StringBuilder();
        for (int i = 0; i < bytearray.length; i++) {
            strDigest.append(byteToHexStr(bytearray[i]));
        }
        return strDigest.toString();
    }
	
	/**
	 * 将字节转换为十六进制字符串
	 * @param ib --byte*-- 字节
	 * @return 十六进制字符串
	 */
	static String byteToHexStr(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        return new String(ob);
    }
	
}