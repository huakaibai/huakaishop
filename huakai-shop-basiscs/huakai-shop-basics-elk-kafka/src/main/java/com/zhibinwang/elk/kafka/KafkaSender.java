package com.zhibinwang.elk.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 生产者
 * 
 * 
 * @description:
 * @author: 97后互联网架构师-余胜军
 * @contact: QQ644064779、微信yushengjun644 www.mayikt.com
 * @date: 2019年1月3日 下午3:03:17
 * @version V1.0
 * @Copyright 该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下，
 *            私自分享视频和源码属于违法行为。
 */
@Component
@Slf4j
public class KafkaSender<T> {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * kafka 发送消息
	 *
	 * @param obj
	 *            消息对象
	 */
	public void send(T obj) {
		String jsonObj = JSON.toJSONString(obj);
		log.info("------------ message = {}", jsonObj);

		// 发送消息
		ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("my_log", jsonObj);
		future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
			@Override
			public void onFailure(Throwable throwable) {
				log.info("Produce: The message failed to be sent:" + throwable.getMessage());
			}

			@Override
			public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
				// TODO 业务处理
				log.info("Produce: The message was sent successfully:");
				log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
			}
		});
	}
}