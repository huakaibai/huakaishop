package com.zhibinwang.member.serviceimpl;

import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.bin.HuakaiBeanUtils;
import com.zhibinwang.member.entity.UserDo;
import com.zhibinwang.member.feignclient.WeixinFeignClient;
import com.zhibinwang.member.mapper.UserMapper;
import com.zhibinwang.member.output.dto.UserOutputDTO;
import com.zhibinwang.member.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * @author 花开
 * @create 2019-10-23 21:44
 * @desc
 **/
@RestController

public class MemberServiceImpl extends BaseApiService<UserOutputDTO> implements IMemberService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseResponse<UserOutputDTO> existMobile( String phone) {
        UserDo userDo = userMapper.existMobile(phone);
        if (userDo == null){
            return setResultError(Constants.HTTP_RES_CODE_EXISTMOBILE_203,"用户不存在");
        }
        UserOutputDTO o = HuakaiBeanUtils.doToDto(userDo, UserOutputDTO.class);
        return setResultSuccess(o);
    }
}
