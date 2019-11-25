package com.zhibinwang.pay.strategy.impl;

import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.strategy.PayStrategy;

/**
 * @author zhibin.wang
 * @create 2019-11-25 14:47
 * @desc 阿里支付策略模式实现类
 **/
public class AliPayStrategyImpl implements PayStrategy {

    @Override
    public String toPayHtml(PaymentChannel paymentChannel, PayMentTransacInfoDTO payMentTransacInfoDTO) {
        return null;
    }
}
