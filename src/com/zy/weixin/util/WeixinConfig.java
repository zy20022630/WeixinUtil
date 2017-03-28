package com.zy.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取微信配置文件的工具类
 * @author zy20022630
 */
public class WeixinConfig {

	private static Properties properties = null;
	
	static {
		if (properties == null){
			properties = new Properties();
			InputStream inputStream = null;
			try {
				inputStream = WeixinConfig.class.getResourceAsStream("/weixin.properties");
				properties.load(inputStream);
			} catch (IOException e) {
				//e.printStackTrace();
			} finally{
				if (inputStream != null){
					try {
						inputStream.close();
					} catch (IOException e) {
						//e.printStackTrace();
					} finally{
						inputStream = null;
					}
				}
			}
		}
	}
	
	/**
	 * 根据KEY，获取VALUE
	 * @param key
	 * @return
	 */
	public static String getConfig(String key){
		if (properties == null)
			return null;
		return properties.getProperty(key);
	}
}