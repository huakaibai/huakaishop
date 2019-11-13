package com.zhibinwang.portal.controller;

import com.netflix.discovery.converters.Auto;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.member.feign.MembserService;
import com.zhibinwang.member.output.dto.UserOutputDTO;
import com.zhibinwang.web.Constants;
import com.zhibinwang.web.cookie.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

	private static final String PO_INDEX="index";


	@Autowired
	private MembserService memberService;

	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String index(HttpServletRequest request, Model model) {
		String cookieValue = CookieUtils.getCookieValue(request, Constants.HUAKAI_LOGIN_PC_COOKIE_KEY);
		if (StringUtils.isNotBlank(cookieValue)){
			//cookie值不为空，则根据token查询用户信息
			BaseResponse<UserOutputDTO> user = memberService.getInfoByToken(cookieValue);


		}
		return PO_INDEX;
	}

	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping("/index.html")
	public String indexHtml(HttpServletRequest request, Model model) {
		return index(request,model);
	}
}
