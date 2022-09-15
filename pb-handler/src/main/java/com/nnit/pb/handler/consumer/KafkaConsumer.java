package com.nnit.pb.handler.consumer;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.nnit.pb.common.utils.JsonUtil;
import com.nnit.pb.common.vm.Task;
import com.nnit.pb.handler.service.TaskHandlerService;
import com.nnit.pb.handler.util.GetProperties;
import com.nnit.pb.handler.util.MailSender;

@Component
public class KafkaConsumer {
	
	@Autowired
	TaskHandlerService taskHandlerService;
	
	@KafkaListener(topics = {"task"}, groupId = "taskHandleGroup")
    public void onMessage1(ConsumerRecord<?, ?> consumerRecord,Acknowledgment ack) {
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object msg = optional.get();
            System.out.println("get remote info:"+msg);
            Task task = JsonUtil.decode(msg.toString(),  Task.class);
            
            taskHandlerService.handleTask(task);
            
            System.out.println("change to class:"+task);
            ack.acknowledge();
        }
    }

	
}
