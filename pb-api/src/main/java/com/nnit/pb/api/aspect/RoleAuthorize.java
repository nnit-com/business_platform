package com.nnit.pb.api.aspect;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nnit.pb.api.annotation.RequiresRoles;
import com.nnit.pb.api.exception.ResultException;
import com.nnit.pb.common.constant.ResultEnum;
import com.nnit.pb.common.constant.SecurityConstants;
import com.nnit.pb.common.manager.TokenManager;
import com.nnit.pb.common.utils.ServletUtils;
import com.nnit.pb.common.vm.LoginUser;


/**
 * 权限验证
 * @author LUFY
 *
 */
@Component
@Aspect
public class RoleAuthorize
{
    @Autowired
    private TokenManager tokenManager;
	/**
     * 构建
     */
    public RoleAuthorize()
    {
    }

    /**
     * 定义AOP签名 (切入所有使用鉴权注解的方法)
     */
    public static final String POINTCUT_SIGN = "@annotation(com.nnit.pb.api.annotation.RequiresRoles)";

    /**
     * 声明AOP签名
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut()
    {
    }

    /**
     * 环绕切入
     * 
     * @param joinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        // 注解鉴权
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        checkMethodAnnotation(signature.getMethod());
        try
        {
            // 执行原有逻辑
            Object obj = joinPoint.proceed();
            return obj;
        }
        catch (Throwable e)
        {
            throw e;
        }
    }

    /**
     * 对一个Method对象进行注解检查
     */
    public void checkMethodAnnotation(Method method)
    {
        // 校验 @RequiresRoles 注解
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        if (requiresRoles != null)
        {
        	HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        	String userKey = httpServletRequest.getHeader(SecurityConstants.USER_KEY);
        	if(userKey == null){
    			throw new ResultException(ResultEnum.USER_KEY_NOT_EXISTE);
        	}
        	LoginUser loginUser = tokenManager.getLoginUser(userKey);
        	if(loginUser == null){
    			throw new ResultException(ResultEnum.USER_NOT_EXIST);
        	}
        	String[] roleArray = requiresRoles.value();
        	for(String role : roleArray){
        		if(!loginUser.getUserRoles().contains(role)){
        			throw new ResultException(ResultEnum.USER_NOT_HAVE_ROLE);
        		}
        	}
        }
    }
}
