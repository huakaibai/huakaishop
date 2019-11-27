package com.zhibinwang.pay.enu;

import lombok.Data;

/**
 * @author 花开
 * @create 2019-11-22 21:23
 * @desc 支付状态枚举 // 0待支付1已经支付2支付超时3支付失败'
 **/

public enum PayStatu {
    UN_PAY(0),PAY_SUC(1),PAY_TIMEOUT(2),PAY_FAILED(3),PAY_COMPLETE(4);

    private int value;
    PayStatu(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
