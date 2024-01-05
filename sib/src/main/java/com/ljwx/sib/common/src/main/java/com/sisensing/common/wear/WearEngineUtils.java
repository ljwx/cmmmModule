package com.sisensing.common.wear;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.wearengine.HiWear;
import com.huawei.wearengine.auth.AuthCallback;
import com.huawei.wearengine.auth.AuthClient;
import com.huawei.wearengine.auth.Permission;
import com.huawei.wearengine.device.Device;
import com.huawei.wearengine.device.DeviceClient;
import com.huawei.wearengine.p2p.Message;
import com.huawei.wearengine.p2p.P2pClient;
import com.huawei.wearengine.p2p.Receiver;
import com.huawei.wearengine.p2p.SendCallback;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.user.UserInfoUtils;

import java.nio.charset.Charset;
import java.util.List;

import static com.sisensing.common.wear.Constants.MESSAGE_ALARM;
import static com.sisensing.common.wear.Constants.MESSAGE_BLOOD_SYNC;
import static com.sisensing.common.wear.Constants.MESSAGE_PHONE_APP_OPEN;
import static com.sisensing.common.wear.Constants.MESSAGE_USER_INFO;
import static com.sisensing.common.wear.Constants.MESSAGE_VALID;


/**
 * ProjectName: CGM_C
 * Package: com.sisensing.wearengine
 * Author: f.deng
 * CreateDate: 2021/5/28 14:23
 * Description:
 */
public class WearEngineUtils {

