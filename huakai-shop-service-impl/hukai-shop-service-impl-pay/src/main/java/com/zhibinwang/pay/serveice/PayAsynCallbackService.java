package com.zhibinwang.pay.serveice;

import com.zhibinwang.pay.callback.AbstractPayCallbackTemplate;
import com.zhibinwang.pay.callback.PayCallbackFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PayAsynCallbackService {

	/**
	 * 获取银联回调模版
	 */
	private static final String UNIONPAYCALLBACKT_EMPLATENAME = "unionPayCallbackTemplateImpl";

	private static  final String ALIPAYCALLBACK_TEMPLATENAME = "aliPayCallbackTemplateImpl";

	/**
	 * 银联异步回调通知
	 * 
	 * @return
	 */
	@RequestMapping("/unionPayAsynCallback")
	public String unionPayCallback(HttpServletRequest req, HttpServletResponse resp) {
		AbstractPayCallbackTemplate payCallbackTemplate = PayCallbackFactory.getPayCallBackTemplateImpl(UNIONPAYCALLBACKT_EMPLATENAME);
		return payCallbackTemplate.asyncCallBack(req, resp);
	}
	@RequestMapping("/aliPayCallback")
	public String aliPayCallback(HttpServletRequest req, HttpServletResponse resp){
		AbstractPayCallbackTemplate payCallbackTemplate = PayCallbackFactory.getPayCallBackTemplateImpl(ALIPAYCALLBACK_TEMPLATENAME);
		return payCallbackTemplate.asyncCallBack(req, resp);
	}

}