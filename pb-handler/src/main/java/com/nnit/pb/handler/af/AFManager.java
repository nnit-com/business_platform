package com.nnit.pb.handler.af;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.api.RemoteAFService;
import com.nnit.pb.api.model.AFLogin;
import com.nnit.pb.common.constant.RedisPrefixConstants;
import com.nnit.pb.common.redis.service.RedisService;
import com.nnit.pb.common.utils.AesUtil;
import com.nnit.pb.common.utils.StringUtils;

@Component
public class AFManager {
	/**
	 * 超时时间 （分钟）
	 */
    private static final  long EXPIRE_TIME = 23 * 60 ;

	@Autowired
	private RemoteAFService remoteAFService;
	
	@Autowired
	private AFProperties afProperties;
	
    @Autowired
    private RedisService redisService;
	
	/**
	 * 获取AF token
	 * @return
	 */
	public String getToken(){
		String token = redisService.getCacheObject(getTokenKey());
    	if(StringUtils.isEmpty(token)){
    		token = requestNewToken();
    	}	
    	return token;
	}
	/**
	 * 请求新的AF token
	 * @return
	 */
	private String requestNewToken(){
    	String token = null;
    	AFLogin afLogin = new AFLogin();
    	afLogin.setUsername(afProperties.getAfUn());
    	afLogin.setPassword(AesUtil.decrypt(afProperties.getAfUp()));
		JSONObject re =  remoteAFService.getToken(afLogin);
    	boolean success = re.getBooleanValue("success");
    	if(success){
    		token = re.getString("auth-token");
    		redisService.setCacheObject(getTokenKey(), token, EXPIRE_TIME, TimeUnit.MINUTES);
    	}    		
    	return token;
	} 
	
    private String getTokenKey()
    {
        return RedisPrefixConstants.AF_TOKEN + afProperties.getAfUn();
    }
    
    /**
     * 调用 AF 模块
     * @return
     */
    public AFResponseData callMoule(String serverName){
    	String[] serverScope = {"nnitsqldk178t.nnitcorp.com","nnitpcodk010t.nnitcorp.com","nnitpcodk004t.nnitcorp.com"};
    	AFResponseData rData = new AFResponseData();
    	if(serverName == null || !Arrays.asList(serverScope).contains(serverName.toLowerCase())){
    		rData.setSuccess(false);
    		rData.setError("server is not in scope");
    		return rData;
    	}
    	JSONObject afMoudle = new JSONObject();
    	afMoudle.put("auth-token", getToken());
    	afMoudle.put("module-name", "CPUUtilizationCheck");
    	afMoudle.put("async", "false");  	
    	JSONObject a1 = new JSONObject();
    	a1.put("name", "customer_id");
    	a1.put("value", "nnit");
    	JSONObject a2 = new JSONObject();
    	a2.put("name", "hpsa_user");
    	a2.put("value", afProperties.getHpsaUn());
    	JSONObject a3 = new JSONObject();
    	a3.put("name", "hpsa_password");
    	a3.put("value", AesUtil.decrypt(afProperties.getHpsaUp()));
    	JSONObject a4 = new JSONObject();
    	a4.put("name", "server_fqdn");
    	a4.put("value", serverName);
    	JSONArray inputs = new JSONArray();
    	inputs.add(a1);
    	inputs.add(a2);
    	inputs.add(a3);
    	inputs.add(a4);
    	afMoudle.put("inputs", inputs);
    	JSONObject re =  remoteAFService.callModule(afMoudle);
    	rData.setSuccess(re.getBoolean("success"));
    	if(rData.isSuccess()){
    		JSONObject result = re.getJSONObject("result"); 
    		rData.setResult(result.getString("result"));
    		if("SUCCESS".equals(rData.getResult())){
    			JSONObject outputs = result.getJSONObject("outputs");
            	JSONArray entrys = outputs.getJSONArray("entry");
            	for(int i=0;i<entrys.size();i++){
            		JSONObject entry = (JSONObject)entrys.getJSONObject(i);
            		if("AF-Status".equals(entry.getString("key"))){
            			rData.setAfStatus(entry.getJSONObject("value").getString("$")); 
            		}else if("AF-result".equals(entry.getString("key"))){
            			rData.setAfResult(entry.getJSONObject("value").getString("$")); 
            			if(rData.getAfResult()!=null){
            				rData.setAfResult(rData.getAfResult().split(" is lower")[0]);
            			}
            		}            	
        		} 
    		}			       	
    	}else{
        	rData.setError(re.getString("error-message"));  
        }
    	return rData;

    }
}
