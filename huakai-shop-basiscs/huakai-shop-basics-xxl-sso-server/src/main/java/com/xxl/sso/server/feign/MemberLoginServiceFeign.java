package com.xxl.sso.server.feign;

import com.zhibinwang.member.service.IMemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhibin.wang
 * @create 2019-11-18 15:39
 * @desc
 **/
@FeignClient("app-huakai-member")
public interface MemberLoginServiceFeign extends IMemberLoginService {
}
