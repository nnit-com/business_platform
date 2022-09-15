package com.nnit.pb.api.factory;


import org.slf4j.Logger;



import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.api.RemoteWXService;
import com.nnit.pb.api.model.AFLogin;
import com.nnit.pb.api.model.WXCorpInfo;
import com.nnit.pb.api.model.WXLogin;
import com.nnit.pb.common.constant.WXConstants;

import feign.hystrix.FallbackFactory;




@Component
public class RemoteWXServiceFallbackFactory implements FallbackFactory<RemoteWXService>
{    
	private static final Logger log = LoggerFactory.getLogger(RemoteWXServiceFallbackFactory.class);

	@Override
	public RemoteWXService create(Throwable throwable) {
        log.error("WX服务调用失败:{}", throwable.getMessage());
        return new RemoteWXService()
        {       
			@Override
			public JSONObject jscode2session(WXLogin wxLogin) {
				JSONObject js = new JSONObject();
				js.put(WXConstants.WX_ERROR_CODE, 4001);
				js.put(WXConstants.WX_ERROR_MEG, "WX服务调用失败:" + throwable.getMessage());
				return js;
			}	
			@Override
			public JSONObject getToken(@SpringQueryMap WXCorpInfo wxCorpInfo) {
				JSONObject js = new JSONObject();
				js.put(WXConstants.WX_ERROR_CODE, 4002);
				js.put(WXConstants.WX_ERROR_MEG, "WX服务调用失败:" + throwable.getMessage());
				return js;
			}	
        };
	}

    
}
