package com.sisensing.common.entity.BloodGlucoseEntity;

/**
 * @author y.xie
 * @date 2021/5/25 11:28
 * @desc 糖尿病类型实体类
 */
public class TnbTypeEntity {
    private int position;
    private String type;
    private boolean isSelect;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
