package com.sisensing.common.entity.Device;

/**
 * @author y.xie
 * @date 2021/3/17 9:25
 * @desc 新增设备请求javaBean类
 */
public class DeviceAddRequestBean2 {

    private AddressDTO address;
    private String blueToothNum;
    private String enableTime;
    private String fv;
    private String lastTime;
    private String macAddress;
    private String name;
    private Integer status;
    private String userId;
    private String algorithmVersion;
    private String deviceModelStr;
    private String bluetoothProtocolVersion;

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public String getBlueToothNum() {
        return blueToothNum;
    }

    public void setBlueToothNum(String blueToothNum) {
        this.blueToothNum = blueToothNum;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getFv() {
        return fv;
    }

    public void setFv(String fv) {
        this.fv = fv;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public String getDeviceModelStr() {
        return deviceModelStr;
    }

    public void setDeviceModelStr(String deviceModelStr) {
        this.deviceModelStr = deviceModelStr;
    }

    public String getBluetoothProtocolVersion() {
        return bluetoothProtocolVersion;
    }

    public void setBluetoothProtocolVersion(String bluetoothProtocolVersion) {
        this.bluetoothProtocolVersion = bluetoothProtocolVersion;
    }

    public static class AddressDTO {
        private String cityCode;
        private String cityName;
        private String detailAddress;
        private String districtCode;
        private String districtName;
        private String provinceCode;
        private String provinceName;
        private String latitude;
        private String longitude;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getDistrictCode() {
            return districtCode;
        }

        public void setDistrictCode(String districtCode) {
            this.districtCode = districtCode;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
