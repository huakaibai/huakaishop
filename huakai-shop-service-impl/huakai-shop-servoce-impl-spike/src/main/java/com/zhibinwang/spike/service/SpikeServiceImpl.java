package com.zhibinwang.spike.service;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.core.utils.RedisUtil;
import com.zhibinwang.spike.constant.SpikeConstants;
import com.zhibinwang.spike.entity.HuakaiSeckill;
import com.zhibinwang.spike.entity.HukaiOrder;
import com.zhibinwang.spike.mapper.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author zhibin.wang
 * @create 2019-12-05
 * @desc 秒杀服务实现类
 **/
@RestController
public class SpikeServiceImpl extends BaseApiService<JSONObject> implements  ISpikeService {

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private SeckillMapper seckillMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResponse<JSONObject> spike(String phone,  Long seckilld) {
        // 设置防止连续秒杀，10s后再来
        Boolean aBoolean = redisUtil.setIfAbsent(SpikeConstants.KEY_PRIX + phone, seckilld+"", SpikeConstants.KEY_TIME);
        if (aBoolean){
            return setResultError("系统繁忙，请稍后再试！");
        }
        // 查询sekilld;
        HuakaiSeckill seckill = seckillMapper.findById(seckilld);
        if (seckill == null){
            return setResultError("秒杀商品不存在");
        }
        //修改库存准备
        Long version = seckill.getVersion();
        int result = seckillMapper.updateSeckill(seckilld, version);
        if (result < 1){
            return setResultError("秒杀失败");
        }
        //增加订单数量
        HukaiOrder hukaiOrder = new HukaiOrder();
        hukaiOrder.setSeckillId(seckilld);
        hukaiOrder.setUserPhone(phone);
        hukaiOrder.setCreateTime(new Date());
        hukaiOrder.setState((byte)1); //状态标示:-1:无效 0:成功 1:已付款 2:已发货
        // 调用数据库增加订单数量
        return setResultSuccess("秒杀成功");
    }
}
