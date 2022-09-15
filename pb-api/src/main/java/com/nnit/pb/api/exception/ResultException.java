package com.nnit.pb.api.exception;

import com.nnit.pb.common.constant.ResultEnum;

public class ResultException extends RuntimeException {
	private Integer code;

  
    public ResultException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
		return code;
	}

	public ResultException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
