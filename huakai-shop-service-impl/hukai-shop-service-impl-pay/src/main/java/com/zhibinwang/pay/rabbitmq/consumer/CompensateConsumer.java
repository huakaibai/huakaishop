package com.zhibinwang.pay.rabbitmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentChannelExample;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.enu.PayStatu;
import com.zhibinwang.pay.mapper.PaymentChannelMapper;
import com.zhibinwang.pay.mapper.PaymentTransactionMapper;
import com.zhibinwang.pay.rabbitmq.config.RabbitmqConfig;
import com.zhibinwang.pay.strategy.PayStrategy;
import com.zhibinwang.pay.strategy.PayStrategyFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhibin.wang
 * @create 2019-11-29
 * @desc 支付订单补偿消费者,当收到mq消息说明，订单已经支付成功，1.可以防止网关重复提交，2.可以保证数据状态一致性
 **/
@Component
public class CompensateConsumer {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private PaymentChannelMapper channelMapper;

    /**
     * 消息补偿服务
     *
     * @param message
     * @param headers
     * @param channel
     * @throws IOException
     */

    @RabbitListener(queues = RabbitmqConfig.INTEGRAL_CREATE_QUEUE)
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        byte[] body = message.getBody();
        String jsonString = new String(body, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String paymentId = (String) jsonObject.get("paymentId");

        PaymentTransaction paymentTransaction = paymentTransactionMapper.selectByPrimaryKey(Long.valueOf(paymentId));
        Integer paymentStatus = paymentTransaction.getPaymentStatus();
        if (PayStatu.PAY_SUC.getValue() == paymentStatus || PayStatu.PAY_COMPLETE.getValue() == paymentStatus) {
            basicNack(message,channel);
            return ;
        }

        // 调用第三方对账查询订单
        String channelId = paymentTransaction.getChannelId();
        PaymentChannelExample example = new PaymentChannelExample();
        PaymentChannelExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        List<PaymentChannel> paymentChannels =  channelMapper.selectByExample(example);
        if (paymentChannels ==  null || paymentChannels.size() < 1){
            basicNack(message,channel);
            return ;
        }
        PaymentChannel paymentChannel = paymentChannels.get(0);
        PayStrategy payStrategy = PayStrategyFactory.getPayStrategy(paymentChannel.getClassName());
        boolean queryResult = payStrategy.payQuery(paymentChannel, paymentTransaction);
        // 支付成功
        if (queryResult){
            paymentTransaction.setPaymentStatus(PayStatu.PAY_SUC.getValue());
            paymentTransaction.setUpdatedTime(new Date());
            int i = paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);
            if (i > 0){
                basicNack(message,channel);
            }
            return ;
        }



    }

    /**
     * 手动接收确认
     * @param message
     * @param channel
     * @throws IOException
     */
    private void basicNack(Message message, Channel channel) throws IOException {
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);

    }

}
