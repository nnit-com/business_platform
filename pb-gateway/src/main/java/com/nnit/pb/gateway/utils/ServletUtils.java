package com.nnit.pb.gateway.utils;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.common.vm.ResponseData;

import reactor.core.publisher.Mono;

public class ServletUtils {

    public static Mono<Void> responseMessage(ServerHttpResponse response, ResponseData responseData) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(responseData).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
