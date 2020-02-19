package com.question.admin.domain;

/**
 * 统一Rest API异常类
 * 
 * @author 
 *
 */
public class QuestionServiceException extends Exception {

	private static final long serialVersionUID = -7795831940099270963L;

	/**
	 * 构造函数
	 */
	public QuestionServiceException() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public QuestionServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 */
	public QuestionServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 */
	public QuestionServiceException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 */
	public QuestionServiceException(Throwable cause) {
		super(cause);
	}

}
