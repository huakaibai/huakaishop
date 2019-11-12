package com.zhibinwang.member.output.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "用户中注册")
public class UserOutputDTO {

	@ApiModelProperty(value = "用户Id")

	private Integer userId;

	@ApiModelProperty(value = "手机号码")

	private String mobile;
	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱")
	private String email;

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
