package com.zhibinwang.pay.controller;

import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.pay.PayMentTransacInfoDTO;
import com.zhibinwang.pay.PaymentChannelDTO;
import com.zhibinwang.pay.feign.PayMentTransacInfoFeign;
import com.zhibinwang.pay.feign.PaymentChannelFeign;
import com.zhibinwang.web.controller.BaseWebController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PayController extends BaseWebController {
	@Autowired
	private PayMentTransacInfoFeign payMentTransacInfoFeign;
	@Autowired
	private PaymentChannelFeign paymentChannelFeign;

	@RequestMapping("/pay")
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
		BaseResponse<List<PaymentChannelDTO>> listBaseResponse = paymentChannelFeign.selectAllChannel();
		if (!isSuccess(listBaseResponse)){
			setErrorMsg(model, listBaseResponse.getMsg());
			return ERROR_500_FTL;
		}
		model.addAttribute("paymentChanneList", listBaseResponse.getData());
		return "index";
	}

}
