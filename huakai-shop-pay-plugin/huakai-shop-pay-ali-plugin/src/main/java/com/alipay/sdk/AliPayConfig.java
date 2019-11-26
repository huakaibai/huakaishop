package com.alipay.sdk;

import cn.hutool.setting.Setting;
import lombok.Data;

/**
 * @author zhibin.wang
 * @create 2019-11-26 15:20
 * @desc ali支付配置文件
 **/
@Data
public class AliPayConfig {

    private static final String FILE_NAME = "alipay_sdk.setting";

    private String signType; //签名方式

    private String applicationCertPath; //应用证书存放路劲

    private String appCertPath; //支付宝证书存放路劲

    private String alipayRootCertPath;// 支付宝根证书存放路径

    private String charset;

    private Setting setting = null;

    private AliPayConfig(){super();}

    private static AliPayConfig config = new AliPayConfig();

    public static AliPayConfig getAliPayConfig(){
        return  config;
    }

    public void loadProperties(){

        setting = new Setting(FILE_NAME);
        signType = setting.getStr("alipay.sign_type");

        applicationCertPath = setting.getStr("alipay.app_application_cert_path");

        appCertPath = setting.getStr("alipay.app_cert_path");

        alipayRootCertPath = setting.getStr("alipay.alipay_root_cert_path");

        charset = setting.getStr("alipay.charset");


    }
}
