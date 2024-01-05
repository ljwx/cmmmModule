package com.sisensing.common.entity;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/4/15 13:43
 * @desc 智能提醒实体类（assets中IntelligentReminder文件内容）
 */
public class IntelligentReminderEntity {

    private List<String> max;
    private List<String> two_tar;
    private List<String> one_tar;
    private List<String> dm_gt_six_point_one;
    private List<String> ad_four_point_four_and_dm_six_point_one;
    private List<String> ad_lt_four_point_four;
    private List<String> one_tbr;
    private List<String> two_tbr;
    private List<String> min;

    public List<String> getMax() {
        return max;
    }

    public void setMax(List<String> max) {
        this.max = max;
    }

    public List<String> getTwo_tar() {
        return two_tar;
    }

    public void setTwo_tar(List<String> two_tar) {
        this.two_tar = two_tar;
    }

    public List<String> getOne_tar() {
        return one_tar;
    }

    public void setOne_tar(List<String> one_tar) {
        this.one_tar = one_tar;
    }

    public List<String> getDm_gt_six_point_one() {
        return dm_gt_six_point_one;
    }

    public void setDm_gt_six_point_one(List<String> dm_gt_six_point_one) {
        this.dm_gt_six_point_one = dm_gt_six_point_one;
    }

    public List<String> getAd_four_point_four_and_dm_six_point_one() {
        return ad_four_point_four_and_dm_six_point_one;
    }

    public void setAd_four_point_four_and_dm_six_point_one(List<String> ad_four_point_four_and_dm_six_point_one) {
        this.ad_four_point_four_and_dm_six_point_one = ad_four_point_four_and_dm_six_point_one;
    }

    public List<String> getAd_lt_four_point_four() {
        return ad_lt_four_point_four;
    }

    public void setAd_lt_four_point_four(List<String> ad_lt_four_point_four) {
        this.ad_lt_four_point_four = ad_lt_four_point_four;
    }

    public List<String> getOne_tbr() {
        return one_tbr;
    }

    public void setOne_tbr(List<String> one_tbr) {
        this.one_tbr = one_tbr;
    }

    public List<String> getTwo_tbr() {
        return two_tbr;
    }

    public void setTwo_tbr(List<String> two_tbr) {
        this.two_tbr = two_tbr;
    }

    public List<String> getMin() {
        return min;
    }

    public void setMin(List<String> min) {
        this.min = min;
    }
}
