package com.sisensing.common.entity.BloodGlucoseEntity;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zj.zhong
 * @Description:
 * @Date: Created in 2021/3/31 17:05
 *
 * 报告每日数据
 */

public class DailyVO implements Serializable {

    private static final long serialVersionUID = -1497623804370151983L;
    //时间戳
    private Long dateTime;
    //MG平均葡萄糖值
    private BigDecimal mg;
    //TIR目标范围内时间
    private BigDecimal tir;
    //TAR高于目标范围的时间
    private BigDecimal tar;
    //TBR低于目标范围的时间
    private BigDecimal tbr;
    //探头值上限
    private BigDecimal upper;
    //探头值下限
    private BigDecimal lower;
    //探头值数量
    private Long count;
    //LAGE最大血糖波动振幅
    private BigDecimal lage;
    //MAGE平均血糖波动幅度
    private BigDecimal mage;
    //MODD日间血糖平均绝对差
    private BigDecimal modd;
    //SD葡萄糖标准差
    private BigDecimal sd;
    //CV变异系数
    private BigDecimal cv;
    //全天低血糖事件
    private List<BgEvent> tbrEvent;
    //夜间低血糖事件
    private List<BgEvent> tbrEventNight;
    //全天高血糖事件
    private List<BgEvent> tarEvent;
    //血糖数据点
    private ArrayList<ReportPointVO> data;

    public DailyVO(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public ArrayList<ReportPointVO> getData() {
        return data;
    }

    public void setData(ArrayList<ReportPointVO> data) {
        this.data = data;
    }

    public BigDecimal getMg() {
        return mg;
    }

    public void setMg(BigDecimal mg) {
        this.mg = mg;
    }

    public BigDecimal getTir() {
        return tir;
    }

    public void setTir(BigDecimal tir) {
        this.tir = tir;
    }

    public BigDecimal getTar() {
        return tar;
    }

    public void setTar(BigDecimal tar) {
        this.tar = tar;
    }

    public BigDecimal getTbr() {
        return tbr;
    }

    public void setTbr(BigDecimal tbr) {
        this.tbr = tbr;
    }

    public BigDecimal getUpper() {
        return upper;
    }

    public void setUpper(BigDecimal upper) {
        this.upper = upper;
    }

    public BigDecimal getLower() {
        return lower;
    }

    public void setLower(BigDecimal lower) {
        this.lower = lower;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getLage() {
        return lage;
    }

    public void setLage(BigDecimal lage) {
        this.lage = lage;
    }

    public BigDecimal getMage() {
        return mage;
    }

    public void setMage(BigDecimal mage) {
        this.mage = mage;
    }

    public BigDecimal getModd() {
        return modd;
    }

    public void setModd(BigDecimal modd) {
        this.modd = modd;
    }

    public BigDecimal getSd() {
        return sd;
    }

    public void setSd(BigDecimal sd) {
        this.sd = sd;
    }

    public BigDecimal getCv() {
        return cv;
    }

    public void setCv(BigDecimal cv) {
        this.cv = cv;
    }

    public List<BgEvent> getTbrEvent() {
        return tbrEvent;
    }

    public void setTbrEvent(List<BgEvent> tbrEvent) {
        this.tbrEvent = tbrEvent;
    }

    public List<BgEvent> getTbrEventNight() {
        return tbrEventNight;
    }

    public void setTbrEventNight(List<BgEvent> tbrEventNight) {
        this.tbrEventNight = tbrEventNight;
    }

    public List<BgEvent> getTarEvent() {
        return tarEvent;
    }

    public void setTarEvent(List<BgEvent> tarEvent) {
        this.tarEvent = tarEvent;
    }

    @Override
    public String toString() {
        return "DailyVO{" +
                "dateTime=" + dateTime +
                ", mg=" + mg +
                ", tir=" + tir +
                ", tar=" + tar +
                ", tbr=" + tbr +
                ", upper=" + upper +
                ", lower=" + lower +
                ", count=" + count +
                ", lage=" + lage +
                ", mage=" + mage +
                ", modd=" + modd +
                ", sd=" + sd +
                ", cv=" + cv +
                ", tbrEvent=" + tbrEvent +
                ", tbrEventNight=" + tbrEventNight +
                ", tarEvent=" + tarEvent +
                ", data=" + data +
                '}';
    }
}
