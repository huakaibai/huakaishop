package com.zhibinwang.spike.entity;

import lombok.Data;

import java.util.Date;

@Data
public class HukaiOrder {
    private Long seckillId;

    private String userPhone;

    private Byte state;

    private Date createTime;

}
