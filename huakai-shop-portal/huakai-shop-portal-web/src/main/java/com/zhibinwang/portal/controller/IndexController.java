package com.zhibinwang.portal.controller;

import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.store.SsoSessionIdHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.core.util.CookieUtil;
import com.zhibinwang.member.feign.MembserService;
import com.zhibinwang.web.WebConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public String index(HttpServletRequest request, Model model, HttpServletResponse response) {
/*		String cookieValue = CookieUtils.getCookieValue(request, WebConstants.HUAKAI_LOGIN_PC_COOKIE_KEY);
		if (StringUtils.isNotBlank(cookieValue)){
			//cookie值不为空，则根据token查询用户信息
			BaseResponse<UserOutputDTO> user = memberService.getInfoByToken(cookieValue);
			UserOutputDTO userData = user.getData();
			if (userData != null){
				String mobile = userData.getMobile();
				// 对手机号码实现脱敏
				String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
				model.addAttribute("desensMobile", desensMobile);
			}

		}*/
/*		String sessionId = request.getParameter(WebConstants.SSO_SESSIONID);
		if (StringUtils.isNotBlank(sessionId)){
			String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
			XxlSsoUser xxlSsoUser = SsoLoginStore.get(storeKey);
			if (xxlSsoUser != null){
				String mobile = xxlSsoUser.getUsername();
				String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
				model.addAttribute("desensMobile", desensMobile);
				CookieUtil.set(response, Conf.SSO_SESSIONID, sessionId, false);
			}

		}*/
		String sessionId = request.getParameter(WebConstants.SSO_SESSIONID);
		if (StringUtils.isBlank(sessionId)){
			sessionId = CookieUtil.getValue(request,Conf.SSO_SESSIONID);
		}else {
			CookieUtil.set(response,Conf.SSO_SESSIONID,sessionId,false);
		}
		if (StringUtils.isNotBlank(sessionId)){
			String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
			XxlSsoUser xxlSsoUser = SsoLoginStore.get(storeKey);
			if (xxlSsoUser != null){
				String mobile = xxlSsoUser.getUsername();
				String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
				model.addAttribute("desensMobile", desensMobile);
			}else {
				CookieUtil.remove(request,response, Conf.SSO_SESSIONID);
			}
		}

		return PO_INDEX;
	}

	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String indexHtml(HttpServletRequest request, Model model,HttpServletResponse response) {
		return index(request,model,response);
	}
}
