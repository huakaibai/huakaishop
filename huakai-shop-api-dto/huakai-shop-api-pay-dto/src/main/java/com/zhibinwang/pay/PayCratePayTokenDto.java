package com.zhibinwang.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhibin.wang
 * @create 2019-11-22 14:09
 * @desc 订单预支付接口，防止参数被篡改
 **/
@Data
@ApiModel(value = "订单预支付")
public class PayCratePayTokenDto {


    @ApiModelProperty(value = "订单ID")
    @NotBlank(message = "订单id不能为空")
    private String orderId;


    @ApiModelProperty(value = "支付金额")
    @NotNull(message = "支付金额不能为空")
    private Long payAmount;


    @ApiModelProperty(value = "用户信息")
    @NotBlank(message = "用户Id不能为空")
    private String userId;

}
