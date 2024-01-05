package com.ljwx.basemodule.ble;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sisensing.common.R;
import com.sisensing.common.base.Model;
import com.sisensing.common.base.ResponseListener;
import com.sisensing.common.base.RetrofitUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.BloodGlucoseEntity.BsDateRequestBean;
import com.sisensing.common.entity.BloodGlucoseEntity.RemoteDataBean;
import com.sisensing.common.entity.Device.DeviceAddRequestBean;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.entity.Device.DeviceModifyRequestBean;
import com.sisensing.common.entity.Device.DeviceRepository;
import com.sisensing.common.entity.LocationEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEnum;
import com.sisensing.common.entity.actionRecord.ActionRecordRepository;
import com.sisensing.common.entity.actionRecord.BatchActionData;
import com.sisensing.common.entity.login.UserInfoBean;
import com.sisensing.common.net.BsMonitoringApiService;
import com.sisensing.common.net.DeviceApiService;
import com.sisensing.common.share.LogUploadModel;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.ConfigUtils;
import com.sisensing.common.utils.Log;
import com.sisensing.http.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.ble
 * @Author: f.deng
 * @CreateDate: 2021/7/21 17:30
 * @Description:蓝牙服务数据请求
 */
public class BluetoothServiceModel extends Model {


    //创建单线程化线程池
    private ExecutorService mSingleThreadExecutor;
    //上一次接口请求时间
    private long lastRequestTime;

    public BluetoothServiceModel() {
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        deleteMoreThanNinetyDayDeviceData();
    }


    private class UpdateDeviceStatusTask implements Runnable {

        private final int mStatus;
        private final long enableTime;

        public UpdateDeviceStatusTask(int status, long time) {
            mStatus = status;
            enableTime = time;
        }

        @Override
        public void run() {
           // modifyDevice(mStatus,enableTime);
        }
    }

    private void deleteMoreThanNinetyDayDeviceData() {
        //获取所有设备
        AppDatabase.getInstance().getDeviceDao().getAll().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(deviceEntities -> {
                    if (ObjectUtils.isEmpty(deviceEntities)) return;

                    for (DeviceEntity deviceEntity : deviceEntities) {
                        //第一笔血糖的时间
                        long firstBsMill = deviceEntity.getFirstBsMill();
                        //设备数据超过90天
                        if (firstBsMill != 0 && (System.currentTimeMillis() - firstBsMill > 90 * 24 * 60 * 60 * 1000l)) {
                            //删除设备
                            AppDatabase.getInstance().getDeviceDao().delete(deviceEntity);
                            //删除血糖数据
                            AppDatabase.getInstance().getBloodEntityDao().deleteDeviceAllBsDataByDeviceName(deviceEntity.getDeviceName());
                            //清除当前设备保留在本地的sp配置
                            SPUtils.getInstance(deviceEntity.getDeviceName()).clear();
                        }
                    }
                });
    }


