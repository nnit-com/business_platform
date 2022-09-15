package com.nnit.pb.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关启动程序
 * @author LUFY
 *
 */
@SpringBootApplication
public class PbGateWayApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(PbGateWayApplication.class, args);
        System.out.println("网关启动成功!!");
    }
}
