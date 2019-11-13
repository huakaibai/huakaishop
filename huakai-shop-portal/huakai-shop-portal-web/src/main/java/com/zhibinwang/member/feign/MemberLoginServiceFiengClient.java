package com.zhibinwang.member.feign;

import com.zhibinwang.member.service.IMemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhibin.wang
 * @create 2019-11-13 10:05
 * @desc feignClient
 **/
@FeignClient("app-huakai-member")
public interface MemberLoginServiceFiengClient extends IMemberLoginService {
}
