package com.sisensing.common.entity;

/**
 * @ClassName NewlyAddedBsDataEntity
 * @Description 新增血糖返回的数据类
 * @Author xieyang
 * @Date 2023/4/24 10:01
 */
public class NewlyAddedBsDataEntity {
    private String algorithmVersion;
    private int index;

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
