package com.wjb.o2o.enums;

public enum ShopStateEnum {
    CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),
    PASS(2,"通过认证"),INNER_ERROR(-1001,"内部系统错误"),
    NULL_SHOPID(-1002,"ShopId为空"),NULL_SHOP(-1003,"店铺为空");
        private int state;
        private String stateInfo;

        private ShopStateEnum(int state,String stataInfo) {
            this.state = state;
            this.stateInfo = stataInfo;
        }

    /**
     * 依据传入的state返回响应的enum值
     */
    public static ShopStateEnum stateOf(int state) {
        for (ShopStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
