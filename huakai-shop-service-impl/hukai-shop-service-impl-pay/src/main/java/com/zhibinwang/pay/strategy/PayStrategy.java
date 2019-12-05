package com.zhibinwang.pay.strategy;

import com.alipay.api.AlipayApiException;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentTransaction;

/**
 * @author zhibin.wang
 * @create 2019-11-25
 * @desc 支付策略模式接口类
 **/
public interface PayStrategy {

    String toPayHtml(PaymentChannel paymentChannel, PayMentTransacInfoDTO payMentTransacInfoDTO) throws AlipayApiException;

    /**
     * 根据订单id查询交易是否成功
     * @param paymentChannel
     * @param paymentTransaction
     * @return
     */
    boolean payQuery(PaymentChannel paymentChannel, PaymentTransaction paymentTransaction);
}
