package com.nnit.pb.common.constant;

public class SysConstants {
	
	/**
	 * 用户相关信息的redis 过期时间 （分钟） 12小时
	 */
	public static final long USER_EXPIRE_TIME = 12 * 60;

	
	public static final byte TASK_STATUS_DRAFT = 0;
	public static final byte TASK_STATUS_SUCCESS = 1;
	public static final byte TASK_STATUS_FAILED = 2;

	public static final String TASK_NAME_SENDEMAIL = "sendemail";
	public static final String TASK_NAME_CALLAF = "callaf";


}
