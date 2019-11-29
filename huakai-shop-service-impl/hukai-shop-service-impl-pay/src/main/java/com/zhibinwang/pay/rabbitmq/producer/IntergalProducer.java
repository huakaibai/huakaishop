package com.zhibinwang.pay.rabbitmq.producer;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.pay.rabbitmq.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhibin.wang
 * @create 2019-11-29
 * @desc 积分消费者发送到mq
 * 发送确认模式
 **/
@Component
@Slf4j
public class IntergalProducer implements RabbitTemplate.ConfirmCallback  {

    @Autowired
    private RabbitTemplate rabbitTemplate;



    public void send(JSONObject jsonObject){
        String paymentId = jsonObject.getString("paymentId");
        String jsonString = jsonObject.toJSONString();
        log.info("发送到mq消息id：{}，消息内容{}",paymentId,jsonString);

        Message message = MessageBuilder.withBody(jsonString.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8")
                //设置全局唯一消息id
                .setMessageId(paymentId)
                .build();
        // 构建回调返回的数据（消息id）
        this.rabbitTemplate.setMandatory(true);
        //设置confirmcallback实现类
        rabbitTemplate.setConfirmCallback(this);

        CorrelationData correlationData = new CorrelationData(jsonString); // 用来进行消息对比

        // 发送消息 参数：交换机，路由key，消息，唯一数据
        rabbitTemplate.convertAndSend(RabbitmqConfig.INTEGRAL_EXCHANGE_NAME,RabbitmqConfig.INTEGRAL_ROUTING_KEY,message,correlationData);


    }

    // 这个只是针对是否发送到交换机，还有一个是发送到消费队列

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String jsonString = correlationData.getId();
        log.info("发送确认机制，消息id：{}",jsonString);
        if (ack){
            log.info("发送确认机制，发送成功，消息id：{}",jsonString);
        }else {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            send(jsonObject);
            log.info("发送确认机制,发送失败，进行重发,消息id：{}",jsonString);
        }
    }
}
