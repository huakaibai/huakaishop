package com.zhibinwang.weixin.service;

import com.google.gson.JsonObject;
import com.zhibinwang.api.weixin.VerRegCodeInterface;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.utils.RedisUtil;
import com.zhibinwang.core.utils.RegexUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 花开
 * @create 2019-10-30 19:51
 * @descc
 **/
@RestController
public class VerRegCodeInterfaceImpl extends BaseApiService implements VerRegCodeInterface {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public BaseResponse<JsonObject> verRegCode(String phone, String weixinCode) {

        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(weixinCode)){

            return setResultError("手机号码或者注册码为空");
        }

        if (!RegexUtils.checkMobile(phone)){
            return setResultError("手机号码不合法");
        }

        String regCode = redisUtil.getString(Constants.WEIXINCODE_KEY + phone);
        if(StringUtils.isEmpty(regCode)){
            return setResultError("注册码无效!");
        }
        if (!weixinCode.equals(regCode)){
            return setResultError("注册码不正确!");
        }
        redisUtil.delKey(Constants.WEIXINCODE_KEY + phone);

        return setResultSuccess("校验成功");
    }
}
