package com.zhibinwang.pay.serveice;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PayCratePayTokenDto;
import com.zhibinwang.pay.service.IPayMentTransacService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhibin.wang
 * @create 2019-11-22 14:26
 * @desc 支付实现接口
 **/
@RestController
public class PayTransactionServiceImpl implements IPayMentTransacService {

    public BaseResponse<JSONObject> cratePayToken(@RequestBody PayCratePayTokenDto payCratePayTokenDto) {

        return  null;
    }
}
