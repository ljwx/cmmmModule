package com.sisensing.common.ble;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.ble
 * Author: f.deng
 * CreateDate: 2021/7/22 11:41
 * Description:
 */
public interface CgmConnectListener {


    void connected();

    void connecting();

    void disConnected(boolean showToast);

    void areaValid(boolean valid, String areaCode);

}
