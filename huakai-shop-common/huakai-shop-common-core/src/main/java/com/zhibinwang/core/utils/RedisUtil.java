package com.zhibinwang.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author huakai
 */
@Component
public class RedisUtil {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	/**
	 * 存放string类型
	 *
	 * @param key
	 *            key
	 * @param data
	 *            数据
	 * @param timeout
	 *            超时间
	 */
	public void setString(String key, String data, Long timeout) {
		try {

			stringRedisTemplate.opsForValue().set(key, data);
			if (timeout != null) {
				stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 存放list
	 * @param key
	 * @param list
	 * @param timeOut
	 */
	public void setList(String key, List list,Long timeOut){
		stringRedisTemplate.opsForList().leftPushAll(key,list);
		if (timeOut != null){
			stringRedisTemplate.expire(key,timeOut,TimeUnit.SECONDS);
		}
	}

	/**
	 * 获取和删除list的值
	 * @param key
	 * @return
	 */
	public String getAndDelList(String key){
		return   stringRedisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 开启Redis 事务
	 *
	 * @param
	 */
	public void begin() {
		// 开启Redis 事务权限
		stringRedisTemplate.setEnableTransactionSupport(true);
		// 开启事务
		stringRedisTemplate.multi();

	}
	/**
	 * 提交事务
	 *
	 * @param
	 */
	public void exec() {
		// 成功提交事务
		stringRedisTemplate.exec();
	}

	/**
	 * 回滚Redis 事务
	 */
	public void discard() {
		stringRedisTemplate.discard();
	}
	/**
	 * 存放string类型
	 *
	 * @param key
	 *            key
	 * @param data
	 *            数据
	 */
	public void setString(String key, String data) {
		setString(key, data, null);
	}

	/**
	 * 根据key查询string类型
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		String value = stringRedisTemplate.opsForValue().get(key);
		return value;
	}

	/**
	 * 根据对应的key删除key
	 *
	 * @param key
	 */
	public Boolean delKey(String key) {
		return stringRedisTemplate.delete(key);

	}

	public Boolean setIfAbsent(String key, String value, Long timeOut){
		Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
		if (aBoolean){
			stringRedisTemplate.expire(key,timeOut,TimeUnit.SECONDS);
		}
		return aBoolean;
	}
}
