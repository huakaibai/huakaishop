package com.zhibinwang.api.weixin;

import com.zhibinwang.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 花开
 * @create 2019-10-30
 * @desc 校验注册码
 **/
@Api(tags = "微信校验注册码")
@RequestMapping("/weixin")
public interface VerRegCodeInterface {

    @ApiOperation(value = "校验注册码")
    @GetMapping("verRegCode")
    @ApiImplicitParams({
            // @ApiImplicitParam(paramType="header",name="name",dataType="String",required=true,value="用户的姓名",defaultValue="zhaojigang"),
            @ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "用户手机号码"),
            @ApiImplicitParam(paramType = "query", name = "weixinCode", dataType = "String", required = true, value = "微信注册码") })
    BaseResponse verRegCode(@RequestParam("phone") String phone,@RequestParam("weixinCode")   String weixinCode);
}
