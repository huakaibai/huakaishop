package com.zhibinwang.member.serviceimpl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.bin.HuakaiBeanUtils;
import com.zhibinwang.member.entity.UserDo;
import com.zhibinwang.member.feignclient.IVerRegCodeFeingClient;
import com.zhibinwang.member.input.dto.UserInputDTO;
import com.zhibinwang.member.mapper.UserMapper;
import com.zhibinwang.member.service.IMemberRegisterService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * @author 花开
 * @create 2019-10-31 20:36
 * @desc 注册实现类
 **/
@RestController

public class MemberRegisterServiceImpl extends BaseApiService<JSONObject>  implements IMemberRegisterService {

    @Autowired
    private IVerRegCodeFeingClient verRegCodeFeingClient;

    @Autowired
    private UserMapper userMapper;


    @Override
    public BaseResponse<JSONObject> register(@RequestBody UserInputDTO userInputDTO, String registCode) {
        //1 参数校验已经在框架做了
        //2 判断手机号和验证吗是否正确
        BaseResponse baseResponse = verRegCodeFeingClient.verRegCode(userInputDTO.getMobile(), registCode);
        if (!baseResponse.getCode().equals( Constants.HTTP_RES_CODE_200)){
            return  setResultError(Constants.HTTP_RES_CODE_201,baseResponse.getMsg());
        }
    /*    //3 判断手机号是否已经注册
    // 手机号注册过获取不到验证吗
        UserDo userDo = userMapper.existMobile(userInputDTO.getMobile());
        if (userDo != null){
            return setResultError(Constants.HTTP_RES_CODE_EXISTMOBILE_203,"手机号已经被注册");
        }*/

        String s = DigestUtil.md5Hex(userInputDTO.getPassword());
        UserDo userDo = HuakaiBeanUtils.dtoToDo(userInputDTO, UserDo.class);
        userDo.setPassword(s);
        int register = userMapper.register(userDo);
        return register>0?setResultSuccess("注册成功"):setResultError("注册失败");
    }
}
