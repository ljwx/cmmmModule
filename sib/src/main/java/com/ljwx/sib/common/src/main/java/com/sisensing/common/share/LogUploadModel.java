package com.sisensing.common.share;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.sisensing.common.base.Model;
import com.sisensing.common.base.ResponseListener;
import com.sisensing.common.base.RetrofitUtils;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.entity.login.ConnectInfoRequestBean;
import com.sisensing.common.net.LoginApiService;
import com.sisensing.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class LogUploadModel extends Model {


    private LogUploadModel() {

    }

    private static class SingletonHolder {
        private static final LogUploadModel INSTANCE = new LogUploadModel();
    }

    public static final LogUploadModel getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 上传终端连接信息
     *
     * @param s
     */
    public void uploadConnectInfo(String s) {
        if (ObjectUtils.isNotEmpty(s)) {
            Model model = new Model();
            ConnectInfoRequestBean connectInfo = new ConnectInfoRequestBean();
            connectInfo.setConnInfo(s);
            connectInfo.setModule(DeviceUtils.getModel());
//            connectInfo.setReportTime(TimeUtils.millis2String(System.currentTimeMillis(), Constant.NORMAL_TIME_PATTERN));
            connectInfo.setReportTime(System.currentTimeMillis()+"");
            RequestBody requestBody = model.getRequestBody(connectInfo);
            Observable<BaseResponse<Object, Object>> observable = RetrofitUtils.getInstance().getRequest(LoginApiService.class)
                    .uploadConnectInfo(requestBody);
            model.requestFromServer(observable, false, new ResponseListener<Object, Object>() {
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

}
