package com.zhibinwang.member.service;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.member.input.dto.UserInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;

/**
 * @author 花开
 */
@Api(tags = "会员注册接口")
@RequestMapping("/member")
@Validated
public interface IMemberRegisterService {
	/**
	 * 用户注册接口
	 * 
	 * @param userInputDTO
	 * @return
	 */
	@PostMapping("/register")
	@ApiOperation(value = "会员用户注册信息接口")
//	@ApiImplicitParams({
		//	@ApiImplicitParam(paramType = "requestBody", name = "userInputDTO", dataType = "Json", required = true, value = "用户信息"),
		//	@ApiImplicitParam(paramType = "query", name = "registCode", dataType = "String", required = true, value = "注册码")})

	BaseResponse<JSONObject> register(@RequestBody @Validated UserInputDTO userInputDTO,
									  @RequestParam("registCode") @NotEmpty(message = "注册码不能为空") @Length(min = 4,max = 4,message = "注册码无效") String registCode);

}
