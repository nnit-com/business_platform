package com.nnit.pb.task.controller;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nnit.pb.common.vm.ResponseData;
import com.nnit.pb.common.vm.Task;
import com.nnit.pb.task.pojo.SysUserTask;
import com.nnit.pb.task.service.SysUserTaskService;
import com.nnit.pb.task.vm.CallAFVM;
import com.nnit.pb.task.vm.SendMailVM;
import com.nnit.pb.task.vm.UserTaskInfo;
import com.nnit.pb.task.vm.UserTaskVM;
import com.nnit.pb.common.utils.JsonUtil;
import com.nnit.pb.api.annotation.RequiresRoles;
import com.nnit.pb.common.constant.RedisPrefixConstants;
import com.nnit.pb.common.constant.SecurityConstants;
import com.nnit.pb.common.constant.SysConstants;
import com.nnit.pb.common.redis.service.RedisService;

@RestController
public class PbTaskController {
	
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    private RedisService redisService;
    
    @Autowired
    private SysUserTaskService sysUserTaskService;
    
    @PostMapping("/taskapi/sendemail")
    public ResponseData sendMail(
    		@RequestBody SendMailVM sendMailVM,
    		@RequestHeader(SecurityConstants.USER_ID) Long userId,
    		@RequestHeader(SecurityConstants.USER_CODE) String userCode) {
    	ResponseData responseData = ResponseData.ok(); 	
    	SysUserTask sysUserTask = new SysUserTask();
    	sysUserTask.setUserId(userId);
    	sysUserTask.setTaskName(SysConstants.TASK_NAME_SENDEMAIL);
    	sysUserTask.setTaskStatus(SysConstants.TASK_STATUS_DRAFT);
    	sysUserTask.setCreateBy(userCode);
    	sysUserTask.setCreateTime(new Date());
    	sysUserTaskService.save(sysUserTask);
    	
    	Task task = new Task();
    	task.setTaskId(sysUserTask.getId());
    	task.setTaskName(sysUserTask.getTaskName());
    	task.setUserId(sysUserTask.getUserId());
    	task.setUserCode(userCode);
    	task.setStatus(sysUserTask.getTaskStatus());
    	HashMap parameters = new HashMap();
    	parameters.put("receiver", sendMailVM.getReceiver());
    	task.setParameters(parameters);    	
    	kafkaTemplate.send("task",JsonUtil.encode(task));
    	
    	refreshTaskList(userId, userCode);      	
    	return responseData;

    	
    }
    
    @RequiresRoles("user")
    @PostMapping("/taskapi/gettasklist")
    public ResponseData getTaskList(
    		@RequestHeader(SecurityConstants.USER_ID) Long userId,
    		@RequestHeader(SecurityConstants.USER_CODE) String userCode,
    		@RequestHeader(SecurityConstants.USER_KEY) String userKey) {
    	ResponseData responseData = ResponseData.ok();    	
    	UserTaskInfo taskInfo = redisService.getCacheObject(getTaskKey(userCode));
    	if(taskInfo == null){
    		taskInfo = refreshTaskList(userId, userCode);
    	}
    	responseData.putDataValue("taskList", taskInfo.getTaskList());
    	return responseData;
    }
    
    @PostMapping("/taskapi/updatetaskstatus")
    public void updateTaskStatus(@RequestBody Task task) {   
    	SysUserTask sysUserTask = sysUserTaskService.getById(task.getTaskId());
    	sysUserTask.setUpdateBy("pb-handler");
    	sysUserTask.setUpdateTime(new Date());
    	sysUserTask.setStartTime(task.getStartDate());
    	sysUserTask.setFinishTime(task.getFinishDate());
    	sysUserTask.setTaskResult(task.getTaskResult());
    	sysUserTask.setTaskStatus(task.getStatus());
    	sysUserTaskService.save(sysUserTask);
    	refreshTaskList(task.getUserId(), task.getUserCode());
    }
    
    @PostMapping("/taskapi/callaf")
    public ResponseData callAf(
    		@RequestBody CallAFVM callAFVM,
    		@RequestHeader(SecurityConstants.USER_ID) Long userId,
    		@RequestHeader(SecurityConstants.USER_CODE) String userCode) {
    	ResponseData responseData = ResponseData.ok(); 	
    	SysUserTask sysUserTask = new SysUserTask();
    	sysUserTask.setUserId(userId);
    	sysUserTask.setTaskName(SysConstants.TASK_NAME_CALLAF);
    	sysUserTask.setTaskStatus(SysConstants.TASK_STATUS_DRAFT);
    	sysUserTask.setCreateBy(userCode);
    	sysUserTask.setCreateTime(new Date());
    	sysUserTaskService.save(sysUserTask);
    	
    	Task task = new Task();
    	task.setTaskId(sysUserTask.getId());
    	task.setTaskName(sysUserTask.getTaskName());
    	task.setUserId(sysUserTask.getUserId());
    	task.setUserCode(userCode);
    	task.setStatus(sysUserTask.getTaskStatus()); 
    	HashMap parameters = new HashMap();
    	parameters.put("serverName", callAFVM.getServerName());
    	task.setParameters(parameters);  
    	kafkaTemplate.send("task",JsonUtil.encode(task));
    	
    	refreshTaskList(userId, userCode);      	
    	return responseData;

    	
    }
    
    /**
     * 更新用户任务列表到缓存
     * @param userId
     * @param userCode
     * @return
     */
    private UserTaskInfo refreshTaskList(Long userId, String userCode){
    	List<SysUserTask> userList = sysUserTaskService.findAllByUserid(userId);
    	List<UserTaskVM> userTaskVMList = new ArrayList<UserTaskVM>();
    	for(SysUserTask sysUserTask : userList){
    		UserTaskVM vm = new UserTaskVM();
    		vm.setTaskId(sysUserTask.getId());
    		vm.setTaskName(sysUserTask.getTaskName());
    		vm.setStatus(sysUserTask.getTaskStatus());
    		vm.setStartDate(sysUserTask.getStartTime());
    		vm.setFinishDate(sysUserTask.getFinishTime());
    		vm.setTaskResult(sysUserTask.getTaskResult());
    		userTaskVMList.add(vm);
    	}
    	UserTaskInfo taskInfo = new UserTaskInfo();
    	taskInfo.setTaskList(userTaskVMList);
        redisService.setCacheObject(getTaskKey(userCode), taskInfo, SysConstants.USER_EXPIRE_TIME, TimeUnit.MINUTES);
        return taskInfo;
    }

    private String getTaskKey(String userCode)
    {
        return RedisPrefixConstants.USER_TASK_LIST + userCode;
    }
}
