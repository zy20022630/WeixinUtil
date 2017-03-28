package com.zy.weixin.common;

/**
 * 微信API通用异常类
 * @author zy20022630
 */
public class WeiXinException extends Exception {

	private static final long serialVersionUID = 2531099329481048608L;

	public WeiXinException() {
		super();
	}

	public WeiXinException(String message) {
		super(message);
	}

	public WeiXinException(Throwable cause) {
		super(cause);
	}

	public WeiXinException(String message, Throwable cause) {
		super(message, cause);
	}
}