package com.zhibinwang.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhibin.wang
 * @create 2019-11-25
 * @desc 支付渠道详细信息
 **/
@Data
@ApiModel(value = "支付渠道信息")
public class PaymentChannelDTO {

    @ApiModelProperty(value = "渠道id")
    private String channelId;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道状态")
    private Integer channelState;

}
