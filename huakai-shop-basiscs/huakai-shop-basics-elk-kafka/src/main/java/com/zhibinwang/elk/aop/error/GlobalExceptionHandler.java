package com.zhibinwang.elk.aop.error;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.elk.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 * 
 * @description: 全局捕获异常
 * @author: 97后互联网架构师-余胜军
 * @contact: QQ644064779、微信yushengjun644 www.mayikt.com
 * @date: 2019年1月3日 下午3:03:17
 * @version V1.0
 * @Copyright 该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下，
 *            私自分享视频和源码属于违法行为。
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@Autowired
	private KafkaSender<JSONObject> kafkaSender;

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public JSONObject exceptionHandler(Exception e) {
		log.info("###全局捕获异常###,error:{}", e);

		// 1.封装异常日志信息
		JSONObject errorJson = new JSONObject();
		JSONObject logJson = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		logJson.put("request_time", df.format(new Date()));
		logJson.put("error_info", e);
		errorJson.put("request_error", logJson);
		kafkaSender.send(errorJson);
		// 2. 返回错误信息
		JSONObject result = new JSONObject();
		result.put("code", 500);
		result.put("msg", "系统错误");

		return result;
	}
}
