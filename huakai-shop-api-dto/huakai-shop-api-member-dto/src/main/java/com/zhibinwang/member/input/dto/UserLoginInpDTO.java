package com.zhibinwang.member.input.dto;

import com.zhibinwang.core.validate.Phone;
import com.zhibinwang.core.validate.VaLoginType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "用户登陆参数")
public class UserLoginInpDTO {
	/**
	 * 手机号码
	 */
	@ApiModelProperty(value = "手机号码")
	@NotBlank(message="手机号不能为空")
	@Phone(message = "手机号不合法")
	private String mobile;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	@NotBlank(message="密码不能为空")
	private String password;

	/**
	 * 登陆类型 PC、Android 、IOS
	 */
	@ApiModelProperty(value = "登陆类型")
	@NotBlank(message="登陆类型不能为空")
	@VaLoginType
	private String loginType;
	/**
	 * 设备信息
	 */
	@ApiModelProperty(value = "设备信息")
	@NotBlank(message="设备信息不能为空")

	private String deviceInfor;

}
