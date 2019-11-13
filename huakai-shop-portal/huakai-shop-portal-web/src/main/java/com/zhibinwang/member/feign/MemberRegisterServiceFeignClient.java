package com.zhibinwang.member.feign;

import com.zhibinwang.member.service.IMemberRegisterService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhibin.wang
 * @create 2019-11-13 10:14
 * @desc
 **/
@FeignClient("app-huakai-member")
public interface MemberRegisterServiceFeignClient extends IMemberRegisterService {
}
