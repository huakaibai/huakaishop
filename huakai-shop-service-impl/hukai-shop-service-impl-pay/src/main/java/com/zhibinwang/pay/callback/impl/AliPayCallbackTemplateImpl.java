package com.zhibinwang.pay.callback.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.sdk.AliPayConfig;
import com.zhibinwang.pay.Constants.PayConstant;
import com.zhibinwang.pay.callback.AbstractPayCallbackTemplate;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentChannelExample;
import com.zhibinwang.pay.mapper.PaymentChannelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zhibin.wang
 * @create 2019-11-27 17:05
 * @desc 阿里支付异步通知处理模板实现类
 **/
@Slf4j
@Component
public class AliPayCallbackTemplateImpl extends AbstractPayCallbackTemplate {
    static final String logPrefix = "【支付宝支付回调通知】";
    static final AliPayConfig aliPayConfig = AliPayConfig.getAliPayConfig();


    @Autowired
    private PaymentChannelMapper paymentChannelMapper;


    @Override
    protected Map<String, String> verifySignature(HttpServletRequest request, HttpServletResponse resp) {
        Map<String,String> params = new HashMap();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name =  iter.next();
            String[] values =  requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }
        // 打印请求报文
        log.info("{}通知请求参数:reqParam={}", logPrefix, params);


        // 根据支付渠道id获取渠道信息
        PaymentChannelExample example = new PaymentChannelExample();
        PaymentChannelExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(PayConstant.PAY_CHANNEL_ID_ALI);
        // 渠道信息0可用，1不可用
        criteria.andChannelStateEqualTo(0);
        List<PaymentChannel> paymentChannels = paymentChannelMapper.selectByExample(example);
        PaymentChannel paymentChannel = paymentChannels.get(0);
        boolean signVerified = false;
        try {
            signVerified  = AlipaySignature.rsaCheckV1(params, paymentChannel.getPublicKey(), aliPayConfig.getCharset(), aliPayConfig.getSignType()); //调用SDK验证签名
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (!signVerified){
            params.put(PayConstant.VALIDATE_RESULT, PayConstant.VALIDATE_RESULT_FAIL);

            log.info("{}验证签名失败,orderId={}", logPrefix, params.get("orderId"));
        }else {
            params.put(PayConstant.VALIDATE_RESULT, PayConstant.VALIDATE_RESULT_SUC);
            log.info("{}验证签名成功,orderId={}", logPrefix, params.get("orderId"));
        }
        params.put(PayConstant.PAY_CHANNEL, PayConstant.PAY_CHANNEL_ALI);
        params.put(PayConstant.PAY_ID, params.get("out_trade_no"));
        params.put(PayConstant.PAY_MONEY, params.get("total_amount"));
        return params;
    }

    @Override
    protected String fail() {
        return  PayConstant.PAY_RESPONSE_FAIL_ALI;
    }

    @Override
    protected String success() {
        return PayConstant.PAY_RESPONSE_SUCESS_ALI;
    }

    @Override
    protected String asyService(Map<String, String> result) {

/*        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");*/

        String payId = result.get("orderId");
        String respCode = result.get("trade_status");
        String partyId = result.get("trade_no");
        if (PayConstant.PAY_RESULT_SUC_ALI.equals(respCode) || PayConstant.PAY_RESULT_UNCOM_SUC_ALI.equals(respCode)) {
            // 更新订单支付成功
            int updateResult = updatePaymentTransactionSuc(Long.valueOf(payId), partyId);
            if (updateResult != 1) {
                log.info("{}更新数据库状态失败，orderId={}", logPrefix, payId);
                return fail();
            }

            log.info("{}更新数据库状态成功，支付完成，orderId={}", logPrefix, payId);
            return success();
        }

        return fail();

/*
            // qi它支付状态处理
        //按我的设想第三方支付平台会有一个回调通知次数，所有的次数的都用完则将数据库状态更新为支付失败
        // ，否则不处理数据库数据状态，返回应答失败 TODO
      if(trade_status.equals("TRADE_FINISHED")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
        }else if (trade_status.equals("TRADE_SUCCESS")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //付款完成后，支付宝系统发送该交易状态通知
        }*/




    }
}
