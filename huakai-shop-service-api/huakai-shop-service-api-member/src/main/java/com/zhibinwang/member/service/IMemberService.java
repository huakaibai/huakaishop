package com.zhibinwang.member.service;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.core.validate.Phone;
import com.zhibinwang.member.input.dto.UserLoginInpDTO;
import com.zhibinwang.member.output.dto.UserOutputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.NotEmpty;

/**
 * @author 花开
 * @create 2019-10-23 21:33
 * @desc
 **/
@Api( tags = "会员服务接口")
@RequestMapping("/member")
@Validated
public interface IMemberService {

    /**
     * 用户注册接口
     *
     * @param phone
     * @return
     */
    @PostMapping("/existMobile")
    @ApiOperation(value = "校验用户是否注册")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "用户号码")
          })
    BaseResponse<UserOutputDTO> existMobile(   @RequestParam("phone") @NotEmpty(message = "手机号不能为空") @Phone(message = "手机号不合法") String phone);

    /**
     *
     * @param token
     * @return
     */
    @PostMapping("/getInfoByToken")
    @ApiOperation(value = "获取用户信息根据token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", required = true, value = "Token信息")
    })
    BaseResponse<UserOutputDTO> getInfoByToken(@RequestParam("token") @NotEmpty( message = "token不能为空") String token);

    /**
     *
     * @param userLoginInpDTO
     * @return
     */
    @PostMapping("/loginSSO")
    @ApiOperation(value = "用户登陆通过sso")
    BaseResponse<UserOutputDTO> loginSSO(@RequestBody UserLoginInpDTO userLoginInpDTO);


}