package com.sisensing.common.entity.behavior;

/**
 * @author y.xie
 * @date 2021/4/14 16:30
 * @desc
 */
public class BehaviorEntity {
    private int bgColor;
    private int titleTextColor;
    private int icon;
    private String title;

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
