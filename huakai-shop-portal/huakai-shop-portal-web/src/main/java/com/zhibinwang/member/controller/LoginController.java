package com.zhibinwang.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.base.BaseWebController;
import com.zhibinwang.core.token.LoginType;
import com.zhibinwang.member.feign.MemberLoginServiceFiengClient;
import com.zhibinwang.member.input.dto.UserLoginInpDTO;
import com.zhibinwang.member.vo.LoginVo;
import com.zhibinwang.web.Constants;
import com.zhibinwang.web.bean.HuakaiBeanUtils;
import com.zhibinwang.web.code.RandomValidateCodeUtil;
import com.zhibinwang.web.cookie.CookieUtils;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController extends BaseWebController {
	private static final String MEMBER_LOGIN_PAGE = "member/login";

	/**
	 * 重定向到首页
	 */
	@Autowired
	private static final String REDIRECT_INDEX = "redirect:/";


	@Autowired
	private MemberLoginServiceFiengClient memberLoginServiceFiengClient;




	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String getLogin() {
		return MEMBER_LOGIN_PAGE;
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@PostMapping("/login")
	public String postLogin(@Validated  LoginVo loginVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, Model model) {

		//判断参数校验错误
		if (bindingResult.hasErrors()){
			String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
			setErrorMsg(model,defaultMessage);
			return MEMBER_LOGIN_PAGE;
		}
		// 图形验证码校验

		Boolean aBoolean = RandomValidateCodeUtil.checkVerify(loginVo.getGraphicCode(), request.getSession());
		if (!aBoolean){
			setErrorMsg(model,"验证码不正确");
			return MEMBER_LOGIN_PAGE;
		}
		UserLoginInpDTO userLoginInpDTO = HuakaiBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
		//设置登录类型
		userLoginInpDTO.setLoginType(LoginType.PC.toString());
		//设置设备信息
		userLoginInpDTO.setDeviceInfor(webBrowserInfo(request));

		//调用登录接口
		BaseResponse<JSONObject> login = memberLoginServiceFiengClient.login(userLoginInpDTO);
		if (!isSuccess(login)){
			setErrorMsg(model,login.getMsg());
			return MEMBER_LOGIN_PAGE;
		}
		//设置cookie
		CookieUtils.setCookie(request,response, Constants.HUAKAI_LOGIN_PC_COOKIE_KEY,login.getData().getString("token"));

		//返回首页
		return REDIRECT_INDEX;
	}

	@GetMapping("/exit")
	public String exit(HttpServletRequest request,HttpServletResponse response){
		String token = CookieUtils.getCookieValue(request, Constants.HUAKAI_LOGIN_PC_COOKIE_KEY);
		if (StringUtils.isNotBlank(token)){
			//调用member服务注销登录
			memberLoginServiceFiengClient.exit(token);
			//删除cookie
			CookieUtils.deleteCookie(request,response,Constants.HUAKAI_LOGIN_PC_COOKIE_KEY);
		}
		return REDIRECT_INDEX;
	}

}