   /* private static final String TAG = "WearEngineUtils";

    private static final String DEVICE_PKGNAME = "com.sisensing.cgm";
    private static final String DEVICE_FINGER = "com.sisensing.cgm_BIOyorVTU2QTDou0MjZi1MRibfbj3nW086JjM3W63OOJcy3+5S0/lobMyW/gIzUN0/4IGD7sM6dgzNKefZEevhc=";



    private Device mConnectDevice;

    private Receiver mReceiver;

    private P2pClient mP2pClient;


    private WearEngineUtils() {

    }

    private static class Inner {
        private static WearEngineUtils INSTANCE = new WearEngineUtils();

    }

    public static WearEngineUtils getInstance() {
        return Inner.INSTANCE;
    }


    public void init(Context context) {
        // 步骤2：获取点对点通信对象
        mP2pClient = HiWear.getP2pClient(context);

        // 步骤3：指定需要通信的穿戴设备侧应用包名
        mP2pClient.setPeerPkgName(DEVICE_PKGNAME);

        // 步骤4：指定需要通信的穿戴设备侧应用签名证书指纹
        mP2pClient.setPeerFingerPrint(DEVICE_FINGER);


        checkHasAvailableDevices(context);


    }


    *//**
     * 查询可用穿戴设备
     *//*
    private void checkHasAvailableDevices(Context context) {
        // 步骤1：获取DeviceClient对象
        //this 表示应用上下文Context对象
        DeviceClient deviceClient = HiWear.getDeviceClient(context);

        // 步骤2：调用 hasAvailableDevices 方法，查询用户是否有可用穿戴设备
        deviceClient.hasAvailableDevices().addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                //查询是否有可用设备任务执行成功，result值表示是否有可用设备
                if (result) {
                    getBondedDevices(context);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //查询是否有可用设备任务执行失败

                Log.e(TAG, e.getMessage());
            }
        });
    }


    *//**
     * 手机侧应用获取已配对的华为穿戴设备列表，并从设备列表中选定需要操作的设备。
     *
     * @param context
     *//*
    private void getBondedDevices(Context context) {

        //步骤1：获取DeviceClient对象
        DeviceClient deviceClient = HiWear.getDeviceClient(context);

        //步骤2：获取已配对的设备列表
        deviceClient.getBondedDevices()
                .addOnSuccessListener(new OnSuccessListener<List<Device>>() {
                    @Override
                    public void onSuccess(List<Device> devices) {
                        //获取到的设备列表
                        //步骤3：从设备列表选定需要操作的设备，必须是已连接的设备，不然后续操作会失败。
                        if (devices != null && !devices.isEmpty()) {
                            //说明：该部分为获取已连接的设备的代码逻辑，开发者需要根据实际情况修改，获取需要通信的设备
                            for (Device device : devices) {
                                if (device.isConnected()) {
                                    mConnectDevice = device;
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //获取设备列表异常时，处理相关的逻辑
                    }
                });

    }


    *//**
     * 查询用户授权结果
     *//*
    public void checkPermissions(Context context) {

        // 步骤1：获取AuthClient对象
        AuthClient authClient = HiWear.getAuthClient(context);

        // 或调用checkPermissions方法，查询一组权限是否授予
        Permission[] permissions = {Permission.DEVICE_MANAGER, Permission.NOTIFY};
        // 步骤2：调用checkPermission方法，查询权限是否授予

        authClient.checkPermissions(permissions).addOnSuccessListener(new OnSuccessListener<Boolean[]>() {
            @Override
            public void onSuccess(Boolean[] booleans) {
                // 返回权限是否授予，true为授予，false为未授予，按照权限的查询顺序返回对应的值。

                for (int i = 0; i < booleans.length; i++) {
                    if (!booleans[i]) {
                        requestPermissions(context);
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 接口调用失败

            }
        });


    }


    *//**
     * 请求权限
     *//*
    private void requestPermissions(Context context) {

        // 步骤1：获取AuthClient对象
        //this 表示应用上下文Context对象
        AuthClient authClient = HiWear.getAuthClient(context);

        // 步骤2：定义用户授权的回调对象
        AuthCallback authCallback = new AuthCallback() {
            @Override
            public void onOk(Permission[] permissions) {
                //返回用户授予的权限列表
            }

            @Override
            public void onCancel() {
                //用户取消授权
            }
        };

        // 步骤3：申请需要的权限（如：DEVICE_MANAGER，设备管理权限）
        authClient.requestPermission(authCallback, Permission.DEVICE_MANAGER)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void successVoid) {
                        //请求授权任务执行成功
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //请求授权任务执行失败
            }
        });
        //说明：可一次申请多个权限，多个权限之间用逗号分隔：authClient.requestPermission(authCallback, Permission.DEVICE_MANAGER,Permission.NOTIFY,其他权限)
        //说明： 调用该API后，会弹出授权界面，让用户选择授予权限
    }


    *//**
     * 穿戴侧App是否安装
     *
     * @param
     *//*
    public void isAppInstalled(OnSuccessListener<Boolean> listener) {

        // 步骤3：检测穿戴设备侧对应的第三方应用是否安装
        if (mConnectDevice != null && mConnectDevice.isConnected()) {
            mP2pClient.isAppInstalled(mConnectDevice, DEVICE_PKGNAME)
                    .addOnSuccessListener(listener);
        }
    }

    *//**
     * 向穿戴侧设备发送ping命令
     *
     * @param
     * @param
     *//*
    public void pingWearable(PingCallback callback) {

        // 步骤4：检测穿戴设备侧对应的第三方应用是否在线
        if (mConnectDevice != null && mConnectDevice.isConnected()) {
            mP2pClient.ping(mConnectDevice, new com.huawei.wearengine.p2p.PingCallback() {
                @Override
                public void onPingResult(int i) {
                    //方法的 PingCallback 是收到设备返回信息接口，属于设备给手机回响应的过程
                    callback.onPingResult(i);
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void successVoid) {
                    // ping任务执行成功时三方应用处理相关的逻辑
                    callback.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    // ping任务执行失败时三方应用处理相关的逻辑
                    callback.onFailure();
                }
            });
        }
    }


    public interface PingCallback {

        void onPingResult(int i);

        void onSuccess();

        void onFailure();
    }


    *//**
     * 向穿戴设备侧发送消息
     *
     * @param
     *//*
    public void sendMessage(String message, SendCallback sendCallback) {

        // 构造Message对象
        Message.Builder builder = new Message.Builder();
        builder.setPayload(message.getBytes(Charset.defaultCharset()));
        Message sendMessage = builder.build();

        if (mConnectDevice != null && mConnectDevice.isConnected() && sendMessage != null && sendCallback != null) {
            mP2pClient.send(mConnectDevice, sendMessage, sendCallback);
        }

    }

    *//**
     * 发送报警消息
     *
     * @param value
     *//*
    public void sendAlarm(float value) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type", MESSAGE_ALARM);
        jsonObject.put("data", value + "");

        sendMessage(jsonObject.toString(), new SendCallback() {
            @Override
            public void onSendResult(int i) {
                if (i != 207) {
                    pingWearable(new PingCallback() {
                        @Override
                        public void onPingResult(int i) {
                            if (i == 202) {
                                sendAlarm(value);
                            }
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            }

            @Override
            public void onSendProgress(long l) {

            }
        });
    }


    *//**
     * 接收穿戴设备侧第三方应用发过来的点对点消息
     *//*
    public void registerMessageReceiver(Receiver receiver) {

        mReceiver = receiver;

        // 步骤5：手机第三方应用接收穿戴设备侧第三方应用发过来的消息或文件
        if (mConnectDevice != null && mConnectDevice.isConnected()) {
            mP2pClient.registerReceiver(mConnectDevice, mReceiver)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {


                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }

    }

    *//**
     * 取消穿戴侧设备消息订阅
     *
     * @param
     *//*
    public void unregisterReceiver() {
        if (mReceiver != null) {
            // 步骤6：手机第三方应用取消接收穿戴设备侧第三方应用发过来的消息或文件
            mP2pClient.unregisterReceiver(mReceiver)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // 手机侧第三方应用取消接收消息或文件成功
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // 手机侧第三方应用取消接收消息或文件失败
                        }
                    });
        }

    }

    *//**
     * 向手表发送App启动消息
     *//*
    public void sendAppLaunched() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type", MESSAGE_PHONE_APP_OPEN);

        sendMessage(jsonObject.toString(), new SendCallback() {
            @Override
            public void onSendResult(int i) {
            }

            @Override
            public void onSendProgress(long l) {
            }
        });
    }


    *//**
     * 向手表发送App发送传感器过期消息
     *//*
    public void sendValid() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", MESSAGE_VALID);

        sendMessage(jsonObject.toString(), new SendCallback() {
            @Override
            public void onSendResult(int i) {
            }

            @Override
            public void onSendProgress(long l) {
            }
        });
    }


    *//**
     * 向穿戴设备侧发送用户信息
     *//*
    public void sendUserInfo() {

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("drType", UserInfoUtils.getDrType());
        jsonUser.put("deviceName", DeviceManager.getInstance().getDeviceEntity().getDeviceName());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", MESSAGE_USER_INFO);
        jsonObject.put("data", jsonUser);


        sendMessage(jsonObject.toString(), new SendCallback() {
            @Override
            public void onSendResult(int i) {

            }

            @Override
            public void onSendProgress(long l) {
            }
        });
    }

    *//**
     * 同步血糖数据
     *
     * @param bloodGlucoseEntity
     *//*
    public void sendSyncBlood(BloodGlucoseEntity bloodGlucoseEntity) {

        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("type", MESSAGE_BLOOD_SYNC);
        com.alibaba.fastjson.JSONObject jsonBlood = new com.alibaba.fastjson.JSONObject();
        jsonBlood.put("index", bloodGlucoseEntity.getIndex());
        jsonBlood.put("glucoseValue", bloodGlucoseEntity.getGlucoseValue());
        jsonBlood.put("processedTimeMill", bloodGlucoseEntity.getProcessedTimeMill());
        jsonBlood.put("glucoseTrend", bloodGlucoseEntity.getGlucoseTrend());
        jsonBlood.put("bleName", bloodGlucoseEntity.getBleName());
        jsonObject.put("data", jsonBlood);

        WearEngineUtils.getInstance().sendMessage(jsonObject.toJSONString(), new SendCallback() {
            @Override
            public void onSendResult(int i) {

            }

            @Override
            public void onSendProgress(long l) {

            }
        });

    }*/
}