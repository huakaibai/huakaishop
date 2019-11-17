package com.zhibinwang.member.service;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.core.validate.VaLoginType;
import com.zhibinwang.member.input.dto.UserLoginInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author 花开
 */
@Api(tags = "用户登陆服务接口")
@Validated
public interface IMemberLoginService {
	/**
	 * 用户登陆接口
	 * 
	 * @param userLoginInpDTO
	 * @return
	 */
	@PostMapping("/login")
	@ApiOperation(value = "会员用户登陆信息接口")
	BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO);

	@PostMapping("/exit")
	@ApiOperation(value = "注销信息接口")
	BaseResponse<JSONObject> exit(@RequestParam @NotBlank( message = "token不能为空") String token);

	@PostMapping("/findByOpenId")
	@ApiOperation(value = "根据qqopenId获取用户信息并自动登陆")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "openId", dataType = "String", required = true, value = "openId信息")
	})
	BaseResponse<JSONObject> findByqqOpenId(@RequestParam("openId") @NotEmpty( message = "openId不能为空") String openId,@RequestParam("loginType")  @VaLoginType() String loginType, @RequestParam("deviceInfo") String deviceInfo);

}
