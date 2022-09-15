package com.nnit.pb.common.constant;

public enum ResultEnum  {
	

    SUCCESS(200, "success"),
    ERROR(400, "error"),
    BIND_ERROR(501, "bind error"),
    EXCEPTION_ERROR(502, "exception error"),
    RUNTIME_ERROR(503, "runtime error"),

    

    USER_AUTH_FAILED(401, "Authentication failed"),
    USER_NOT_EXIST(402, "User does not exist"),
    ROLE_NOT_EXIST(403, "User Role does not exist"),
    USER_NOT_HAVE_ROLE(404, "User does not have the requre role"),
    USER_KEY_NOT_EXISTE(405, "UserKey does not exist"),

    ;

    private Integer code;

	private String message;

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
 
}
