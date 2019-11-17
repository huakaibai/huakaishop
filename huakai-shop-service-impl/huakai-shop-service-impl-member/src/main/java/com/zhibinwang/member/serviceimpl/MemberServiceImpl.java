package com.zhibinwang.member.serviceimpl;

import cn.hutool.crypto.digest.DigestUtil;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.bin.HuakaiBeanUtils;
import com.zhibinwang.core.token.GenerateToken;
import com.zhibinwang.member.entity.UserDo;

import com.zhibinwang.member.input.dto.UserLoginInpDTO;
import com.zhibinwang.member.mapper.UserMapper;
import com.zhibinwang.member.output.dto.UserOutputDTO;
import com.zhibinwang.member.service.IMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 花开
 * @create 2019-10-23 21:44
 * @desc
 **/
@RestController
@Slf4j
public class MemberServiceImpl extends BaseApiService<UserOutputDTO> implements IMemberService {


    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<UserOutputDTO> existMobile( String phone) {
        UserDo userDo = userMapper.existMobile(phone);
        if (userDo == null){
            return setResultError(Constants.HTTP_RES_CODE_EXISTMOBILE_203,"用户不存在");
        }
        UserOutputDTO o = HuakaiBeanUtils.doToDto(userDo, UserOutputDTO.class);
        return setResultSuccess(o);
    }

    @Override
    public BaseResponse<UserOutputDTO> getInfoByToken(String token) {

        //1 现根据token获取用户id
        String userId = generateToken.getToken(token);
        if (StringUtils.isEmpty(userId)){

            return setResultError("token已失效");
        }

        //2 根据用户id查询信息
        UserDo userDo = userMapper.findByUserId(userId);
        if ( userDo == null){
            generateToken.removeToken(token);
            return setResultError("用户不存在");
        }
        UserOutputDTO userOutputDTO = HuakaiBeanUtils.doToDto(userDo, UserOutputDTO.class);
        return setResultSuccess(userOutputDTO) ;
    }

    @Override
    public BaseResponse<UserOutputDTO> loginSSO(@RequestBody UserLoginInpDTO userLoginInpDTO) {

        //1 对手机号先进性加密
        log.info("username={},password={}",userLoginInpDTO.getMobile(),userLoginInpDTO.getPassword());
        String password = DigestUtil.md5Hex(userLoginInpDTO.getPassword());

        // 2.根据手机号和密码查询用户
        UserDo user = userMapper.login(userLoginInpDTO.getMobile(), password);
        if (user == null){

            return setResultError("用户名或密码不正确");
        }

        UserOutputDTO userOutputDTO = HuakaiBeanUtils.doToDto(user, UserOutputDTO.class);
        return setResultSuccess(userOutputDTO);
    }
}
