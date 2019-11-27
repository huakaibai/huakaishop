package com.zhibinwang.pay.Constants;

/**
 * @author zhibin.wang
 * @create 2019-11-27 15:39
 * @desc 支付常量
 **/
public interface PayConstant {


    //阿里支付
    String PAY_CHANNEL_ALI  = "1";

    //银联支付
    String PAY_CHANNEL_UNION = "2";

    String PAY_CHANNEL = "payChannel";

    //验证签名结果
    String VALIDATE_RESULT = "validateResult";

    String VALIDATE_RESULT_FAIL = "201";

    String VALIDATE_RESULT_SUC = "200";

    String PAY_ID = "payId";

    String PAY_MONEY = "payMoney";


    String PAY_RESPONSE_SUCESS_UNION = "ok";
    String PAY_RESPONSE_FAIL_UNION = "fail";

    String PAY_RESPONSE_SUCESS_ALI = "success";
    String PAY_RESPONSE_FAIL_ALI = "fail";




    String PAY_RESULT_SUC_UNION = "00";
    String PAY_RESULT_UNCOM_SUC_UNION = "A6";


    String PAY_RESULT_SUC_ALI = "TRADE_SUCCESS";
    String PAY_RESULT_UNCOM_SUC_ALI = "TRADE_FINISHED";

    String PAY_CHANNEL_ID_ALI = "AA";
}
