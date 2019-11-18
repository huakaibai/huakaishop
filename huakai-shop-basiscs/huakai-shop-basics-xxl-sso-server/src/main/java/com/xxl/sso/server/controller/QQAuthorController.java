package com.xxl.sso.server.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.login.SsoWebLoginHelper;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.store.SsoSessionIdHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.server.feign.MemberLoginServiceFeign;
import com.xxl.sso.server.feign.MemberServiceFeign;
import com.zhibinwang.base.BaseResponse;

import com.zhibinwang.core.bin.HuakaiBeanUtils;
import com.zhibinwang.member.input.dto.UserLoginInpDTO;
import com.zhibinwang.member.output.dto.UserOutputDTO;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author zhibin.wang
 * @create 2019-11-14 9:58
 * @desc 用户登录
 **/
@Controller
@Slf4j
public class QQAuthorController  {

    /**
     * 重定向到首页
     */
    private static final String REDIRECT_INDEX = "redirect:/";

    private static  final String ERROR_500_FTL = "500.ftl";

    private static final String MB_QQ_QQLOGIN = "member/qqlogin";

    private static  final String LOGIN = "login";

    @Value("${huakai.shop.indexUrl}")
    private String indexUrl;

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    @GetMapping("/qqAuth")
    public String qqAuth(HttpServletRequest request){
        String authorizeURL = null;
        try {
            authorizeURL = new Oauth().getAuthorizeURL(request);
        } catch (QQConnectException e) {
            log.error("生成授权页码异常：",e);
        }
        return "redirect:"+authorizeURL;
    }

    @GetMapping("/qqLoginBack")
    public String qqLoginBack(String code, HttpServletRequest request, HttpServletResponse response){
        //获取accesstoken 根据code
        try {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            if (accessTokenObj == null){
                return ERROR_500_FTL;
            }
            String accessToken = accessTokenObj.getAccessToken();
            if (StringUtils.isBlank(accessToken)){
                return ERROR_500_FTL;
            }

            //根据accessToken 获取openId
            OpenID openIDObj =  new OpenID(accessToken);
            String openID = openIDObj.getUserOpenID();
            if (StringUtils.isBlank(openID)){
                return ERROR_500_FTL;
            }

            //根据openid调用member接口是否登录
            //BaseResponse<JSONObject> baseResponse = memberLoginServiceFiengClient.findByqqOpenId(openID, LoginType.PC.toString(), webBrowserInfo(request));
            BaseResponse<UserOutputDTO> baseResponse = memberServiceFeign.findUserByQOpenId(openID);
            if (baseResponse.getCode().equals(new Integer(203))){
                //用户未绑定跳转绑定页面
                // 使用openid获取用户信息
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if (userInfoBean == null) {
                    return ERROR_500_FTL;
                }
                String avatarURL100 = userInfoBean.getAvatar().getAvatarURL100();
                // 返回用户头像页面展示
                request.setAttribute("avatarURL100", avatarURL100);
                request.getSession().setAttribute("loginQqOpenId", openID);


                return MB_QQ_QQLOGIN;
            }

        
            if (baseResponse.getCode().equals(new Integer(200))){
                final UserOutputDTO user = baseResponse.getData();
                //如果等于200 说明已经绑定过乐，直接登录即可
                XxlSsoUser xxlUser = new XxlSsoUser();
                xxlUser.setUserid(String.valueOf(user.getUserId()));
                xxlUser.setUsername(user.getMobile());
                xxlUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
                xxlUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
                xxlUser.setExpireFreshTime(System.currentTimeMillis());


                // 2、make session id
                String sessionId = SsoSessionIdHelper.makeSessionId(xxlUser);

                // 3、login, store storeKey + cookie sessionId
                SsoWebLoginHelper.login(response, sessionId, xxlUser, false);

                // 4、return, redirect sessionId
                String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
                if (redirectUrl!=null && redirectUrl.trim().length()>0) {
                    String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
                    return "redirect:" + redirectUrlFinal;
                } else {
                    return "redirect:"+indexUrl+ "?" + Conf.SSO_SESSIONID + "=" + sessionId;
                }
            }
            return ERROR_500_FTL;

        } catch (QQConnectException e) {
            e.printStackTrace();
            return ERROR_500_FTL;
        }


    }

  //  @CrossOrigin
    @PostMapping("/qqJointLogin")
    public String qqJointLogin(HttpServletRequest request, HttpServletResponse response, @Validated LoginVo loginVo, Model model){
        UserLoginInpDTO userLoginInpDTO = new UserLoginInpDTO();
        BeanUtil.copyProperties(loginVo,userLoginInpDTO);
        BaseResponse<JSONObject> baseResponse = memberLoginServiceFeign.bindUserByQopenId(userLoginInpDTO);
        if (!baseResponse.getCode().equals(new Integer(200))){
           model.addAttribute("error",baseResponse.getMsg());
            return MB_QQ_QQLOGIN;
        }
        return LOGIN;


    }
}
