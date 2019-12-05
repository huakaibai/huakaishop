package com.zhibinwang.pay.serveice;

import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.core.bin.HuakaiBeanUtils;
import com.zhibinwang.core.token.GenerateToken;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.enu.PayStatu;
import com.zhibinwang.pay.mapper.PaymentTransactionMapper;
import com.zhibinwang.pay.service.IPayMentTransacInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author zhibin.wang
 * @create 2019-11-25
 * @desc 支付订单信息实现接口
 **/
@RestController
public class PayMentTransacInfoServiceImpl extends BaseApiService<PayMentTransacInfoDTO> implements IPayMentTransacInfoService {

    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;


    @Override
    public BaseResponse<PayMentTransacInfoDTO> getPayTransactionInfoByToken(String token) {
        String payId = generateToken.getToken(token);
        if (StringUtils.isBlank(payId)){
            return setResultError("token无效或者已经过期！");
        }

        PaymentTransaction paymentTransaction = paymentTransactionMapper.selectByPrimaryKey(Long.valueOf(payId));
        if (paymentTransaction == null){
            return setResultError("订单不存在");
        }
        if (PayStatu.UN_PAY.getValue() != paymentTransaction.getPaymentStatus()){
            return setResultError("订单支付状态不正确");
        }
        return setResultSuccessByT(paymentTransaction);
    }


}
