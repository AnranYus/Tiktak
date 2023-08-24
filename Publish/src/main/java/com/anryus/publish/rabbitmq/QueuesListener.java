package com.anryus.publish.rabbitmq;

import com.anryus.publish.mapper.PublishMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueuesListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    final PublishMapper publishMapper;

    public QueuesListener(PublishMapper publishMapper) {
        this.publishMapper = publishMapper;
    }

    @RabbitListener(queues = "publishRollback")
    public void rollbackAction(Message message){
        String video_id = new String(message.getBody());
        int i = publishMapper.deleteById(video_id);
        if (i>0){
            logger.warn("Publish rollback for: "+ video_id);

        }else {
            logger.error("Publish rollback error! Can not change any row");
        }


    }
}
