package com.zy.weixin.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * （带证书的）HTTPS的帮助类
 * @author zy20022630
 */
public class SpecialHttpsHelper {
	
	/**
	 * 响应超时时间：5000毫秒
	 */
	private static final int TIME_OUT = 5000;
	
	/**
	 * 默认的编码格式字符集：UTF-8
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private static final String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)";
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	
	private static final String JKS_CA_FILENAME = "tenpay_cacert.jks";
	private static final String JKS_CA_ALIAS = "tenpay";
	private static final String JKS_CA_PASSWORD = "";
	
	private static final String X_509 = "X.509";
	private static final String SunX509 = "SunX509";
	private static final String JKS = "JKS";
	private static final String PKCS12 = "PKCS12";
	private static final String TLS = "TLS";
	
	/**
     * (私有的)无参构造器
     */
    private SpecialHttpsHelper(){
		super();
    }
    
    //定义一个静态实例
	private static SpecialHttpsHelper instance = new SpecialHttpsHelper();
	
    /**
	 * 获取一个对象实例(单例模式)
	 * @return 一个对象实例
	 */
	public static SpecialHttpsHelper getInstance() {
		return instance;
	}
	
	private boolean isStrEmpty(String s){
		return (s == null || s.trim().length() == 0);
	}
	
	/**
	 * 字符串转换成char数组
	 * @param str
	 * @return char[]
	 */
	private char[] str2CharArray(String str) {
		if(null == str)
			return null;
		return str.toCharArray();
	}
	
	/**
	 * 根据相关证书信息，获取SSLContext对象
	 * @param caFilePath --String*-- ca证书绝对路径
	 * @param certFilePath --String*-- 个人(商户)p12证书绝对路径
	 * @param certPasswd --String*-- 个人(商户)p12证书密钥
	 * @return SSLContext对象
	 * @throws IOException
	 */
	public SSLContext getSSLContextByFiles(String caFilePath, String certFilePath, String certPasswd) throws IOException {
		SSLContext sslContext = null;
		
		//定义临时变量
		File caFile = null;
		File certFile = null;
		File jksCAFile = null;
		StringBuffer tmpBuffer = null;
		CertificateFactory certificateFactory = null;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		X509Certificate certificate = null;
		KeyStore keyStore = null;
		FileInputStream trustFileInputStream = null;
		FileInputStream keyFileInputStream = null;
		
		TrustManagerFactory trustManagerFactory = null;
		KeyStore trustKeyStore = null;
		KeyManagerFactory keyManagerFactory = null;
		KeyStore pkcs12KeyStore = null;
		SecureRandom secureRandom = null;

		try {
			//ca证书
			caFile = new File(caFilePath);
			if (!caFile.isFile())
				throw new IOException("ca证书文件不存在");
			
			//个人(商户)证书
			certFile = new File(certFilePath);
			if (!certFile.isFile())
				throw new IOException("个人(商户)证书文件不存在");
			
			//获取jks证书文件绝对路径
			tmpBuffer = new StringBuffer();
			tmpBuffer.append(caFile.getParent()).append(File.separatorChar).append(JKS_CA_FILENAME);
			jksCAFile = new File(tmpBuffer.toString());
			
			//若jks证书文件不存在，则缓存
			if (!jksCAFile.isFile()) {
				fileInputStream = new FileInputStream(caFile);
				fileOutputStream = new FileOutputStream(jksCAFile);
				
				certificateFactory = CertificateFactory.getInstance(X_509);
				certificate = (X509Certificate)certificateFactory.generateCertificate(fileInputStream);

				keyStore = KeyStore.getInstance(JKS);
				keyStore.load(null, null);
				keyStore.setCertificateEntry(JKS_CA_ALIAS, certificate);
				keyStore.store(fileOutputStream, str2CharArray(JKS_CA_PASSWORD));//store keystore
			}
			
			//获取SSLContext对象
			trustFileInputStream = new FileInputStream(jksCAFile);
			keyFileInputStream = new FileInputStream(certFile);

			trustManagerFactory = TrustManagerFactory.getInstance(SunX509);
			trustKeyStore = KeyStore.getInstance(JKS);
			trustKeyStore.load(trustFileInputStream, str2CharArray(JKS_CA_PASSWORD));
			trustManagerFactory.init(trustKeyStore);

			keyManagerFactory = KeyManagerFactory.getInstance(SunX509);
			pkcs12KeyStore = KeyStore.getInstance(PKCS12);
			pkcs12KeyStore.load(keyFileInputStream, str2CharArray(certPasswd));
			keyManagerFactory.init(pkcs12KeyStore, str2CharArray(certPasswd));

			secureRandom = new SecureRandom();
			sslContext = SSLContext.getInstance(TLS);
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), secureRandom);
			
