package com.nnit.pb.api.exception;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nnit.pb.common.constant.ResultEnum;
import com.nnit.pb.common.vm.ResponseData;

@RestControllerAdvice
public class ResultExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ResultExceptionHandler.class);

    @ExceptionHandler(ResultException.class)    
    public ResponseData resultException(ResultException e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',结果异常.", requestURI, e);
    	ResponseData resp = new ResponseData(e.getCode(), e.getMessage());
        return resp;
    }
    
    @ExceptionHandler(Exception.class)    
    public ResponseData exception(Exception e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',系统异常.", requestURI, e);
        Integer code = ResultEnum.EXCEPTION_ERROR.getCode();
    	ResponseData resp = new ResponseData(code, e.getMessage());
        return resp;
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseData bindException(BindException e){
        log.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        Integer code = ResultEnum.BIND_ERROR.getCode();
        String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        ResponseData resp = new ResponseData(code, msg);
        return resp;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseData runtimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',未知异常.", requestURI, e);
    	Integer code = ResultEnum.RUNTIME_ERROR.getCode();
        ResponseData resp = new ResponseData(code, e.getMessage());
        return resp;
    }
}

