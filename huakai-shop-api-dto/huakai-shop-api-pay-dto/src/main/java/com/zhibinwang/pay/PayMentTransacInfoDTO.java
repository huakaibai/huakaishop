package com.zhibinwang.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhibin.wang
 * @create 2019-11-25
 * @desc 制定订单信息
 **/
@Data
@ApiModel(value = "订单支付详细信息")
public class PayMentTransacInfoDTO {


    @ApiModelProperty(value = "订单ID")
    private String orderId;


    @ApiModelProperty(value = "支付金额")
    private Long payAmount;


    @ApiModelProperty(value = "用户信息")
    private Integer userId;
    @ApiModelProperty(value = "支付状态")
    private Integer paymentStatus;

    @ApiModelProperty(value = "商品名称")
    private String payName;

    @ApiModelProperty(value = "第三方支付id")
    private String partyPayId;
    @ApiModelProperty(value = "支付订单id")
    private Long id;
}
