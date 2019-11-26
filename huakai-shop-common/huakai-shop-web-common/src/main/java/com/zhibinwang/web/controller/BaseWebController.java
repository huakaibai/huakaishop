package com.zhibinwang.web.controller;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public class BaseWebController {

	protected static final String ERROR_500_FTL = "500";

	public Boolean isSuccess(BaseResponse<?> baseResp) {
		if (baseResp == null) {
			return false;
		}
		if (!baseResp.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			return false;
		}
		return true;
	}

	public void setErrorMsg(Model model, String errorMsg) {
		model.addAttribute("error", errorMsg);
	}

	public String webBrowserInfo(HttpServletRequest request) {
		// 获取浏览器信息
		UserAgent  ua = UserAgentUtil.parse(request.getHeader("User-Agent"));
		String info = ua.getBrowser().toString() + "/" + ua.getEngineVersion();
		return info;
	}


}
