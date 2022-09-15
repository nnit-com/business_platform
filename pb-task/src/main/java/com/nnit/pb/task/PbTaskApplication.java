package com.nnit.pb.task;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * 任务拆分展示微服务
 * @author YWZZ
 *
 */
@SpringBootApplication
public class PbTaskApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(PbTaskApplication.class, args);
        System.out.println("task拆分展示微服务启动成功");
    }
}
