package com.zhibinwang.spike.mapper;

import com.zhibinwang.spike.entity.HukaiOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrderMapper {

    @Insert("INSERT INTO `hukai_order` VALUES (#{seckillId},#{userPhone},#{state}, now());")
    int insertOrder(HukaiOrder orderEntity);

    @Select("SELECT seckill_id AS seckillid,user_phone as userPhone , state as state FROM hukai_order WHERE USER_PHONE=#{phone}  and seckill_id=#{seckillId}  AND STATE='1';")
    HukaiOrder findByOrder(@Param("phone") String phone, @Param("seckillId") Long seckillId);
}
