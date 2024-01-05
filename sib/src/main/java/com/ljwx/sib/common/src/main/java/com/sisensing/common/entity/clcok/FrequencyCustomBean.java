package com.sisensing.common.entity.clcok;

/**
 * @author y.xie
 * @date 2021/5/19 16:56
 * @desc
 */
public class FrequencyCustomBean {
    //定义一个角标,从1开始
    private int index;
    private String text;
    private boolean isSelect;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
