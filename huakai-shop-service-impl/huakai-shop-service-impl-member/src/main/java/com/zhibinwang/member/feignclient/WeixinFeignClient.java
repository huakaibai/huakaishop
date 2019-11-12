package com.zhibinwang.member.feignclient;

import com.zhibinwang.api.weixin.WeiXinInteface;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 花开
 * @create 2019-10-23 21:38
 * @desc
 **/
@FeignClient("app-huakai-weixin")
public interface WeixinFeignClient extends WeiXinInteface {
}
