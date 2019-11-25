package com.zhibinwang.pay.serveice;

import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PaymentChannelDTO;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.mapper.PaymentChannelMapper;
import com.zhibinwang.pay.service.IPaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhibin.wang
 * @create 2019-11-25 10:21
 * @desc 支付渠道实现类
 **/
@RestController
public class PaymentChannelService extends BaseApiService<List<PaymentChannelDTO>> implements IPaymentChannelService {


    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    public BaseResponse<List<PaymentChannelDTO>> selectAllChannel() {
        List<PaymentChannel> paymentChannels = paymentChannelMapper.selectByExample(null);

        return null;
    }
}
