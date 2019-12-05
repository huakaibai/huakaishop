package com.zhibinwang.pay.callback;

import com.zhibinwang.core.spring.SpringContextUtil;

/**
 * @author zhibin.wang
 * @create 2019-11-27
 * @desc 支付异步回调模板工厂类
 **/
public class PayCallbackFactory {

    public  static  AbstractPayCallbackTemplate getPayCallBackTemplateImpl(String beanName){
       return (AbstractPayCallbackTemplate) SpringContextUtil.getBean(beanName);

    }

}
