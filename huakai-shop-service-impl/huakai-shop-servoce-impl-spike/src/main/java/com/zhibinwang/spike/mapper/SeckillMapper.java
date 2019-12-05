package com.zhibinwang.spike.mapper;

import com.zhibinwang.spike.entity.HuakaiSeckill;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author zhibin.wang
 * @create 2019-12-05 10:27
 * @desc
 **/
public interface SeckillMapper {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("SELECT  seckill_id as seckillId, name, inventory, start_time as startTime , end_time as endTime, create_time as createTime, version FROM huakai_seckill WHERE seckill_id = #{id}")
    HuakaiSeckill findById(Long id);

    /**
     * 使用乐观锁，更新库存数量
     * @param id
     * @param version
     * @return
     */
    @Update("update huakai_seckill set inventory=inventory-1 ,version=version-1 WHERE seckill_id = #{id} and version = #{version} and inventory > 0  ")
    int updateSeckill(Long id,Long version);
}
