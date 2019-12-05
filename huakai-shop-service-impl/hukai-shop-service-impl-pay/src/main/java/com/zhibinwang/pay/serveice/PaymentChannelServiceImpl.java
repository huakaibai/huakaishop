package com.zhibinwang.pay.serveice;

import com.zhibinwang.core.mapper.MapperUtils;
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
public class PaymentChannelServiceImpl implements IPaymentChannelService {


    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    @Override
    public List<PaymentChannelDTO> selectAllChannel() {
        List<PaymentChannel> paymentChannels = paymentChannelMapper.selectByExample(null);
        List<PaymentChannelDTO> paymentChannelDTOS = MapperUtils.mapAsList(paymentChannels, PaymentChannelDTO.class);
        return paymentChannelDTOS;
    }


}
