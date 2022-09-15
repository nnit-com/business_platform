package com.nnit.pb.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;



/**
 * 认证授权中心
 * @author LUFY
 *
 */
@EnableFeignClients(basePackages="com.nnit.pb.api")
@SpringBootApplication
public class PbAuthApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(PbAuthApplication.class, args);
        System.out.println("认证授权中心启动成功");
    }
}
