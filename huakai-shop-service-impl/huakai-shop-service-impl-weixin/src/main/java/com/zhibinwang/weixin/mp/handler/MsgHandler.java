package com.zhibinwang.weixin.mp.handler;

import cn.hutool.core.util.RandomUtil;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.utils.RedisUtil;
import com.zhibinwang.core.utils.RegexUtils;
import com.zhibinwang.weixin.mp.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



import java.util.Map;
import java.util.Random;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
@SuppressWarnings("static-access")
public class MsgHandler extends AbstractHandler {

	@Value("${huakaibai.weixin.registration.code.message}")
	private String regCodemsg;

	@Value(("${huakaibai.weixin.default.registration.code.message}"))
	private String defaultMsg;
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {



		if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
			// TODO 可以选择将消息保存到本地
		}

		// 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
		try {
			if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
					&& weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
				return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
						.toUser(wxMessage.getFromUser()).build();
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
       String fromContent =  wxMessage.getContent();
		// 先判断为不为空是不是手机号
        if (StringUtils.isEmpty(fromContent)){
            return new TextBuilder().build(defaultMsg, wxMessage, weixinService);
        }
        if (!RegexUtils.checkMobile(fromContent)){
			return new TextBuilder().build(defaultMsg, wxMessage, weixinService);

		}
        //2 生成随机数
		int randomCode = RandomUtil.randomInt(1000, 9999);
		redisUtil.setString(Constants.WEIXINCODE_KEY+fromContent,randomCode+"",Constants.WEIXINCODE_TIMEOUT);

		String toContent = String.format(regCodemsg, randomCode+"");

		return new TextBuilder().build(toContent, wxMessage, weixinService);

	}

	// 获取注册码
	private int registCode() {
		int registCode = (int) (Math.random() * 9000 + 1000);
		return registCode;
	}

}
