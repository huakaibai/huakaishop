package com.zhibinwang.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.PaymentChannelDTO;
import com.zhibinwang.pay.feign.PayMentTransacInfoFeign;
import com.zhibinwang.pay.feign.PayMentTransacServiceFeign;
import com.zhibinwang.pay.feign.PaymentChannelFeign;
import com.zhibinwang.pay.service.IPayMentTransacService;
import com.zhibinwang.web.controller.BaseWebController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author 花开
 */
@Controller
public class PayController extends BaseWebController {
	@Autowired
	private PayMentTransacInfoFeign payMentTransacInfoFeign;
	@Autowired
	private PaymentChannelFeign paymentChannelFeign;

	@Autowired
	private PayMentTransacServiceFeign payMentTransacServiceFeign;

	@GetMapping("/payl")
	public String pay(String payToken, Model model) {
		// 1.验证payToken参数
		if (StringUtils.isEmpty(payToken)) {
			setErrorMsg(model, "支付令牌不能为空!");
			return ERROR_500_FTL;
		}
		// 2.使用payToken查询支付信息
		BaseResponse<PayMentTransacInfoDTO> payTransactionInfoByToken = payMentTransacInfoFeign.getPayTransactionInfoByToken(payToken);
		if (!isSuccess(payTransactionInfoByToken)) {
			setErrorMsg(model, payTransactionInfoByToken.getMsg());
			return ERROR_500_FTL;
		}
		// 3.查询支付信息
		PayMentTransacInfoDTO data = payTransactionInfoByToken.getData();
		model.addAttribute("data", data);
		// 4.查询渠道信息
		List<PaymentChannelDTO> paymentChannelDTOS = paymentChannelFeign.selectAllChannel();
		model.addAttribute("payToken",payToken);
		model.addAttribute("paymentChanneList", paymentChannelDTOS);
		return "index";
	}


	@RequestMapping("/payHtml")
	public void payHtml(String channelId, String payToken, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		BaseResponse<JSONObject> payHtmlData = payMentTransacServiceFeign.payHtml(payToken,channelId);
		if (isSuccess(payHtmlData)) {
			JSONObject data = payHtmlData.getData();
			String payHtml = data.getString("payHtml");
			response.getWriter().print(payHtml);
		}

	}

}
