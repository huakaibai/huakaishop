package com.zhibinwang.core.token;

import com.zhibinwang.core.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author 花开
 */
@Component
public class GenerateToken {
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 生成令牌
	 * 
	 * @param prefix
	 *            令牌key前缀
	 * @param redisValue
	 *            redis存放的值
	 * @return 返回token
	 */
	public String createToken(String keyPrefix, String redisValue) {
		return createToken(keyPrefix, redisValue, null);
	}

	/**
	 * 生成令牌
	 * 
	 * @param prefix
	 *            令牌key前缀
	 * @param redisValue
	 *            redis存放的值
	 * @param time
	 *            有效期
	 * @return 返回token
	 */
	public String createToken(String keyPrefix, String redisValue, Long time) {
		if (StringUtils.isEmpty(redisValue)) {
			new Exception("redisValue Not nul");
		}
		String token = keyPrefix + UUID.randomUUID().toString().replace("-", "");
		redisUtil.setString(token, redisValue, time);
		return token;
	}

	/**
	 * 生成商品库存令牌桶
	 * @param key 前缀+商品id
	 * @param num 商品库存数量
	 * @param time 过期时间
	 */
	public void creteTokenBucket(String key,Integer num,Long time) throws Exception {
		if (StringUtils.isEmpty(key)){
			throw  new RuntimeException("key值为空");
		}
		List<String> list = new ArrayList<>();
		for (int i = 0;i < num;i++){
			list.add( UUID.randomUUID().toString().replace("-", ""));
		}

		redisUtil.setList(key,list,time);

	}

	/**
	 * 获取令牌桶
	 * @param key 关键字
	 * @return
	 */
	public String getTokenBucket(String key){

		return redisUtil.getAndDelList(key);

	}

	/**
	 * 根据token获取redis中的value值
	 * 
	 * @param token
	 * @return
	 */
	public String getToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		String value = redisUtil.getString(token);
		return value;
	}

	/**
	 * 移除token
	 * 
	 * @param token
	 * @return
	 */
	public Boolean removeToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		return redisUtil.delKey(token);

	}

}
