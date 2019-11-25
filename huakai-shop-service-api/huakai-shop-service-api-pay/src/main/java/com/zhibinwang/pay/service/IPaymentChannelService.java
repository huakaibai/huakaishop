package com.zhibinwang.pay.service;

import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PaymentChannelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author zhibin.wang
 * @create 2019-11-25 10:17
 * @desc 支付渠道信息接口
 **/
@Api("支付渠道接口")
@RequestMapping("/pay")
public interface IPaymentChannelService {

    @PostMapping("/selectAllChannel")
    @ApiOperation(value = "获取支付渠道信息")
    BaseResponse<List<PaymentChannelDTO>> selectAllChannel();
}
