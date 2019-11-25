package com.zhibinwang.pay.strategy;

import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.PaymentChannelDTO;
import com.zhibinwang.pay.entity.PaymentChannel;

/**
 * @author zhibin.wang
 * @create 2019-11-25 14:44
 * @desc 支付策略模式接口类
 **/
public interface PayStrategy {

    String toPayHtml(PaymentChannel paymentChannel, PayMentTransacInfoDTO payMentTransacInfoDTO);
}
