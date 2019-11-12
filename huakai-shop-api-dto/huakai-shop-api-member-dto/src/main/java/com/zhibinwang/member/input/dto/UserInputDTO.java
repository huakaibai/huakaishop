package com.zhibinwang.member.input.dto;

import com.zhibinwang.core.validate.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel(value = "用户中注册")
public class UserInputDTO {



	@ApiModelProperty(value = "手机号码")
	@NotBlank(message="手机号不能为空")
	@Phone(message = "手机号不合法")
	private String mobile;
	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱")
	private String email;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	@NotBlank(message="密码不能为空")
	private String password;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 性别 0 男 1女
	 */
	@ApiModelProperty(value = "用户性别")
	private Character sex;
	/**
	 * 年龄
	 */
	@ApiModelProperty(value = "用户年龄")
	private Long age;

	/**
	 * 用户头像
	 */
	@ApiModelProperty(value = " 用户头像")
	private String picImg;
	/**
	 * 用户关联 QQ 开放ID
	 */
	@ApiModelProperty(value = "用户关联 QQ 开放ID")
	private String qqOpenid;
	/**
	 * 用户关联 微信 开放ID
	 */
	@ApiModelProperty(value = "用户关联 微信 开放ID")
	private String wxOpenid;
}
