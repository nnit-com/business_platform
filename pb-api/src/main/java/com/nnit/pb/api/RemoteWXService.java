package com.nnit.pb.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;


import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.api.factory.RemoteWXServiceFallbackFactory;
import com.nnit.pb.api.model.WXCorpInfo;
import com.nnit.pb.api.model.WXLogin;


@FeignClient(contextId = "remoteWXService", value = "wxService", url = "https://qyapi.weixin.qq.com/cgi-bin", fallbackFactory = RemoteWXServiceFallbackFactory.class)
public interface RemoteWXService {
     @GetMapping(value = "/miniprogram/jscode2session")
	 public JSONObject jscode2session(@SpringQueryMap WXLogin wxLogin);	  
     
     @GetMapping(value = "/gettoken")
	 public JSONObject getToken(@SpringQueryMap WXCorpInfo wxCorpInfo);	     
    
}
