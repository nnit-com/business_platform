package com.nnit.pb.handler.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnit.pb.api.RemoteAFService;
import com.nnit.pb.api.RemoteTaskService;
import com.nnit.pb.common.constant.SysConstants;
import com.nnit.pb.common.vm.Task;
import com.nnit.pb.handler.af.AFResponseData;
import com.nnit.pb.handler.af.AFManager;
import com.nnit.pb.handler.service.TaskHandlerService;
import com.nnit.pb.handler.util.MailSender;

@Service
public class TaskHandlerServiceImpl implements TaskHandlerService{
	
	private final static Logger log = LoggerFactory.getLogger(TaskHandlerServiceImpl.class);

	@Autowired
	MailSender mailSender;
	
	@Autowired
	private AFManager afManager;
	
	@Autowired
	private RemoteTaskService remoteTaskService;
	
	public String handleTask(Task task){
		try {			
			task.setStartDate(new Date());//设置开始时间
			if(task.getTaskName() == null){
				task.setStatus(SysConstants.TASK_STATUS_FAILED);
				task.setTaskResult("Task name is null");
			}else{
				if(SysConstants.TASK_NAME_SENDEMAIL.equals(task.getTaskName())){
		        	
		        	String receiver = String.valueOf(task.getParameters().get("receiver"));
		        	if(mailSender.sendMail(receiver, "Backend task handler test", "Backend task handler test", true)){
		        		task.setStatus(SysConstants.TASK_STATUS_SUCCESS);
		        	}else{
		        		task.setStatus(SysConstants.TASK_STATUS_FAILED);
		        	}
		        	
		        }else if(SysConstants.TASK_NAME_CALLAF.equals(task.getTaskName())){
		        	String serverName = String.valueOf(task.getParameters().get("serverName"));
		        	AFResponseData afResponseData = afManager.callMoule(serverName);
		        	if("success".equals(afResponseData.getAfStatus())){
		        		task.setStatus(SysConstants.TASK_STATUS_SUCCESS);
		        		task.setTaskResult(afResponseData.getAfResult());
		        	}else{
		        		task.setStatus(SysConstants.TASK_STATUS_FAILED);
		        		task.setTaskResult(afResponseData.getError());
		        	}
		        } 
			}	
		} catch (Exception e) {			
			log.error("HandleTask exception", e);
			task.setStatus(SysConstants.TASK_STATUS_FAILED);
    		task.setTaskResult(e.getMessage());
		}
		task.setFinishDate(new Date());//设置结束时间
		remoteTaskService.updateTaskStatus(task);
		return String.valueOf(SysConstants.TASK_STATUS_SUCCESS);
		
	}
	
	
}
