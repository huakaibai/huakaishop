package com.zhibinwang.spike.entity;

import lombok.Data;

import java.util.Date;

@Data
public class HuakaiSeckill {
    private Long seckillId;

    private String name;

    private Integer inventory;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Long version;

}