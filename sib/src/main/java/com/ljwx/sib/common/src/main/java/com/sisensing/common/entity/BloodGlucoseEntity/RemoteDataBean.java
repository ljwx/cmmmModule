package com.sisensing.common.entity.BloodGlucoseEntity;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.entity.BloodGlucoseEntity
 * Author: f.deng
 * CreateDate: 2021/8/31 9:24
 * Description:
 */
public class RemoteDataBean {

    private int latestIndex;
    private String algorithmVersion;

    public int getIndex() {
        return latestIndex;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }
}
