package com.sisensing.common.entity;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.entity
 * Author: f.deng
 * CreateDate: 2021/4/25 19:55
 * Description:
 * <p>
 * 一、高血糖事件次数与时长：
 * 高血糖开始时间：当检测到有连续≥15min血糖值高于10.0mmol/L（一级上限）时，则定义第一个高于10.0mmol/L的时间为开始时间。
 * 高血糖结束时间：当持续高血糖情况下，有连续≥15min血糖值低于10.0mmol/L时，则定义为第一个不高于10.0mmol/L的点为结束时间。
 * 在这个区间内，即为一次高血糖事件，开始时间和结束时间（包含开始与结束时间）的时间即为持续时间。
 * （如按点数计算，则根据实际点数对应的时间作为持续时间）
 * <p>
 * 低血糖事件次数与时长：
 * 低血糖开始时间：当检测到有连续≥15min血糖值低于3.9mmol/L（一级下限）时，则定义第一个低于3.9mmol/L的时间为开始时间。
 * 低血糖结束时间：当持续低血糖情况下，有连续≥15min血糖值高于3.9mmol/L时，则定义为第一个不低于3.9mmol/L的时间为结束时间。
 */
public class DailyEventEntity {

    private int highEventCount;//高于目标值的事件数 >13.9
    private long highEventTime;//高于某个目标值时间
    private long rangeHighEventTime;//高于目标范围时间  10-13.9

    private int lowEventCount;//低于目标值的事件数<3.0
    private long lowEventTime;//低于某个目标值时间
    private long rangeLowEventTime;//低于目标范围时间3.0-3.9

    private long rangeTargetTime;//目标范围时间  3.9-10
    private long totalTime;//总时间

    public int getHighEventCount() {
        return highEventCount;
    }

    public void setHighEventCount(int highEventCount) {
        this.highEventCount = highEventCount;
    }

    public long getHighEventTime() {
        return highEventTime;
    }

    public void setHighEventTime(long highEventTime) {
        this.highEventTime = highEventTime;
    }

    public long getRangeHighEventTime() {
        return rangeHighEventTime;
    }

    public void setRangeHighEventTime(long rangeHighEventTime) {
        this.rangeHighEventTime = rangeHighEventTime;
    }

    public int getLowEventCount() {
        return lowEventCount;
    }

    public void setLowEventCount(int lowEventCount) {
        this.lowEventCount = lowEventCount;
    }

    public long getLowEventTime() {
        return lowEventTime;
    }

    public void setLowEventTime(long lowEventTime) {
        this.lowEventTime = lowEventTime;
    }

    public long getRangeLowEventTime() {
        return rangeLowEventTime;
    }

    public void setRangeLowEventTime(long rangeLowEventTime) {
        this.rangeLowEventTime = rangeLowEventTime;
    }

    public long getRangeTargetTime() {
        return rangeTargetTime;
    }

    public void setRangeTargetTime(long rangeTargetTime) {
        this.rangeTargetTime = rangeTargetTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
