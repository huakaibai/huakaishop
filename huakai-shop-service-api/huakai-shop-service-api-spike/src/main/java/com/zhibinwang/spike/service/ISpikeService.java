package com.zhibinwang.spike.service;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.core.validate.Phone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.NotNull;

/**
 * @author zhibin.wang
 * @create 2019-12-05
 * @desc 秒杀服务
 **/
@Api(tags = "秒杀服务接口")
@Validated
public interface ISpikeService {



    @ApiOperation(value = "秒杀功能")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "seckilld", dataType = "String", required = true, value = "秒杀商品id")
    })
    @GetMapping("/spike")
    BaseResponse<JSONObject> spike(@Phone String phone, @NotNull(message = "秒杀id不能为空") Long seckilld);

    @ApiOperation(value = "生成秒杀token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "seckilld", dataType = "String", required = true, value = "秒杀商品id")
    })
    @GetMapping("/generateSeckillToken")
    BaseResponse<JSONObject> generateSeckillToken(@NotNull(message = "秒杀id不能为空")String seckilId);


    @ApiOperation(value = "查询秒杀结果")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "seckilld", dataType = "String", required = true, value = "秒杀商品id")
    })
    @GetMapping("/querySeckillResult")
    BaseResponse<JSONObject> querySeckillResult(String phone,String secKillId);



}
