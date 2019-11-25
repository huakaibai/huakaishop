package com.zhibinwang.pay.strategy.impl;

import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.strategy.PayStrategy;

/**
 * @author zhibin.wang
 * @create 2019-11-25 14:46
 * @desc 银联支付策略模式实现类
 **/
public class UnionPayStrategyImpl implements PayStrategy {


    @Override
    public String toPayHtml(PaymentChannel paymentChannel, PayMentTransacInfoDTO payMentTransacInfoDTO) {
        return null;
    }
}
