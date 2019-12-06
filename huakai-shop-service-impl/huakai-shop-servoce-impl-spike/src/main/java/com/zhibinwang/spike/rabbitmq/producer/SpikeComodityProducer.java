package com.zhibinwang.spike.rabbitmq.producer;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.spike.rabbitmq.config.RabbitmqConfig;
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
 * @create 2019-12-06 9:55
 * @desc 秒杀抢购订单生产者 进行发送确人，确认时到交换机的
 **/
@Component
@Slf4j
public class SpikeComodityProducer implements RabbitTemplate.ConfirmCallback {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送修改库存消息到mq
     *

     */
    public void send(JSONObject jsonObject) {
/*        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("skellId", skellId);
        jsonObject.put("token", token);*/
        String jsonString = jsonObject.toJSONString();
        String messageId = IdUtil.simpleUUID();
        Message message = MessageBuilder.withBody(jsonString.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8")
                //设置全局唯一消息id
                .setMessageId(messageId)
                .build();
        rabbitTemplate.setMandatory(true);
        //设置confirmcallback实现类 发送确认
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(jsonString);
        rabbitTemplate.convertAndSend(RabbitmqConfig.MODIFY_INVENTORY_QUEUE, RabbitmqConfig.MODIFY_ROUTING_KEY, message, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String jsonString = correlationData.getId();
        log.info("发送确认机制，消息id：{}", jsonString);
        if (ack) {
            log.info("发送确认机制，发送成功，消息id：{}", jsonString);
        } else {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            send(jsonObject);
            log.info("发送确认机制,发送失败，进行重发,消息id：{}", jsonString);
        }
    }
}

