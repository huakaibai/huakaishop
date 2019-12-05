package com.zhibinwang.member.feign;

import com.zhibinwang.member.service.IMemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhibin.wang
 * @create 2019-11-13
 * @desc
 **/
@FeignClient("app-huakai-member")
public interface MembserService extends IMemberService {
}
