package com.zhibinwang.pay.mapper;

import com.zhibinwang.pay.entity.PaymentChannel;
import com.zhibinwang.pay.entity.PaymentChannelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PaymentChannelMapper {
    int countByExample(PaymentChannelExample example);

    int deleteByExample(PaymentChannelExample example);

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("channelId") String channelId);

    int insert(PaymentChannel record);

    int insertSelective(PaymentChannel record);

    List<PaymentChannel> selectByExampleWithBLOBs(PaymentChannelExample example);

    List<PaymentChannel> selectByExample(PaymentChannelExample example);

    PaymentChannel selectByPrimaryKey(@Param("id") Integer id, @Param("channelId") String channelId);

    int updateByExampleSelective(@Param("record") PaymentChannel record, @Param("example") PaymentChannelExample example);

    int updateByExampleWithBLOBs(@Param("record") PaymentChannel record, @Param("example") PaymentChannelExample example);

    int updateByExample(@Param("record") PaymentChannel record, @Param("example") PaymentChannelExample example);

    int updateByPrimaryKeySelective(PaymentChannel record);

    int updateByPrimaryKeyWithBLOBs(PaymentChannel record);

    int updateByPrimaryKey(PaymentChannel record);
}