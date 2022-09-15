package com.nnit.pb.api;

import org.springframework.cloud.openfeign.FeignClient;




import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.api.factory.RemoteAFServiceFallbackFactory;
import com.nnit.pb.api.model.AFLogin;


@FeignClient(contextId = "remoteAFService", value = "afService", url = "http://af.nnitcorp.com/api", fallbackFactory = RemoteAFServiceFallbackFactory.class)
public interface RemoteAFService {
     @PostMapping(value = "/login")
	 public JSONObject getToken(@RequestBody AFLogin afLogin);
     
     @PostMapping(value = "/task")
	 public JSONObject callModule(@RequestBody JSONObject afMoudle);
}
