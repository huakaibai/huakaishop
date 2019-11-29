package com.zhibinwang.pay.callback;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.pay.Constants.PayConstant;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.entity.PaymentTransactionExample;
import com.zhibinwang.pay.enu.PayStatu;
import com.zhibinwang.pay.mapper.PaymentTransactionLogMapper;
import com.zhibinwang.pay.mapper.PaymentTransactionMapper;
import com.zhibinwang.pay.rabbitmq.producer.IntergalProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhibin.wang
 * @create 2019-11-27 10:15
 * @desc 模板模式支付异步回调接口
 **/
@Slf4j
public abstract class AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionLogMapper paymentTransactionLogMapper;

    @Autowired
    private PaymentTransactionMapper transactionMapper;

    @Autowired
    private IntergalProducer intergalProducer;

    protected abstract  Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) ;

    protected abstract String fail();

    protected abstract String success();

    protected abstract String asyService(Map<String, String> result);

    public String asyncCallBack(HttpServletRequest req, HttpServletResponse resp){

        //1.解析参数

        //1 校验参数

        Map<String, String> result = verifySignature(req, resp);
        //判断是支付宝通知还是银联通知
        String pay = "【支付宝通知】";
        String channel = result.get(PayConstant.PAY_CHANNEL);
        if (PayConstant.PAY_CHANNEL_UNION.equals(channel)){
            pay = "[银联支付通知]";
        }
        String validateResult = result.get(PayConstant.VALIDATE_RESULT);
        if (PayConstant.VALIDATE_RESULT_FAIL.equals(validateResult)){
            return fail();
        }
        //2.记录日志 //异步
        addAsyncPayLog(result);
        // 3验证订单记录 TODO
        String payId = result.get(PayConstant.PAY_ID);
        //获取订单id
        PaymentTransaction paymentTransactionInfo = getPaymentTransactionInfo(Long.valueOf(payId));
        if (paymentTransactionInfo == null){
            log.info("{}未查询到支付订单信息，订单id:{}",pay,payId);
            return fail();
        }
        if (PayStatu.PAY_SUC.getValue() == paymentTransactionInfo.getPaymentStatus() || PayStatu.PAY_COMPLETE.getValue() == paymentTransactionInfo.getPaymentStatus()){
            log.info("{}订单已经成功处理，重复通知，订单id:{}",pay,payId);
            return success();
        }
        //获取jine TODO
        long money = Long.valueOf(result.get(PayConstant.PAY_MONEY));
        //4 验证支付金额
        if (money != paymentTransactionInfo.getPayAmount()){
            log.info("{}支付金额不一致，支付订单金额:{},通知金额:{},订单id:{}",pay,payId,money,paymentTransactionInfo.getPayAmount());
            return fail();
        }
        //5.接收处理
      return  asyService(result);

        //6.mq通知前台业务

    }




    private PaymentTransaction getPaymentTransactionInfo(Long id) {
        PaymentTransactionExample example = new PaymentTransactionExample();
        PaymentTransactionExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<PaymentTransaction> paymentTransactions = transactionMapper.selectByExample(example);
        PaymentTransaction paymentTransaction = paymentTransactions.get(0);
        return paymentTransaction;
    }

    protected int updatePaymentTransactionSuc(Long id,String thirdPartyId){
        PaymentTransactionExample example = new PaymentTransactionExample();
        PaymentTransactionExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setUpdatedTime(new Date());
        paymentTransaction.setPartyPayId(thirdPartyId);
        paymentTransaction.setPaymentStatus(PayStatu.PAY_SUC.getValue());
     return    transactionMapper.updateByExampleSelective(paymentTransaction,example);


    }



    @Async
    protected void addIntegral(PaymentTransaction paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getId()+"");
        jsonObject.put("userId", paymentTransaction.getUserId());
        jsonObject.put("integral", 100);
       // jsonObject.put("paymentChannel", paymentTransaction.getPaymentChannel());
        intergalProducer.send(jsonObject);
    }


    protected  PaymentTransaction getPaymentTransactionByid(long id){
       return  transactionMapper.selectByPrimaryKey(id);
    }


    @Async
    void addAsyncPayLog(Map<String, String> result){

    }


}
