package com.zhibinwang.pay.callback.impl;

import com.union.sdk.AcpService;
import com.union.sdk.SDKConstants;
import com.zhibinwang.pay.constant.PayConstant;
import com.zhibinwang.pay.callback.AbstractPayCallbackTemplate;
import com.zhibinwang.pay.entity.PaymentTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhibin.wang
 * @create 2019-11-27 15:21
 * @desc 银联异步回调通知接口
 **/
@Slf4j
@Component
public class UnionPayCallbackTemplateImpl extends AbstractPayCallbackTemplate {

    static final String logPrefix = "【银联支付回调通知】";

    @Override
    protected Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) {


        log.info("==={}支付回调开始===", logPrefix);

        String encoding = req.getParameter(SDKConstants.param_encoding);

        Map<String, String> respParam = getAllRequestParam(req);

        // 打印请求报文
        log.info("{}通知请求参数:reqParam={}", logPrefix, respParam);

        Map<String, String> valideData = null;
        if (null != respParam && !respParam.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = respParam.entrySet()
                    .iterator();
            valideData = new HashMap(respParam.size());
            while (it.hasNext()) {
                Map.Entry<String, String> e = it.next();
                String key = e.getKey();
                String value = e.getValue();
                try {
                    value = new String(value.getBytes(encoding), encoding);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }

                valideData.put(key, value);
            }
        }

        if (!AcpService.validate(valideData, encoding)) {
            valideData.put(PayConstant.VALIDATE_RESULT, PayConstant.VALIDATE_RESULT_FAIL);

            log.info("{}验证签名失败,orderId={}", logPrefix, valideData.get("orderId"));
        } else {

            valideData.put(PayConstant.VALIDATE_RESULT, PayConstant.VALIDATE_RESULT_SUC);
            log.info("{}验证签名成功,orderId={}", logPrefix, valideData.get("orderId"));

        }

        valideData.put(PayConstant.PAY_CHANNEL, PayConstant.PAY_CHANNEL_UNION);
        valideData.put(PayConstant.PAY_ID, valideData.get("orderId"));
        valideData.put(PayConstant.PAY_MONEY, valideData.get("txnAmt"));
        return valideData;
    }

    @Override
    protected String fail() {
        return PayConstant.PAY_RESPONSE_FAIL_UNION;
    }

    @Override
    protected String success() {
        return PayConstant.PAY_RESPONSE_SUCESS_UNION;
    }

    @Override
    protected String asyService(Map<String, String> result) {

        String payId = result.get("orderId");
        String respCode = result.get("respCode");
        String partyId = result.get("queryId");
        if (PayConstant.PAY_RESULT_SUC_UNION.equals(respCode) || PayConstant.PAY_RESULT_UNCOM_SUC_UNION.equals(respCode)) {
            // 更新订单支付成功
            int updateResult = updatePaymentTransactionSuc(Long.valueOf(payId), partyId);
            if (updateResult != 1) {
                log.info("{}更新数据库状态失败，orderId={}", logPrefix, payId);
                return fail();
            }
            PaymentTransaction paymentTransaction = getPaymentTransactionByid(Long.parseLong(payId));

            //发送通知到qctivemq
            addIntegral(paymentTransaction);
            log.info("{}更新数据库状态成功，支付完成，orderId={}", logPrefix, payId);
            return success();
        }
        // qi它支付状态处理
        //按我的设想第三方支付平台会有一个回调通知次数，所有的次数的都用完则将数据库状态更新为支付失败
        // ，否则不处理数据库数据状态，返回应答失败 TODO

        //   System.out.println(valideData.get("orderId"));

        //   String respCode = valideData.get("respCode");
        //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
        return fail();

    }


    /**
     * 获取请求参数中所有的信息
     * 当商户上送frontUrl或backUrl地址中带有参数信息的时候，
     * 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(
            final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    // System.out.println("======为空的字段名===="+en);
                    res.remove(en);
                }
            }
        }
        return res;
    }

}
