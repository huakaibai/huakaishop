package com.zhibinwang.spike.service;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.core.token.GenerateToken;
import com.zhibinwang.core.utils.RedisUtil;
import com.zhibinwang.spike.constant.SpikeConstants;
import com.zhibinwang.spike.entity.HuakaiSeckill;
import com.zhibinwang.spike.mapper.SeckillMapper;
import com.zhibinwang.spike.rabbitmq.producer.SpikeComodityProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhibin.wang
 * @create 2019-12-05
 * @desc 秒杀服务实现类
 **/
@RestController
@Slf4j
public class SpikeServiceImpl extends BaseApiService<JSONObject> implements  ISpikeService {

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private SeckillMapper seckillMapper;


    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private SpikeComodityProducer spikeComodityProducer;


    @Override
    @HystrixCommand(fallbackMethod = "generateTokenForSkill",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000"), //服务调用超时时间
            @HystrixProperty(name = "execution.isolation.strategy ",value = "Thread") //HystrixCommand隔离级别：Thread表示重新开始一个线程执行回滚；Semaphore表示在当前调用线程执行回滚
    },threadPoolProperties = {
            @HystrixProperty(name = "coreSize",value = "1"),
            @HystrixProperty(name = "maxQueueSize",value = "10")
    })

    public BaseResponse<JSONObject> spike(String phone,  Long seckilld) {
        // 设置防止连续秒杀，10s后再来
/*        Boolean aBoolean = redisUtil.setIfAbsent(SpikeConstants.KEY_PRIX + phone, seckilld+"", SpikeConstants.KEY_TIME);
        if (aBoolean){
            return setResultError("系统繁忙，请稍后再试！");
        }*/
        log.info(">>>>>>秒杀服务接口:spike()线程名称:" + Thread.currentThread().getName());
        //根据秒杀id获取令牌桶
        String tokenBucket = generateToken.getTokenBucket(SpikeConstants.TOKEN_PRIX + seckilld);


        if (StringUtils.isBlank(tokenBucket)){
            // 说明已经被抢购完了
            return setResultError("商品被抢购空了");
        }
        //异步发送mq修改库存
        asyncSendMq(phone,seckilld+"",tokenBucket);
        return setResultSuccess("秒杀成功");
    }

    @Override
    public BaseResponse<JSONObject> generateSeckillToken(String seckilId) {
        HuakaiSeckill seckill = seckillMapper.findById(Long.parseLong(seckilId));
        if (seckill == null){
            return  setResultError("秒杀商品不存在");
        }
        if (seckill.getInventory() == 0){
            return  setResultError("库存为0");
        }

        generateTokenForSkill(seckilId,seckill.getInventory());


        return setResultSuccess("正在生成令牌中");
    }

    @Override
    public BaseResponse<JSONObject> querySeckillResult(String phone, String secKillId) {
        //1.先根据手机号和秒杀号，查询是否有记录； TODO

        //2.没有记录，查询库存是否为0
        HuakaiSeckill seckill = seckillMapper.findById(Long.parseLong(secKillId));
        if (seckill == null){
            return  setResultError("秒杀失败");
        }
        if (seckill.getInventory() == 0){
            return  setResultError("库存为0");
        }

        return setResultError("正在排队中");
    }


    @Async
    protected void asyncSendMq(String phone,String  skellId,String token){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("skellId", skellId);
        jsonObject.put("token", token);

        spikeComodityProducer.send(jsonObject);
    }


    @Async
    protected void generateTokenForSkill(String seckilId,int num){

        try {
            generateToken.creteTokenBucket(SpikeConstants.TOKEN_PRIX + seckilId,num,null);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private BaseResponse<JSONObject> spikeFallBack(String phone,  Long seckilld){
        return setResultError("服务器繁忙，请稍后再试！");
    }
}
