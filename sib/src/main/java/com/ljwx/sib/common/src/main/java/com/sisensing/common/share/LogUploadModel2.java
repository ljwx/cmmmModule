package com.sisensing.common.share;

import android.os.Build;
import android.util.Pair;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.sisensing.common.base.Model;
import com.sisensing.common.base.ResponseListener;
import com.sisensing.common.base.RetrofitUtils;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.entity.LogInfoEntity;
import com.sisensing.common.entity.LogInfoRequest;
import com.sisensing.common.entity.login.ConnectInfoRequestBean;
import com.sisensing.common.entity.login.TerminalInfoRequestBean;
import com.sisensing.common.net.LoginApiService;
import com.sisensing.http.BaseResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.RequestBody;

public class LogUploadModel2 extends Model {
    private final int MAX_SIZE = 10;
    private final Subject<LogInfoRequest> serializedSubject;
    private final LoginApiService apiService;
    private final List<LogInfoEntity> tenLogInfoList = new ArrayList<>();
    public LogUploadModel2() {

        serializedSubject = PublishSubject.<LogInfoRequest>create().toSerialized();
        apiService = RetrofitUtils.getInstance().getRequest(LoginApiService.class);

        initialize();
    }

    private static class SingletonHolder {
        private static final LogUploadModel2 INSTANCE = new LogUploadModel2();
    }

    public static final LogUploadModel2 getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void initialize() {
        serializedSubject
                .concatMap((Function<LogInfoRequest, ObservableSource<Pair<LogInfoRequest, BaseResponse<Object, Object>>>>) parameters ->
                        apiService.uploadConnectInfo(parameters.getRequestBody())
                                .map((Function<BaseResponse<Object, Object>, Pair<LogInfoRequest, BaseResponse<Object, Object>>>) baseResponse ->
                                        new Pair<>(parameters, baseResponse)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Pair<LogInfoRequest, BaseResponse<Object, Object>>>() {
                    @Override
                    public void accept(Pair<LogInfoRequest, BaseResponse<Object, Object>> pair) throws Exception {
                        LogInfoRequest parameters = pair.first;
                        BaseResponse<Object, Object> baseResponse = pair.second;
                        LogUtils.dTag("删除日志",GsonUtils.toJson(parameters.getLogInfoList()));
                        // 在这里，你可以访问到 parameters.logInfoList
                        List<LogInfoEntity> list = parameters.getLogInfoList();
                        if (ObjectUtils.isNotEmpty(list)){
                            AppDatabase.getInstance().getLogInfoEntityDao().deleteLogs(list);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 处理错误
                    }
                });

    }
    public synchronized void startUploadLogInfo(String log, boolean isUploadBsData) {
        long currentMills = System.currentTimeMillis();

        LogInfoEntity logInfoEntity = new LogInfoEntity();
        logInfoEntity.setTimestamp(currentMills);
        logInfoEntity.setLogInfo(log);

        AppDatabase.getInstance().getLogInfoEntityDao().insertLog(logInfoEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        logInfoEntity.setId(aLong);
                        if (isUploadBsData) {
                            List<LogInfoEntity> list = new ArrayList<>();
                            list.add(logInfoEntity);
                            //实时上传血糖的时候直接上传
                            uploadLogInfo(list);
                        } else {
                            tenLogInfoList.add(logInfoEntity);
                            if (tenLogInfoList.size() == MAX_SIZE){
                                uploadLogInfo(tenLogInfoList);
                                tenLogInfoList.clear();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.eTag("插入日志",throwable.getMessage());
                    }
                });

    }




    private void uploadLogInfo(List<LogInfoEntity> list) {
        Queue<Map<String, String>> logQueue = new LinkedList<>();
        for (LogInfoEntity logInfo:list) {
            Map<String, String> tempLogMap = new LinkedHashMap<>();
            tempLogMap.put(String.valueOf(logInfo.getTimestamp()), logInfo.getLogInfo());
            logQueue.offer(tempLogMap);
        }
        Model model = new Model();
        ConnectInfoRequestBean connectInfo = new ConnectInfoRequestBean();
        connectInfo.setConnInfo(GsonUtils.toJson(logQueue));
        connectInfo.setModule(DeviceUtils.getModel());
        //国际版时间值都需要时间戳
//        connectInfo.setReportTime(TimeUtils.millis2String(System.currentTimeMillis(), Constant.NORMAL_TIME_PATTERN));
        connectInfo.setReportTime(System.currentTimeMillis()+"");
        RequestBody requestBody = model.getRequestBody(connectInfo);
        LogInfoRequest logInfoRequest = new LogInfoRequest(list,requestBody);
        serializedSubject.onNext(logInfoRequest);
    }

    /**
     * 上传终端信息
     *
     * @param
     */
    public void uploadTerminalInfo() {
        TerminalInfoRequestBean terminalInfo = new TerminalInfoRequestBean();
        terminalInfo.setAppCode("GJDG");
        terminalInfo.setAppName("硅基动感");
        terminalInfo.setAppVersion(AppUtils.getAppVersionName());
        terminalInfo.setLanguage(LanguageUtils.getAppContextLanguage().getLanguage());
        terminalInfo.setManuFact(DeviceUtils.getManufacturer());
        terminalInfo.setMdType("android");
        terminalInfo.setModule(DeviceUtils.getModel());
        terminalInfo.setReportTime(TimeUtils.millis2String(System.currentTimeMillis(), Constant.NORMAL_TIME_PATTERN));
        terminalInfo.setSwVersion(DeviceUtils.getSDKVersionName());
        terminalInfo.setBrand(Build.BRAND);
        String[] abiArray = DeviceUtils.getABIs();
        if (ObjectUtils.isNotEmpty(abiArray)) {
            StringBuilder abiStr = new StringBuilder();
            for (String abi : abiArray) {
                abiStr.append(abi);
                abiStr.append(',');
            }
            terminalInfo.setCoreArch(abiStr.toString());
        }
        terminalInfo.setDisMetric(ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight());
        terminalInfo.setProduct(Build.PRODUCT);
        terminalInfo.setSdkVersion(String.valueOf(DeviceUtils.getSDKVersionCode()));
        terminalInfo.setSwRomVersion(Build.VERSION.INCREMENTAL);
        Observable<BaseResponse<Object, Object>> observable = RetrofitUtils.getInstance().getRequest(LoginApiService.class).uploadTerminalInfo(getRequestBody(terminalInfo));
        requestFromServer(observable, false, new ResponseListener<Object, Object>() {
            @Override
            public void onSuccess(Object data, String msg) {

            }

            @Override
            public void onFail(int code, String message, Object errorData) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }

}
