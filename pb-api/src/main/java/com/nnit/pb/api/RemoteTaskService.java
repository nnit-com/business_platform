package com.nnit.pb.api;

import org.springframework.cloud.openfeign.FeignClient;





import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nnit.pb.api.factory.RemoteTaskServiceFallbackFactory;
import com.nnit.pb.common.vm.Task;


@FeignClient(contextId = "remoteTaskService", value = "pb-task", fallbackFactory = RemoteTaskServiceFallbackFactory.class)
public interface RemoteTaskService {
     @PostMapping(value = "/taskapi/updatetaskstatus")
	 public void updateTaskStatus(@RequestBody Task task);
}
