package com.sisensing.common.entity;

/**
 * @author y.xie
 * @date 2021/12/31 15:46
 * @desc 红点消息
 */
public class RedDotBean {
    //type=1 报告生成 type=2 血糖分享
    private int type;
    private boolean isHide;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }
}
