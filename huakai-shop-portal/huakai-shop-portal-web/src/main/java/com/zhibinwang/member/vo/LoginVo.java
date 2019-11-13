package com.zhibinwang.member.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Valid
/**
 * 2. @NotEmpty 用在集合类上面
 * 　　　　@NotBlank 用在String上面
 * 　　　　@NotNull 用在基本类型上
 */
public class LoginVo {
	/**
	 * 手机号码
	 */
	@NotBlank(message = "手机号码不能为空")
	@Size(min = 11, max = 11, message = "手机号码长度不正确")
	@Pattern(regexp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$", message = "手机号格式错误")
	private String mobile;
	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空!")
	private String password;

/*
 图形验证码*/


	@NotBlank(message = "图形验证码不能为空!")
	private String graphicCode;


}
