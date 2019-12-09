package com.zhibinwang.spike.rabbitmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zhibinwang.spike.entity.HuakaiSeckill;
import com.zhibinwang.spike.entity.HukaiOrder;
import com.zhibinwang.spike.mapper.OrderMapper;
import com.zhibinwang.spike.mapper.SeckillMapper;
import com.zhibinwang.spike.rabbitmq.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author zhibin.wang
 * @create 2019-12-06 11:28
 * @desc 抢购订单消费者
 **/
@Component
@Slf4j
public class StockConsumer {


    @Autowired
    private SeckillMapper seckillMapper;


    @Autowired
    private OrderMapper orderMapper;

    /**
     * concurrency =  "10" 在注解中，这里可以采用消费者并发
     * @param message
     * @param headers
     * @param channel
     */
    @RabbitListener(queues = RabbitmqConfig.MODIFY_INVENTORY_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void updateRepertorys(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
        log.info(">>>messageId:{},msg:{}", messageId, msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String skellId = (String) jsonObject.get("skellId");
        String phone = (String) jsonObject.get("phone");
        String token = (String) jsonObject.get("token");

        // 查询sekilld;
        HuakaiSeckill seckill = seckillMapper.findById(Long.parseLong(skellId));
        if (seckill == null){
           log.warn("商品不能存在--》seckillid：{}",skellId);
           return ;
        }
        //修改库存准备
        Long version = seckill.getVersion();
        int result = seckillMapper.updateSeckill(Long.parseLong(skellId), version);
        if (result < 1){
            log.warn("秒杀失败，phone：{}，sekilid：{}",phone,skellId);
            return;
        }
        //增加订单数量
        HukaiOrder hukaiOrder = new HukaiOrder();
        hukaiOrder.setSeckillId(Long.parseLong(skellId));
        hukaiOrder.setUserPhone(phone);
        hukaiOrder.setCreateTime(new Date());
        //状态标示:-1:无效 0:成功 1:已付款 2:已发货
        hukaiOrder.setState((byte)1);
        int i = orderMapper.insertOrder(hukaiOrder);
        if (i > 0){
            // 消费成功后手动确认
            basicNack(message,channel);
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
