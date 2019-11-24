package com.zhibinwang.pay.serveice;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.token.GenerateToken;
import com.zhibinwang.pay.PayCratePayTokenDto;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.enu.PayStatu;
import com.zhibinwang.pay.mapper.PaymentTransactionMapper;
import com.zhibinwang.pay.service.IPayMentTransacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author zhibin.wang
 * @create 2019-11-22 14:26
 * @desc 支付实现接口
 **/
@RestController
public class PayTransactionServiceImpl extends BaseApiService<JSONObject> implements IPayMentTransacService {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private  GenerateToken generateToken;



    private static Snowflake snowflake = IdUtil.createSnowflake(1, 1);
    @Override
    public BaseResponse<JSONObject> cratePayToken(@RequestBody PayCratePayTokenDto payCratePayTokenDto) {

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setOrderId(payCratePayTokenDto.getOrderId());
        paymentTransaction.setPayAmount(payCratePayTokenDto.getPayAmount());
        // 0待支付1已经支付2支付超时3支付失败'
        paymentTransaction.setPaymentStatus(PayStatu.UN_PAY.getValue());
        paymentTransaction.setRevision(0);
        paymentTransaction.setCreatedTime(new Date());
        paymentTransaction.setUpdatedTime(new Date());
        paymentTransaction.setId(snowflake.nextId());
        paymentTransactionMapper.insert(paymentTransaction);

        //根据id生成token

        String token = generateToken.createToken(Constants.PAY_TOKEN_PRIX, paymentTransaction.getId() + "");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);


        return setResultSuccess(jsonObject) ;
    }


}
