package com.zhibinwang.pay.feign;

import com.zhibinwang.pay.service.IPayMentTransacService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 花开
 * @create 2019-11-26 19:42
 * @desc fegn
 **/
@FeignClient("app-huakai-pay")
public interface PayMentTransacServiceFeign extends IPayMentTransacService {
}
