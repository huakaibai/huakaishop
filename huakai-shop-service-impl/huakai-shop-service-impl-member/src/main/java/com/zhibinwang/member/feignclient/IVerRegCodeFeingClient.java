package com.zhibinwang.member.feignclient;

import com.zhibinwang.api.weixin.VerRegCodeInterface;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 花开
 * @create 2019-10-31 20:40
 * @desc 注册feign客户端
 **/
@FeignClient("app-huakai-weixin")
public interface IVerRegCodeFeingClient extends VerRegCodeInterface {
}
