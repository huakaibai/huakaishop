package com.zhibinwang.member.serviceimpl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.token.GenerateToken;
import com.zhibinwang.core.utils.MD5Util;
import com.zhibinwang.core.utils.RedisDataSoureceTransaction;
import com.zhibinwang.core.utils.RedisUtil;
import com.zhibinwang.member.entity.UserDo;
import com.zhibinwang.member.entity.UserTokenDo;
import com.zhibinwang.member.input.dto.UserLoginInpDTO;
import com.zhibinwang.member.mapper.UserMapper;
import com.zhibinwang.member.mapper.UserTokenMapper;
import com.zhibinwang.member.service.IMemberLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 花开
 * @create 2019-11-08 23:21
 * @desc 登陆接口实现类
 **/
@RestController
@Slf4j
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements IMemberLoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTokenMapper userTokenMapper;


    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private RedisDataSoureceTransaction redisDataSoureceTransaction;




    /**
     * @RequestBody 这个注解在实现类必须添加不然swagger不识别
     * @param userLoginInpDTO
     * @return
     */
    public BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO) {

        //1 对手机号先进性加密
        String password = DigestUtil.md5Hex(userLoginInpDTO.getPassword());

        // 2.根据手机号和密码查询用户
        UserDo user = userMapper.login(userLoginInpDTO.getMobile(), password);
        if (user == null){
            return setResultError("用户名或密码不正确");
        }
        //3.根据用户id查询token
        UserTokenDo userTokenDo = userTokenMapper.selectByUserIdAndLoginType(user.getUserId(), userLoginInpDTO.getLoginType());
        TransactionStatus transactionStatus = null;

        try {
            //开启事务
            transactionStatus  = redisDataSoureceTransaction.begin();
            if (userTokenDo != null){

                String redisUserId = redisUtil.getString(userTokenDo.getToken());
                if (StringUtils.isNotEmpty(redisUserId)){
                    //删除缓存
                    redisUtil.delKey(userTokenDo.getToken());
                }
                //4 更新数据状态
                int i = userTokenMapper.updateTokenAvailability(userTokenDo.getToken());
                if (!toDbResult(i)){
                    //更新失败回滚事务
                    //回退数据库和redis事务
                    redisDataSoureceTransaction.rollback(transactionStatus);
                    return setResultError("系统错误!");
                }
            }

            //5 生成新的token插入缓存
            String token = generateToken.createToken(Constants.MEMBER_TOKEN_KEYPREFIX + userLoginInpDTO.getLoginType(), user.getUserId() + "", Constants.TOKEN_MEMBER_TIME);

            //6 存入数据库中
            UserTokenDo newToken = new UserTokenDo();
            newToken.setDeviceInfor(userLoginInpDTO.getDeviceInfor());
            newToken.setLoginType(userLoginInpDTO.getLoginType());
            newToken.setToken(token);
            newToken.setUserId(user.getUserId());
            newToken.setIsAvailability(0L);
            newToken.setCreateTime(new Date());
            int i = userTokenMapper.insertUserToken(newToken);
            if (!toDbResult(i)){
                //更新失败回滚事务
                //回退数据库和redis事务
                redisDataSoureceTransaction.rollback(transactionStatus);
                return setResultError("系统错误!");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",token);
            //提交数据库和redis事务
            redisDataSoureceTransaction.commit(transactionStatus);
            return setResultSuccess(jsonObject);
        } catch (Exception e) {
            log.error("用户登陆异常",e);
            try {
                redisDataSoureceTransaction.rollback(transactionStatus);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return setResultError("系统错误!");

        }


    }
}
