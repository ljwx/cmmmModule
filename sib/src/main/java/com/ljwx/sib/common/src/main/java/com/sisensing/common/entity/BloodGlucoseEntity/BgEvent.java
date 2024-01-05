package com.sisensing.common.entity.BloodGlucoseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zj.zhong
 * @Description:
 * @Date: Created in 2021/4/20 14:50
 *
 * 高/低血糖事件
 */
public class BgEvent implements Serializable {

    private static final long serialVersionUID = -984717175371587082L;
    //开始事件时间戳
    private Long datetime;
    //血糖极高/低值
    private BigDecimal peak;
    //持续分钟数
    private BigDecimal duration;

    public BgEvent(Long datetime, BigDecimal peak, BigDecimal duration) {
        this.datetime = datetime;
        this.peak = peak;
        this.duration = duration;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public BigDecimal getPeak() {
        return peak;
    }

    public void setPeak(BigDecimal peak) {
        this.peak = peak;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "BgEvent{" +
                "datetime=" + datetime +
                ", peak=" + peak +
                ", duration=" + duration +
                '}';
    }
}
