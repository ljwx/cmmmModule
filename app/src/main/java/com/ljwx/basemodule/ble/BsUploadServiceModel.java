package com.ljwx.basemodule.ble;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sisensing.common.base.Model;
import com.sisensing.common.base.ResponseListener;
import com.sisensing.common.base.RetrofitUtils;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.BloodGlucoseEntity.BsDateRequestBean;
import com.sisensing.common.entity.BloodGlucoseEntity.RemoteDataBean;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.net.BsMonitoringApiService;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.http.BaseResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class BsUploadServiceModel extends Model {

    //上一次接口请求时间
    private long lastRequestTime;

    /**
     * 获取服务器返回的index
     */
    public void getCurrentIndex(String bleName, String deviceId, String localAlgorithmVersion,ResponseListener listener) {
        long currentTime = System.currentTimeMillis();

        LogUtils.e("开始调用获取index接口" + (currentTime - lastRequestTime < 60 * 1000));
        // TODO: 2021/8/20 防止同一时间多次调用接口
        if (currentTime - lastRequestTime < 60 * 1000) return;
        lastRequestTime = currentTime;
        Observable<BaseResponse<RemoteDataBean, Object>> observable = RetrofitUtils.getInstance().getRequest(BsMonitoringApiService.class)
                .getCurrentIndex(deviceId);
        requestFromServer(observable, false, new ResponseListener<RemoteDataBean, Object>() {
            @Override
            public void onSuccess(RemoteDataBean data, String msg) {
                int validIndex;
                String algorithmVersion = "";
                if (data == null) {
                    validIndex = 0;
                } else {
                    validIndex = data.getIndex();
                    algorithmVersion = data.getAlgorithmVersion();
                }

                if (ObjectUtils.isNotEmpty(algorithmVersion) && !algorithmVersion.equals(localAlgorithmVersion)) {

                    return;
                }
                LogUtils.e("获取到远程index：" + validIndex);
                RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getMoreThanIndexBloodGlucose(UserInfoUtils.getUserId(), bleName, validIndex), bloodGlucoseEntities -> {
                    LogUtils.e("调用上传血糖接口：" + bloodGlucoseEntities.size() + "---------------------" + UserInfoUtils.isUploadBsDataToService);
                    if (ObjectUtils.isEmpty(bloodGlucoseEntities)) {
                        UserInfoUtils.isUploadBsDataToService = false;
                    } else {
                        upBsData(bloodGlucoseEntities,listener);
                    }
                });
            }

            @Override
            public void onFail(int code, String message, Object errorData) {
                LogUtils.e("获取远程index接口onFail：" + message);
            }

            @Override
            public void onError(String message) {
                LogUtils.e("获取远程index接口onError：" + message);
            }
        });
    }

    /**
     * 上传每5分钟的有效血糖数据到服务器
     */
    private void upBsData(List<BloodGlucoseEntity> entitys,ResponseListener listener) {
        BsDateRequestBean bsDateRequestBean = new BsDateRequestBean();
        List<BsDateRequestBean.GlucosesDTO> glucosesDTOList = new ArrayList<>();

        for (int i = 0; i < entitys.size(); i++) {
            BloodGlucoseEntity entity = entitys.get(i);
            BsDateRequestBean.GlucosesDTO glucosesDTO = new BsDateRequestBean.GlucosesDTO();
            glucosesDTO.setV(entity.getGlucoseValue());
            glucosesDTO.setI(entity.getIndex());
            glucosesDTO.setT(entity.getProcessedTimeMill());
            glucosesDTO.setCv(entity.getCurrentValue());
            glucosesDTO.setCw(entity.getCurrentWarning());
            glucosesDTO.setS(entity.getGlucoseTrend());
            glucosesDTO.setTv(entity.getTemperatureValue());
            glucosesDTO.setTw(entity.getTemperatureWarning());
            glucosesDTO.setAst(entity.getAlarmStatus());
            glucosesDTO.setBl(entity.getElectric());
            glucosesDTO.setGw(entity.getGlucoseWarning());
            glucosesDTO.setSv(entity.getStateValue());
            glucosesDTOList.add(glucosesDTO);
        }
        bsDateRequestBean.setGlucoses(glucosesDTOList);
        bsDateRequestBean.setUserId(UserInfoUtils.getUserId());

        RequestBody requestBody = getRequestBody(bsDateRequestBean);
        Observable<BaseResponse<Boolean, Object>> observable = RetrofitUtils.getInstance().getRequest(BsMonitoringApiService.class)
                .upBsDataOld(DeviceManager.getInstance().getDeviceEntity().getDeviceId(), requestBody);
        requestFromServer(observable, false, listener);
    }
}
