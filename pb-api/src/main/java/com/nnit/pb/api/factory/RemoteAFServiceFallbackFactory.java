package com.nnit.pb.api.factory;


import org.slf4j.Logger;


import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.api.RemoteAFService;
import com.nnit.pb.api.model.AFLogin;

import feign.hystrix.FallbackFactory;




@Component
public class RemoteAFServiceFallbackFactory implements FallbackFactory<RemoteAFService>
{    
	private static final Logger log = LoggerFactory.getLogger(RemoteAFServiceFallbackFactory.class);

	@Override
	public RemoteAFService create(Throwable throwable) {
        log.error("AF服务调用失败:{}", throwable.getMessage());
        return new RemoteAFService()
        {       
			@Override
			public JSONObject getToken(AFLogin afLogin) {
				JSONObject js = new JSONObject();
				js.put("success", false);
				js.put("error-message", "AF服务调用失败:" + throwable.getMessage());
				return js;
			}

			@Override
			public JSONObject callModule(JSONObject afMoudle) {
				JSONObject js = new JSONObject();
				js.put("success", false);
				js.put("error-message", "AF服务调用失败:" + throwable.getMessage());
				return js;
			}
        };
	}

    
}
