package com.sisensing.common.entity.BloodGlucoseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * All rights Reserved, Designed By www.sibionics.com
 *
 * @Version V1.0
 * @Title: GlucoseWave
 * @Package: com.sisensing.cgm.patient.domain.vo
 * @Description:
 * @Author: Created by wwh
 * @Date: 2021/1/15 16:41
 * @Company: 深圳硅基仿生科技有限公司
 * @Copyright: 2021 www.sibionics.com.
 */
public class GlucoseWave implements Serializable {
    private static final long serialVersionUID = 2510123327051876377L;

    /**
     * 波峰、波谷标识  1.波峰  2.波谷
     */
    private int flag;
    /**
     * 血糖值
     */
    private BigDecimal value;

    public GlucoseWave(int flag, BigDecimal value) {
        this.flag = flag;
        this.value = value;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
