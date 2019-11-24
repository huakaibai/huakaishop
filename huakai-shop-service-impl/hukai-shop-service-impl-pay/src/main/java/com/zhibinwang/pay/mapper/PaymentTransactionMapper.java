package com.zhibinwang.pay.mapper;

import com.zhibinwang.pay.entity.PaymentTransaction;
import com.zhibinwang.pay.entity.PaymentTransactionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PaymentTransactionMapper {
    int countByExample(PaymentTransactionExample example);

    int deleteByExample(PaymentTransactionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PaymentTransaction record);

    int insertSelective(PaymentTransaction record);

    List<PaymentTransaction> selectByExample(PaymentTransactionExample example);

    PaymentTransaction selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PaymentTransaction record, @Param("example") PaymentTransactionExample example);

    int updateByExample(@Param("record") PaymentTransaction record, @Param("example") PaymentTransactionExample example);

    int updateByPrimaryKeySelective(PaymentTransaction record);

    int updateByPrimaryKey(PaymentTransaction record);
}