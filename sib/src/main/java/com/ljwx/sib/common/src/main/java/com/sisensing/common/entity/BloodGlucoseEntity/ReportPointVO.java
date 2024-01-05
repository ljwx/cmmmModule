package com.sisensing.common.entity.BloodGlucoseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zj.zhong
 * @Description:
 * @Date: Created in 2021/3/31 20:20
 */

/**
 * 报告血糖数据点
 */
public class ReportPointVO implements Serializable {

    private static final long serialVersionUID = 5048642769159370382L;
    //时间戳
    private Long t;
    //血糖值
    private BigDecimal v;

    public ReportPointVO(Long t, BigDecimal v) {
        this.t = t;
        this.v = v;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public BigDecimal getV() {
        return v;
    }

    public void setV(BigDecimal v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "ReportPointDTO{" +
                "t=" + t +
                ", v=" + v +
                '}';
    }
}
