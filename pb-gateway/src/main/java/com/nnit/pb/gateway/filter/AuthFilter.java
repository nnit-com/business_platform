package com.nnit.pb.gateway.filter;




import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;
import com.nnit.pb.common.constant.SecurityConstants;
import com.nnit.pb.common.manager.TokenManager;
import com.nnit.pb.common.utils.StringUtils;
import com.nnit.pb.common.vm.LoginUser;
import com.nnit.pb.common.vm.ResponseData;
import com.nnit.pb.common.vm.TokenInfo;
import com.nnit.pb.gateway.config.IgnoreWhite;
import com.nnit.pb.gateway.utils.ServletUtils;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter , Ordered{
    
	@Autowired
    private IgnoreWhite ignoreWhite;
	
	@Autowired
    private TokenManager tokenManager;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        ServerHttpResponse response = exchange.getResponse();
        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (ignoreWhite.getWhites().contains(url))
        {
            return chain.filter(exchange);
        }
        
        String token = getToken(request);
        if(StringUtils.isEmpty(token)){
        	return ServletUtils.responseMessage(response, ResponseData.customerError("Token is empty!"));
        }
        
        TokenInfo tokenInfo = tokenManager.unsign(token);
		if(StringUtils.isNull(tokenInfo)){
        	return ServletUtils.responseMessage(response, ResponseData.customerError("Nothing is in token!"));
		}
		
		boolean isLogin = tokenManager.isExist(tokenInfo.getUserKey());
		if(!isLogin){
        	return ServletUtils.responseMessage(response, ResponseData.customerError("User does not login!"));
		}
		
        // 设置用户信息到头部
        addHeader(mutate, SecurityConstants.USER_KEY, tokenInfo.getUserKey());
        addHeader(mutate, SecurityConstants.USER_ID, tokenInfo.getUserId());
        addHeader(mutate, SecurityConstants.USER_CODE, tokenInfo.getUserCode());
        return chain.filter(exchange.mutate().request(mutate.build()).build());
	}

	@Override
	public int getOrder() {
		return -200;
	}
	
    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value)
    {
        if (value == null)
        {
            return;
        }
        String valueStr = value.toString();
        mutate.header(name, valueStr);
    }
    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request)
    {
        String token = request.getHeaders().getFirst(TokenManager.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenManager.PREFIX))
        {
            token = token.replaceFirst(TokenManager.PREFIX, "");
        }
        if(token != null){
        	token = token.trim();
        }
        return token.trim();
    }
}
