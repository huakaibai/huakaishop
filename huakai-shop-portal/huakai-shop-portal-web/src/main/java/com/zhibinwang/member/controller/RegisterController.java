package com.zhibinwang.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.base.BaseWebController;
import com.zhibinwang.member.feign.MemberRegisterServiceFeignClient;
import com.zhibinwang.member.input.dto.UserInputDTO;
import com.zhibinwang.member.vo.RegisterVo;
import com.zhibinwang.web.bean.HuakaiBeanUtils;
import com.zhibinwang.web.code.RandomValidateCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class RegisterController extends BaseWebController {
	private static final String MEMBER_REGISTER_PAGE = "member/register";

	private static final String MEMBER_LOGIN_PAGE = "member/login";

	@Autowired
	private MemberRegisterServiceFeignClient memberRegisterServiceFeignClient;

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@GetMapping("/register.html")
	public String getRegister() {
		return MEMBER_REGISTER_PAGE;
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@PostMapping("/register.html")
	public String postRegister(@Validated  RegisterVo registerVo, BindingResult bindingResult,  Model model) {

		//参数校验有错误
		if (bindingResult.hasErrors()){
			String errorMessage = bindingResult.getFieldError().getDefaultMessage();
			setErrorMsg(model,errorMessage);
			return MEMBER_REGISTER_PAGE;
		}
		UserInputDTO userInputDTO = HuakaiBeanUtils.voToDto(registerVo, UserInputDTO.class);
		//调用feign客户端

		try {
			BaseResponse<JSONObject> register = memberRegisterServiceFeignClient.register(userInputDTO, registerVo.getRegistCode());
			if (!isSuccess(register)) {
				setErrorMsg(model, register.getMsg());
				return MEMBER_REGISTER_PAGE;
			}
		} catch (Exception e) {
			log.error("注册异常",e);
			setErrorMsg(model, "系统异常");
			return MEMBER_REGISTER_PAGE;
		}
		return MEMBER_LOGIN_PAGE;

	}

}
