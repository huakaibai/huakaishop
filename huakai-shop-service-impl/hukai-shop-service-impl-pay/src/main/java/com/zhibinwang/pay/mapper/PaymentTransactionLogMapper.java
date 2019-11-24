package com.zhibinwang.pay.mapper;

import com.zhibinwang.pay.entity.PaymentTransactionLog;
import com.zhibinwang.pay.entity.PaymentTransactionLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PaymentTransactionLogMapper {
    int countByExample(PaymentTransactionLogExample example);

    int deleteByExample(PaymentTransactionLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PaymentTransactionLog record);

    int insertSelective(PaymentTransactionLog record);

    List<PaymentTransactionLog> selectByExampleWithBLOBs(PaymentTransactionLogExample example);

    List<PaymentTransactionLog> selectByExample(PaymentTransactionLogExample example);

    PaymentTransactionLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PaymentTransactionLog record, @Param("example") PaymentTransactionLogExample example);

    int updateByExampleWithBLOBs(@Param("record") PaymentTransactionLog record, @Param("example") PaymentTransactionLogExample example);

    int updateByExample(@Param("record") PaymentTransactionLog record, @Param("example") PaymentTransactionLogExample example);

    int updateByPrimaryKeySelective(PaymentTransactionLog record);

    int updateByPrimaryKeyWithBLOBs(PaymentTransactionLog record);

    int updateByPrimaryKey(PaymentTransactionLog record);
}