			return sslContext;
		} catch (IOException e){
			throw e;
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		} finally {
			//清空
			caFile = null;
			certFile = null;
			jksCAFile = null;
			tmpBuffer = null;
			certificateFactory = null;
			if (fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					fileInputStream = null;
				}
			}
			if (fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					fileOutputStream = null;
				}
			}
			certificate = null;
			keyStore = null;
			if (trustFileInputStream != null){
				try {
					trustFileInputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					trustFileInputStream = null;
				}
			}
			if (keyFileInputStream != null){
				try {
					keyFileInputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					keyFileInputStream = null;
				}
			}
			trustManagerFactory = null;
			trustKeyStore = null;
			keyManagerFactory = null;
			pkcs12KeyStore = null;
			secureRandom = null;
		}
	}

	/**
	 * 使用POST方法访问请求URL，获取响应信息（https方式）
	 * @param strURL --String*-- 请求URL字符串
	 * @param postData --String*-- 提交的数据
	 * @param encoding --String*-- 编码字符集
	 * @param sslContext --SSLContext*-- 安全套接字协议实例对象
	 * @return null 或 响应内容字符串
	 * @throws IOException
	 */
	public String doPost(String strURL, String postData, String encoding, SSLContext sslContext) throws IOException {
		String rtnContent = null;
		
		//定义临时变量
		URL url = null;
		HttpsURLConnection httpsURLConnection = null;
		SSLSocketFactory sslSocketFactory = null;
		
		try {
			if (isStrEmpty(strURL) || isStrEmpty(postData) || sslContext == null)
				return rtnContent;
			
			sslSocketFactory = sslContext.getSocketFactory();
			url = new URL(strURL);
			httpsURLConnection = (HttpsURLConnection)url.openConnection();
			httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
			rtnContent = doPostWithHttpURLConnection(httpsURLConnection, postData.getBytes(isStrEmpty(encoding) ? DEFAULT_ENCODING : encoding), isStrEmpty(encoding) ? DEFAULT_ENCODING : encoding);
			return rtnContent;
		} catch (IOException e){
			throw e;
		} finally {
			//清空
			url = null;
			if (httpsURLConnection != null)
				httpsURLConnection.disconnect();
			httpsURLConnection = null;
			sslSocketFactory = null;
		}
	}
	
	/**
	 * POST方法处理
	 * @param httpConnection --HttpURLConnection*-- URL连接对象
	 * @param postData --byte[]*-- 数据字节数组
	 * @return 响应内容字符串
	 * @throws IOException
	 */
	private static String doPostWithHttpURLConnection(HttpURLConnection httpConnection, byte[] postData, String encoding) throws IOException {
		String rtnContent = null;
		
		//定义临时变量
		StringBuffer tmpBuffer = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		int len = 1024;
		int dataLen = 0;
		int off = 0;
		
		try {
			//以post方式通信
			httpConnection.setRequestMethod("POST");
			//设置连接超时时间
			httpConnection.setConnectTimeout(TIME_OUT);
			//User-Agent
			httpConnection.setRequestProperty("User-Agent", USER_AGENT_VALUE);
			//不使用缓存
			httpConnection.setUseCaches(false);
			//允许输入输出
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			// Content-Type
			httpConnection.setRequestProperty("Content-Type", CONTENT_TYPE);
			
			//向输入流中输出数据
			outputStream = httpConnection.getOutputStream();
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			dataLen = postData.length;
			off = 0;
			while (off < postData.length) {
				if (len >= dataLen) {
					bufferedOutputStream.write(postData, off, dataLen);
					off += dataLen;
				} else {
					bufferedOutputStream.write(postData, off, len);
					off += len;
					dataLen -= len;
				}
				bufferedOutputStream.flush();// 刷新缓冲区
			}
			
			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
				inputStream = httpConnection.getInputStream();
				rtnContent = getStringFromInputStream(inputStream, encoding);
			} else {
				tmpBuffer = new StringBuffer();
				tmpBuffer.append("后台调用通信失败，响应状态码为").append(httpConnection.getResponseCode());
				throw new IOException(tmpBuffer.toString());
			}
			
			return rtnContent;
		} catch (IOException e){
			throw e;
		} finally {
			//清空
			tmpBuffer = null;
			if (bufferedOutputStream != null){
				try {
					bufferedOutputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					bufferedOutputStream = null;
				}
			}
			if (outputStream != null){
				try {
					outputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					outputStream = null;
				}
			}
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					inputStream = null;
				}
			}
		}
	}
	
	/**
	 * 从输入流中读取数据
	 * @param inputStream --InputStream*-- 输入流
	 * @param encoding --String*-- 编码格式（如：GBK、UTF-8）
	 * @return 数据字符串
	 * @throws IOException
	 */
	private static String getStringFromInputStream(InputStream inputStream, String encoding) throws IOException {
		String rtnContent = null;
		
		//定义临时变量
		int bufferSize = 4096;
		int count = -1;
		byte[] tmpDataArray = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			tmpDataArray = new byte[bufferSize];
			while((count = inputStream.read(tmpDataArray, 0, bufferSize)) != -1){
	        	byteArrayOutputStream.write(tmpDataArray, 0, count);
			}
			rtnContent = byteArrayOutputStream.toString(encoding);
			
			return rtnContent;
		} catch (IOException e){
			throw e;
		} finally {
			//清空
			tmpDataArray = null;
			if (byteArrayOutputStream != null){
				try {
					byteArrayOutputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					byteArrayOutputStream = null;
				}
			}
		}
	}
	
}