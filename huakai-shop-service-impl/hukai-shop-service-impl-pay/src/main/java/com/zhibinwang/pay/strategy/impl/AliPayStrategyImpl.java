package com.zhibinwang.pay.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.sdk.AliPayConfig;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.strategy.PayStrategy;

/**
 * @author zhibin.wang
 * @create 2019-11-25 14:47
 * @desc 阿里支付策略模式实现类
 **/
public class AliPayStrategyImpl implements PayStrategy {

    @Override
    public String toPayHtml(PaymentChannel paymentChannel, PayMentTransacInfoDTO payMentTransacInfoDTO) throws AlipayApiException {
     /*   AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址


        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101001\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }*/
        AliPayConfig aliPayConfig = AliPayConfig.getAliPayConfig();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no",payMentTransacInfoDTO.getOrderId());
        jsonObject.put("product_code","FAST_INSTANT_TRADE_PAY");
        jsonObject.put("total_amount","8812");
        jsonObject.put("subject","8812");
        jsonObject.put("body","8812");
        jsonObject.put("passback_params","8812");
        JSONObject sysServiceObject = new JSONObject();
        sysServiceObject.put("sys_service_provider_id","");
        jsonObject.put("extend_params",sysServiceObject);

        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
//设置网关地址
        certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
//设置应用Id
       // certAlipayRequest.setAppId(app_id);
//设置应用私钥
     //   certAlipayRequest.setPrivateKey(privateKey);
//设置请求格式，固定值json
        certAlipayRequest.setFormat("json");
//设置字符集
        certAlipayRequest.setCharset(aliPayConfig.getCharset());
//设置签名类型
        certAlipayRequest.setSignType(aliPayConfig.getSignType());
//设置应用公钥证书路径
        certAlipayRequest.setCertPath(aliPayConfig.getApplicationCertPath());
//设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(aliPayConfig.getAppCertPath());
//设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(aliPayConfig.getAlipayRootCertPath());
//构造client
        AlipayClient alipayClient1 = new DefaultAlipayClient(certAlipayRequest);
//构造API请求
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setReturnUrl(paymentChannel.getAsynUrl());
        request.setNotifyUrl(paymentChannel.getSyncUrl());//在公共参数中设置回跳和通知地址
        request.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101001\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
//发送请求
        AlipayTradeQueryResponse response = alipayClient1.pageExecute(request);

        return null;
    }
}
