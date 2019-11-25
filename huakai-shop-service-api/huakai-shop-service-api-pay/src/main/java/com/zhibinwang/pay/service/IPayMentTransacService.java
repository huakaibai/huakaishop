package com.zhibinwang.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PayCratePayTokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

@Api(tags = "支付接口")
@RequestMapping("/pay")
@Validated
public interface IPayMentTransacService {

	/**
	 * 创建支付令牌
	 * 
	 * @return
	 */
	@PostMapping("/cratePayToken")
	@ApiOperation(value = "预支付接口")
	 BaseResponse<JSONObject> cratePayToken(@RequestBody PayCratePayTokenDto payCratePayTokenDto);

	@PostMapping("/payHtml")
	@ApiOperation(value = "提交支付表单参数")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "Token信息"),
			@ApiImplicitParam(paramType = "query", name = "channelId", dataType = "String", required = true, value = "支付渠道id")

	})
	BaseResponse<JSONObject>  payHtml(@RequestParam("token") @NotBlank(message = "token 不能为空") String token, @RequestParam("channelId") @NotBlank(message = "渠道id不能为空") String channelId);

}
