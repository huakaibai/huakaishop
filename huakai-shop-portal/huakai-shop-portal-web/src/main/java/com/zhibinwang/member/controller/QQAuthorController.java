package com.zhibinwang.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.web.controller.BaseWebController;
import com.zhibinwang.core.token.LoginType;
import com.zhibinwang.member.feign.MemberLoginServiceFiengClient;
import com.zhibinwang.web.WebConstants;
import com.zhibinwang.web.cookie.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhibin.wang
 * @create 2019-11-14 9:58
 * @desc 用户登录
 **/
@Controller
@Slf4j
public class QQAuthorController extends BaseWebController {

    /**
     * 重定向到首页
     */
    private static final String REDIRECT_INDEX = "redirect:/";

    private static  final String ERROR_500_FTL = "500.ftl";

    private static final String MB_QQ_QQLOGIN = "member/qqlogin";

    @Autowired
    private MemberLoginServiceFiengClient memberLoginServiceFiengClient;

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
            BaseResponse<JSONObject> baseResponse = memberLoginServiceFiengClient.findByqqOpenId(openID, LoginType.PC.toString(), webBrowserInfo(request));
            if (!isSuccess(baseResponse)){
                return ERROR_500_FTL;
            }
            //用户未绑定跳转绑定页面
            if (WebConstants.HTTP_RES_CODE_EXISTMOBILE_203.equals(baseResponse.getCode())){
                // 使用openid获取用户信息
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if (userInfoBean == null) {
                    return ERROR_500_FTL;
                }
                String avatarURL100 = userInfoBean.getAvatar().getAvatarURL100();
                // 返回用户头像页面展示
                request.setAttribute("avatarURL100", avatarURL100);
                request.getSession().setAttribute(WebConstants.LOGIN_QQ_OPENID, openID);


                return MB_QQ_QQLOGIN;
            }
            //已经绑定并且登录成功
            JSONObject data = baseResponse.getData();
            if (data == null){
                return ERROR_500_FTL;
            }
            //判断token是否为空
            String token = data.getString("token");
            if (StringUtils.isBlank(token)) {
                return ERROR_500_FTL;
            }

            //token正常设置cookie，跳转首页
            CookieUtils.setCookie(request,response, WebConstants.HUAKAI_LOGIN_PC_COOKIE_KEY,token);
            return REDIRECT_INDEX;

        } catch (QQConnectException e) {
            e.printStackTrace();
            return ERROR_500_FTL;
        }


    }



}
