package com.zhibinwang.member.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhibin.wang
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserTokenDo extends BaseDo {

	/**
	 * 用户token
	 */
	private String token;
	/**
	 * 登陆类型
	 */
	private String loginType;

	/**
	 * 设备信息
	 */
	private String deviceInfor;
	/**
	 * 用户userId
	 */
	private Long userId;



}
