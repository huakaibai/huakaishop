package com.zhibinwang.intergal.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zhibinwang.intergal.mapper.IntegralMapper;
import com.zhibinwang.intergal.mapper.entity.IntegralEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author 花开
 * @create 2019-11-30 11:06
 * @desc rabbitmq 消费者
 **/
@Component
@Slf4j
public class IntergalConsumer {


    @Autowired
    private IntegralMapper integralMapper;


    @RabbitListener(queues = "integral_queue")
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {

        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(),"UTF-8");
        log.info(">>>messageId:{},msg:{}", messageId, msg);

        JSONObject jsonObject = JSONObject.parseObject(msg);
        String paymentId = (String) jsonObject.get("paymentId");
        if (StringUtils.isBlank(paymentId)){
            log.info("paymentId 不能为空,msg:{}",msg);
            basicNack(message,channel);
            return ;
        }

        IntegralEntity integralEntity = integralMapper.findIntegral(paymentId);
        if (integralEntity != null){
            log.info("积分已经增加,paymentid={}",paymentId);
            basicNack(message,channel);
            return ;

        }

        Integer userId = jsonObject.getInteger("userId");
        if (userId == null) {
            log.error(">>>>paymentId:{},对应的用户userId参数为空", paymentId);
            basicNack(message, channel);
            return;
        }
        Long integral = jsonObject.getLong("integral");
        if (integral == null) {
            log.error(">>>>paymentId:{},对应的用户integral参数为空", integral);
            return;
        }
        IntegralEntity integralEntity1 = new IntegralEntity();
        integralEntity1.setPaymentId(paymentId);
        integralEntity1.setIntegral(integral);
        integralEntity1.setUserId(userId);
        integralEntity1.setAvailability(1);
        // 插入到数据库中
        int insertIntegral = integralMapper.insertIntegral(integralEntity1);
        if (insertIntegral > 0) {
            // 手动签收消息,通知mq服务器端删除该消息
            basicNack(message, channel);
        }

    }





    /**
     * 消费者获取到消息之后 手动签收 通知MQ删除该消息
     * @param message
     * @param channel
     * @throws IOException
     */
    private void basicNack(Message message, Channel channel) throws IOException {
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }
}
