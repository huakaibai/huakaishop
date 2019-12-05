package com.zhibinwang.weixin.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 花开
 * @create 2019-10-31
 * @desc
 **/
@FeignClient("app-huakai-member")
public interface IMemberServiceFeignClient {
}
