package com.zhibinwang.pay.serveice;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.token.GenerateToken;
import com.zhibinwang.pay.PayCratePayTokenDto;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentChannelExample;
import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.enu.PayStatu;
import com.zhibinwang.pay.mapper.PaymentChannelMapper;
import com.zhibinwang.pay.mapper.PaymentTransactionMapper;
import com.zhibinwang.pay.service.IPayMentTransacInfoService;
import com.zhibinwang.pay.service.IPayMentTransacService;
import com.zhibinwang.pay.strategy.PayStrategy;
import com.zhibinwang.pay.strategy.PayStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


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
    
    @Autowired
    private PaymentChannelMapper paymentChannelMapper;
    
    
    @Autowired
    private IPayMentTransacInfoService payMentTransacInfoService;



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
        paymentTransaction.setUserId(payCratePayTokenDto.getUserId());
        paymentTransaction.setPayName(payCratePayTokenDto.getPayName());
        paymentTransactionMapper.insert(paymentTransaction);

        //根据id生成token

        String token = generateToken.createToken(Constants.PAY_TOKEN_PRIX, paymentTransaction.getId() + "",Constants.PAY_TOKEN_TIME);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);


        return setResultSuccess(jsonObject) ;
    }

    @Override
    public BaseResponse<JSONObject> payHtml(String token,  String channelId)  {
        
        // 获取支付订单信息
        BaseResponse<PayMentTransacInfoDTO> payTransactionInfoByToken = payMentTransacInfoService.getPayTransactionInfoByToken(token);
        if (!isSuccess(payTransactionInfoByToken)){
            return  setResultError(payTransactionInfoByToken.getMsg());
        }

        PayMentTransacInfoDTO  payMentTransacInfoDTO = payTransactionInfoByToken.getData();
        
        // 根据支付渠道id获取渠道信息
        PaymentChannelExample example = new PaymentChannelExample();
        PaymentChannelExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        // 渠道信息0可用，1不可用
        criteria.andChannelStateEqualTo(0);
        List<PaymentChannel> paymentChannels = paymentChannelMapper.selectByExample(example);
        if (paymentChannels == null || paymentChannels.size() < 1){
           return  setResultError("根据支付渠道id，没有查询到对应数据");
        }
        // 策略模式获取支付类
        PaymentChannel paymentChannel = paymentChannels.get(0);
        String className = paymentChannel.getClassName();
        PayStrategy payStrategy = PayStrategyFactory.getPayStrategy(className);
        if (payStrategy == null){
            return setResultError("获取支付策略类失败！");
        }
        // 根据具体的策略实现类封装返回的html
        String html = null;
        try {
            html = payStrategy.toPayHtml(paymentChannel, payMentTransacInfoDTO);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("payHtml",html);
        return setResultSuccess(jsonObject);
    }


}
