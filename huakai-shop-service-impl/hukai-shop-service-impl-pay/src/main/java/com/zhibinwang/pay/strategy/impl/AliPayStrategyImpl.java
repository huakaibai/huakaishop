package com.zhibinwang.pay.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.sdk.AliPayConfig;
import com.zhibinwang.pay.constant.PayConstant;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.strategy.PayStrategy;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author zhibin.wang
 * @create 2019-11-25 14:47
 * @desc 阿里支付策略模式实现类
 * @link https://docs.open.alipay.com/api_1/alipay.trade.page.pay
 **/
@Slf4j
public class AliPayStrategyImpl implements PayStrategy {

   static final AliPayConfig aliPayConfig = AliPayConfig.getAliPayConfig();

    @Override
    public String toPayHtml(PaymentChannel paymentChannel, PayMentTransacInfoDTO payMentTransacInfoDTO) {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no",payMentTransacInfoDTO.getId()+"");
        jsonObject.put("product_code","FAST_INSTANT_TRADE_PAY");
        jsonObject.put("total_amount",changeF2Y(payMentTransacInfoDTO.getPayAmount()+""));
        //订单标题
        jsonObject.put("subject",payMentTransacInfoDTO.getPayName());
        //订单描述
        jsonObject.put("body",payMentTransacInfoDTO.getPayName());
        //jsonObject.put("passback_params","8812");
     /*   JSONObject sysServiceObject = new JSONObject();
        sysServiceObject.put("sys_service_provider_id","");
        jsonObject.put("extend_params",sysServiceObject);*/
        CertAlipayRequest certAlipayRequest = getCertAlipayRequest(paymentChannel);

        //构造client
        AlipayClient alipayClient1 = null;
        String html = null;
        try {
            alipayClient1 = new DefaultAlipayClient(certAlipayRequest);
            //构造API请求
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(paymentChannel.getAsynUrl());
            //在公共参数中设置回跳和通知地址
            request.setNotifyUrl(paymentChannel.getSyncUrl());
            //填充业务参数
            request.setBizContent(jsonObject.toJSONString());
//发送请求

            AlipayTradePagePayResponse response = alipayClient1.pageExecute(request);
           html = response.getBody();
           log.info("支付宝交易html={}",html);
        } catch (AlipayApiException e) {
            e.printStackTrace();
           log.error("分装支付订单参数错误:",e);
        }

        return html;
    }

    private CertAlipayRequest getCertAlipayRequest(PaymentChannel paymentChannel) {
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
//设置网关地址 TODO
        certAlipayRequest.setServerUrl("https://openapi.alipaydev.com/gateway.do");
//设置应用Id
        certAlipayRequest.setAppId(paymentChannel.getMerchantId());
//设置应用私钥
        certAlipayRequest.setPrivateKey(paymentChannel.getPrivateKey());
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
        return certAlipayRequest;
    }

    @Override
    public boolean payQuery(PaymentChannel paymentChannel, PaymentTransaction paymentTransaction) {
        //获得初始化的AlipayClient
        CertAlipayRequest certAlipayRequest = getCertAlipayRequest(paymentChannel);
        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
     //   String out_trade_no = new String(request.getParameter("WIDTQout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号
        //String trade_no = new String(request.getParameter("WIDTQtrade_no").getBytes("ISO-8859-1"),"UTF-8");
        //请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ paymentTransaction.getId() +"\","+"\"trade_no\":\""+ paymentTransaction.getPartyPayId() +"\"}");
        AlipayClient alipayClient1 = null;
        try {
            alipayClient1 = new DefaultAlipayClient(certAlipayRequest);

            AlipayTradeQueryResponse response = alipayClient1.execute(alipayRequest);
            if (response.isSuccess()){
                String tradeStatus = response.getTradeStatus();
                if (PayConstant.PAY_RESULT_SUC_ALI.equals(tradeStatus) || PayConstant.PAY_RESULT_UNCOM_SUC_ALI.equals(tradeStatus)){
                    return true;
                }
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();

        }


        return false;
    }


    /** 金额为分的格式 */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            return null;
        }
        return BigDecimal.valueOf(Long.parseLong(amount)).divide(new BigDecimal(100)).toString();
    }
}