    /**
     * 获取服务器返回的index
     */
    public void getCurrentIndex(String bleName, String deviceId, String algorithmVersion) {
        long currentTime = System.currentTimeMillis();

        BleLog.dGlucose("执行血糖上传逻辑,先判断远程index是否小于本地数据库,小于则执行上传");
        LogUtils.i("开始调用获取index接口" + (currentTime - lastRequestTime < 60 * 1000));
        // TODO: 2021/8/20 防止同一时间多次调用接口
        if (currentTime - lastRequestTime < 60 * 1000) {
            BleLog.dGlucose("上传血糖请求间隔小于一分钟,return掉");
            return;
        }
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

                if (ObjectUtils.isNotEmpty(algorithmVersion) && !algorithmVersion.equals(algorithmVersion)) {

                    ToastUtils.showLong(R.string.new_version_is_available_please_upgrade);
                    return;
                }
                LogUtils.i("获取到远程index：" + validIndex);
                BleLog.dGlucose("从服务端获取最后一笔血糖数据index:"+validIndex);
                RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getMoreThanIndexBloodGlucose(UserInfoUtils.getUserId(), bleName, validIndex), bloodGlucoseEntities -> {
                    LogUtils.i("调用上传血糖接口：" + bloodGlucoseEntities.size() + "---------------------" + UserInfoUtils.isUploadBsDataToService);
                    if (ObjectUtils.isEmpty(bloodGlucoseEntities)) {
                        BleLog.dGlucose("本地数据库不比远程血糖数据多,不上传血糖");
                        UserInfoUtils.isUploadBsDataToService = false;
                    } else {
                        Log.d("蓝牙:血糖数据", "要上传,没上传的数据大小:"+bloodGlucoseEntities.size());
                        BleLog.dGlucose("本地比远程多"+bloodGlucoseEntities.size()+"笔血糖,马上执行上传血糖逻辑");
                        upBsData(deviceId, bloodGlucoseEntities);
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
    private void upBsData(String deviceId, List<BloodGlucoseEntity> entitys) {
        //TODO 感觉可以优化一下,分批上传?
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
        String userId = UserInfoUtils.getUserId();
        bsDateRequestBean.setGlucoses(glucosesDTOList);
        bsDateRequestBean.setUserId(userId);

        RequestBody requestBody = getRequestBody(bsDateRequestBean);
        Observable<BaseResponse<Boolean, Object>> observable = RetrofitUtils.getInstance().getRequest(BsMonitoringApiService.class)
                .upBsDataOld(deviceId, requestBody);
        BleLog.dGlucose("数据已准备好,马上发起上传血糖请求");
        LogUtils.e("上传血糖",entitys.size());
        requestFromServer(observable, false, new ResponseListener<Boolean, Object>() {
            @Override
            public void onSuccess(Boolean data, String msg) {
                BleLog.dGlucose("血糖上传成功,上传"+glucosesDTOList.size()+"笔");
                DeviceEntity deviceEntity = DeviceRepository.getInstance().queryDevice(userId, deviceId);
                if (deviceEntity != null && deviceEntity.getUploadDeviceStatus() == 0 ) {
                    BleLog.dGlucose("设备状态需要修改(接口请求),下一步暂时省略");
                    modifyDevice(deviceEntity, userId);
                } else {
                    BleLog.dGlucose("设备状态不需要修改");
                }
            }

            @Override
            public void onFail(int code, String message, Object errorData) {
                LogUploadModel.getInstance().uploadConnectInfo("上传血糖:onFail----message="+message+",errorData="+errorData);
            }

            @Override
            public void onError(String message) {
                LogUploadModel.getInstance().uploadConnectInfo("上传血糖:onError----message="+message);
            }
        });
    }


    /**
     * 获取上传服务器失败的打卡数据
     */
    public void getClockInData(String deviceId) {
        UserInfoBean userInfoBean = UserInfoUtils.getUserInfo();
        if (userInfoBean != null) {
            String userId = userInfoBean.getUserId();
            if (ObjectUtils.isNotEmpty(userId)) {
                AppDatabase.getInstance()
                        .getActionRecordEntityDao()
                        .getActionRecordListByUs(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(entities -> {
                            if (ObjectUtils.isNotEmpty(entities)) {
                                Log.d("蓝牙:设备数据", "从数据库通过用户id查询行为数据:ActionRecordEntity");
                                for (ActionRecordEntity entity : entities) {
                                    if (entity.getWearClockIn() == 1) {
                                        //穿戴设备
                                        uploadClockInData(deviceId, entity);
                                    } else {
                                        int type = entity.getType();
                                        if (type == ActionRecordEnum.MEDICATIONS.getType() || type == ActionRecordEnum.INSULIN.getType()) {
                                            //用药和胰岛素调用批量上传接口
//                                            uploadBatchClockInData(deviceId, entity);
                                            uploadClockInData(deviceId, entity);
                                        } else {
                                            uploadClockInData(deviceId, entity);
                                        }
                                    }
                                }
                            }
                        });
            }
        }
    }


    /**
     * 上传用户打卡数据
     *
     * @param recordEntity
     */
    public void uploadClockInData(String deviceId, ActionRecordEntity recordEntity) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject actionData = new JSONObject(GsonUtils.toJson(recordEntity));
            jsonObject.put("type", recordEntity.getType());
            long endTime = recordEntity.getEndTime();
            if (endTime !=0){
                String endTimeStr = TimeUtils.millis2String(endTime,Constant.NORMAL_TIME_FORMAT_);
                //时间戳只精确到分
                endTime = TimeUtils.string2Millis(endTimeStr,Constant.NORMAL_TIME_FORMAT_);
                jsonObject.put("actionEndTime", endTime);
            }

            long startTime = recordEntity.getStartTime();
            if (startTime !=0){
                String startTimeStr = TimeUtils.millis2String(startTime,Constant.NORMAL_TIME_FORMAT_);
                //时间戳只精确到分
                startTime = TimeUtils.string2Millis(startTimeStr,Constant.NORMAL_TIME_FORMAT_);
            }
            jsonObject.put("actionStartTime", startTime);
            jsonObject.put("actionData", actionData);
            if (ObjectUtils.isEmpty(deviceId)) {
                deviceId = "";
            }
            jsonObject.put("deviceId", deviceId);

            Map<String, Object> params = new HashMap<>();
            params.put("userActionAddDTO", jsonObject.toString());


            Observable observable = RetrofitUtils.getInstance().getRequest(BsMonitoringApiService.class).addActionEvent(getRequestBody(jsonObject));

            requestFromServer(observable, false, new ResponseListener<Object, Object>() {
                @Override
                public void onSuccess(Object data, String msg) {
                    recordEntity.setUploadService(1);
                    ActionRecordRepository.getInstance().insert(recordEntity);
                }

                @Override
                public void onFail(int code, String message, Object errorData) {

                }

                @Override
                public void onError(String message) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传批量打卡数据
     *
     * @param recordEntity
     */
    public void uploadBatchClockInData(String deviceId, ActionRecordEntity recordEntity) {
        BatchActionData batchActionData = new BatchActionData();
        batchActionData.setType(recordEntity.getType());
        long startTime = recordEntity.getStartTime();
        if (startTime !=0){
            String timeStr = TimeUtils.millis2String(startTime,Constant.NORMAL_TIME_FORMAT_);
            //时间戳只精确到分
            startTime = TimeUtils.string2Millis(timeStr,Constant.NORMAL_TIME_FORMAT_);
        }
        batchActionData.setActionStartTime(startTime);
        batchActionData.setDeviceId(deviceId);
        String batchData = recordEntity.getBatchActionData();
        if (ObjectUtils.isNotEmpty(batchData)) {
            List<BatchActionData.ActionData> actionDataList = new Gson().fromJson(batchData,
                    new TypeToken<List<BatchActionData.ActionData>>() {
                    }.getType());
            batchActionData.setActionDataList(actionDataList);
        }

        Observable observable = RetrofitUtils.getInstance().getRequest(BsMonitoringApiService.class).batchAddActionEvent(getRequestBody(batchActionData));

        requestFromServer(observable, false, new ResponseListener<Object, Object>() {
            @Override
            public void onSuccess(Object data, String msg) {
                recordEntity.setUploadService(1);
                ActionRecordRepository.getInstance().insert(recordEntity);
            }

            @Override
            public void onFail(int code, String message, Object errorData) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }


    public void addUpdateDeviceTask(int status,long enableTime) {
        mSingleThreadExecutor.execute(new UpdateDeviceStatusTask(status,enableTime));
    }

    /**
     * 修改设备信息
     *
     */
    private void modifyDevice(DeviceEntity deviceEntity, String userId) {
        String deviceId = deviceEntity.getDeviceId();
        if (ObjectUtils.isEmpty(deviceId)) return;
        String deviceName = deviceEntity.getDeviceName();
        int deviceStatus = deviceEntity.getDeviceStatus();
        DeviceModifyRequestBean modifyRequestBean = new DeviceModifyRequestBean();
        modifyRequestBean.setStatus(deviceStatus);
        long enableTime = deviceEntity.getFirstBsMill();
        if (enableTime != 0) {
            modifyRequestBean.setEnableTime(enableTime);
            modifyRequestBean.setLastTime(enableTime + 14 * 24 * 60 * 60 * 1000l);
        }
        Gson gson = new Gson();
        RequestBody requestBody = getRequestBody(gson.toJson(modifyRequestBean));

        Observable<BaseResponse<Boolean, Object>> observable = RetrofitUtils.getInstance().getRequest(DeviceApiService.class)
                .modifyDeviceInfo(deviceId, requestBody);

        requestFromServer(observable, false, new ResponseListener<Boolean, Object>() {
            @Override
            public void onSuccess(Boolean data, String msg) {
                //本地的状态和上传成功的状态相比,如果此时本地还是相同的状态，则下次不用再重复上传
                DeviceEntity deviceEntity1 = DeviceRepository.getInstance().queryDevice(userId, deviceId);
                int uploadStatus;
                if (deviceEntity1.getDeviceStatus() == deviceStatus){
                    uploadStatus = 1;
                }else {
                    uploadStatus = 0;
                }
                AppDatabase.getInstance().getDeviceDao().updateDeviceUploadStatus(userId,deviceName, uploadStatus);
            }

            @Override
            public void onFail(int code, String message, Object errorData) {
                LogUploadModel.getInstance().uploadConnectInfo("上传设备状态:onFail----message="+message+",errorData:"+errorData);
            }

            @Override
            public void onError(String message) {
                LogUploadModel.getInstance().uploadConnectInfo("上传设备状态:onError----message="+message);
            }
        });
    }

    /**
     * 蓝牙连接成功后调用新增设备接口
     */
    public void addDevice(String bluetoothNum, String macAddress, String deviceName, String algorithmVersion) {

        DeviceAddRequestBean deviceAddRequestBean = new DeviceAddRequestBean();
        deviceAddRequestBean.setBlueToothNum(bluetoothNum);
        deviceAddRequestBean.setMacAddress(macAddress);
        deviceAddRequestBean.setName(deviceName);
        deviceAddRequestBean.setStatus(0);
        deviceAddRequestBean.setUserId(UserInfoUtils.getUserId());
        //设备类型(1:CGM 2:CKM 3:CLM)
        deviceAddRequestBean.setDeviceType(1);
        deviceAddRequestBean.setAlgorithmVersion(algorithmVersion);
        DeviceAddRequestBean.AddressDTO addressDTO = getAddressDTO();
        if (addressDTO != null) {
            deviceAddRequestBean.setAddress(addressDTO);
        }

        Model model = new Model();
        RequestBody requestBody = model.getRequestBody(deviceAddRequestBean);
        Observable<BaseResponse<String, String>> observable = RetrofitUtils.getInstance().getRequest(DeviceApiService.class)
                .addDeviceInfo(requestBody);
        model.requestFromServer(observable, false, new ResponseListener<String, String>() {
            @Override
            public void onSuccess(String data, String msg) {
                //请求成功
                updateDeviceId(deviceName, data);

            }

            @Override
            public void onFail(int code, String message, String errorData) {
                if (code == 203006) {
                    //设备已存在
                    //设备号
                    updateDeviceId(deviceName, errorData);
                } else {
                    ToastUtils.showShort(message);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void updateDeviceId(String deviceName, String data) {
        AppDatabase.getInstance().getDeviceDao().updateDeviceId(deviceName, data);
    }

    private DeviceAddRequestBean.AddressDTO getAddressDTO() {
        DeviceAddRequestBean.AddressDTO addressDTO = null;
        String locationInfo = UserInfoUtils.getLocationInfo();
        if (ObjectUtils.isNotEmpty(locationInfo)) {
            LocationEntity locationEntity = GsonUtils.fromJson(locationInfo, LocationEntity.class);
            if (ObjectUtils.isNotEmpty(locationEntity)) {
                addressDTO = new DeviceAddRequestBean.AddressDTO();
                addressDTO.setCityName(locationEntity.getCity());
                addressDTO.setDetailAddress(locationEntity.getDetail());
                addressDTO.setProvinceName(locationEntity.getProvinceName());
                addressDTO.setLatitude(locationEntity.getLatitude() + "");
                addressDTO.setLongitude(locationEntity.getLongitude() + "");
            }
        }
        return addressDTO;
    }

    private int getTestIndex(boolean test, int originIndex) {
        if (test && com.sisensing.common.BuildConfig.DEBUG) {
            return 1995;
        }
        return originIndex;
    }

}
