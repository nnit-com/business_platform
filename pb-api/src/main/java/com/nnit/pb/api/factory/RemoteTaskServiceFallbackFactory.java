package com.nnit.pb.api.factory;


import org.slf4j.Logger;



import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Component;

import com.nnit.pb.api.RemoteTaskService;
import com.nnit.pb.common.vm.Task;

import feign.hystrix.FallbackFactory;




@Component
public class RemoteTaskServiceFallbackFactory implements FallbackFactory<RemoteTaskService>
{    
	private static final Logger log = LoggerFactory.getLogger(RemoteTaskServiceFallbackFactory.class);

	@Override
	public RemoteTaskService create(Throwable throwable) {
        log.error("pb-task服务调用失败:{}", throwable.getMessage());
        return new RemoteTaskService()
        {       
			@Override
			public void updateTaskStatus(Task task) {

			}
        };
	}

    
}
