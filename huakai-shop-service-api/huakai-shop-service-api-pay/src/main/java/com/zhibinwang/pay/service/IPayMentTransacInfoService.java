package com.zhibinwang.pay.service;

import com.google.gson.JsonObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PayCratePayTokenDto;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

/**
 * @author zhibin.wang
 * @create 2019-11-25 9:25
 * @desc 获取支付订单信息
 **/
@Api("支付获取订单信息")
@Validated
public interface IPayMentTransacInfoService {

    @GetMapping("/getPayTransactionInfoByToken")
    @ApiOperation(value = "根据token获取订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "Token信息")
    })
    BaseResponse<PayMentTransacInfoDTO> getPayTransactionInfoByToken(@RequestParam("token") @NotBlank(message = "token 不能为空") String token);
}
