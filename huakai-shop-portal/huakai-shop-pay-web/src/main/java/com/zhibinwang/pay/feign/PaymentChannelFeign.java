package com.zhibinwang.pay.feign;

import com.zhibinwang.pay.service.IPaymentChannelService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhibin.wang
 * @create 2019-11-25 11:07
 * @desc 支付渠道feign接口
 **/
@FeignClient("app-huakai-pay")
public interface PaymentChannelFeign extends IPaymentChannelService {
}
