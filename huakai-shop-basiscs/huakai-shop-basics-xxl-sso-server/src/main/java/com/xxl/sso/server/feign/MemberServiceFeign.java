package com.xxl.sso.server.feign;

import com.zhibinwang.member.service.IMemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 花开
 * @create 2019-11-17 16:13
 * @desc feign客户端
 **/
@FeignClient("app-huakai-member")
public interface MemberServiceFeign extends IMemberService {
}
