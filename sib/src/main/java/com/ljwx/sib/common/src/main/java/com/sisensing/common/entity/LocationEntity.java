package com.sisensing.common.entity;

/**
 * @author y.xie
 * @date 2021/7/12 14:21
 * @desc
 */
public class LocationEntity {
    private String provinceName;//省
    private String city;//市
    private String detail;//详细地址
    private double latitude;//纬度
    private double longitude;//经度

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
