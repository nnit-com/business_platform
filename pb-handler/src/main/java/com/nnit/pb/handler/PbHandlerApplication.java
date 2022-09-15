package com.nnit.pb.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;





/**
 * 后台任务处理微服务
 * @author YWZZ
 *
 */
@EnableFeignClients(basePackages="com.nnit.pb.api")
@SpringBootApplication
public class PbHandlerApplication 
{
	
    public static void main( String[] args )
    {
        SpringApplication.run(PbHandlerApplication.class, args);
        System.out.println("后台任务处理微服务启动成功");
   }

}
