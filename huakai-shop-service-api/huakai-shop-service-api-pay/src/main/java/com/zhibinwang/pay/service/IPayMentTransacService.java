package com.zhibinwang.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PayCratePayTokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "支付接口")
@RequestMapping("/pay")
public interface IPayMentTransacService {

	/**
	 * 创建支付令牌
	 * 
	 * @return
	 */
	@PostMapping("/cratePayToken")
	@ApiOperation(value = "预支付接口")
	 BaseResponse<JSONObject> cratePayToken(@RequestBody @Validated PayCratePayTokenDto payCratePayTokenDto);
}
