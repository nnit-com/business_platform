package com.nnit.pb.common.manager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nnit.pb.common.constant.RedisPrefixConstants;
import com.nnit.pb.common.redis.service.RedisService;
import com.nnit.pb.common.utils.JWT;
import com.nnit.pb.common.vm.LoginUser;
import com.nnit.pb.common.vm.TokenInfo;
@Component
public class TokenManager {
    /**
     * header中的标识
     */
    public static final String AUTHENTICATION = "Authorization";
    
    public static final String PREFIX = "Bearer";


    private static final long MILLIS_SECOND = 1000;

    private static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * token 超时时间 （分钟） 
     */
    private static final  long EXPIRE_TIME = 90 * 24 * 60 ;
    
    @Autowired
    private RedisService redisService;
    
    /**
     * 创建Token
     * @param apiLogin
     * @return
     */
    public String ceateToken(LoginUser apiLogin){
//		String userKey = UUID.randomUUID().toString();
    	apiLogin.setUserKey(getTokenKey(apiLogin.getUserCode()));
    	refreshToken(apiLogin);
    	TokenInfo tokenInfo = new TokenInfo();
    	tokenInfo.setUserCode(apiLogin.getUserCode());
    	tokenInfo.setUserId(apiLogin.getUserId());
    	tokenInfo.setUserKey(apiLogin.getUserKey());
    	String token = JWT.sign(tokenInfo, EXPIRE_TIME * MILLIS_MINUTE);
		return token;    	
    }
    
   /**
    * 刷新 Token
    * @param apiLogin
    */
    public void refreshToken(LoginUser apiLogin)
    {
        redisService.setCacheObject(apiLogin.getUserKey(), apiLogin, EXPIRE_TIME, TimeUnit.MINUTES);
    }
    
    private String getTokenKey(String userCode)
    {
        return RedisPrefixConstants.TOKEN_LOGIN + userCode;
    }
    
    /**
     * 解析Token
     * @param token
     * @return
     */
    public TokenInfo unsign(String token){
    	return JWT.unsign(token, TokenInfo.class);
    }
    
    /**
     * token key 是否存在
     * @param userKey
     * @return
     */
    public boolean isExist(String userKey){
    	return redisService.hasKey(userKey);
    }
    
    /**
     * 获得loginUser
     * @param userKey
     * @return
     */
    public LoginUser getLoginUser(String userKey){
    	return redisService.getCacheObject(userKey);
    }
    
    
}
