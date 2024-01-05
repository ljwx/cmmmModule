package com.sisensing.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.sisensing.common.R;
import com.sisensing.common.ble.BlePermissionsUtils;
import com.sisensing.common.entity.LocationEntity;
import com.sisensing.common.user.UserInfoUtils;

import java.util.List;
import java.util.Locale;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.utils
 * Author: f.deng
 * CreateDate: 2021/7/9 17:37
 * Description:
 */
public class LocationUtils {

    private static BasePopupView locationPop;

    @SuppressLint("MissingPermission")
    public static void getLocation(Context context) {
        String locationProvider;
        //1.获取位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            LogUtils.d("没有可用的位置提供器");
            return;
        }

        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //LogUtils.e("Longitude:" + location.getLongitude() + "-----------Latitude:" + location.getLatitude());
            getAddress(context, location);
        } else {
            // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
            locationManager.requestLocationUpdates(locationProvider, 0, 0, mListener);
        }
    }


    private static LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        // 如果位置发生变化，重新显示
        @Override
        public void onLocationChanged(Location location) {

        }
    };

    /**
     * 逆地理编码 得到地址
     *
     * @param context
     * @return
     */
    public static void getAddress(Context context, Location mLocation) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();
        // TODO: 2021/8/20  geocoder.getFromLocation 直接在主线程中运行发现部分机型会抛异常,这里在子线程处理  https://blog.csdn.net/sunqihui22/article/details/93509434
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<List<Address>>() {
            @Override
            public List<Address> doInBackground() throws Throwable {
                List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                return address;
            }

            @Override
            public void onSuccess(List<Address> address) {
                if (ObjectUtils.isNotEmpty(address)) {
                    LocationEntity locationEntity = new LocationEntity();
                    locationEntity.setProvinceName(address.get(0).getAdminArea());
                    locationEntity.setCity(address.get(0).getLocality());
                    locationEntity.setDetail(address.get(0).getAddressLine(0));
                    locationEntity.setLongitude(address.get(0).getLongitude());
                    locationEntity.setLatitude(address.get(0).getLatitude());

                    UserInfoUtils.putLocationInfo(GsonUtils.toJson(locationEntity));

                    LogUtils.e("位置1", "得到当前位置" + address + "'\n"
                            + "经度：" + address.get(0).getLongitude() + "\n"
                            + "纬度：" + address.get(0).getLatitude() + "\n"
                            + "国家：" + address.get(0).getCountryName() + "\n"
                            + "省：" + address.get(0).getAdminArea() + "\n"
                            + "城市：" + address.get(0).getLocality() + "\n"
                            + "名称：" + address.get(0).getAddressLine(1) + "\n"
                            + "街道：" + address.get(0).getAddressLine(0)
                    );
                }
            }
        });
    }

    /**
     * 此方法必须在但在 fragment 或 activity 的 Lifecycle 变为 CREATED 状态之前调用,否侧无法启动ActivityResultLauncher
     *
     * @param activity
     * @return
     */
    public static ActivityResultLauncher registerForLocationResult(AppCompatActivity activity) {
        return activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    ToastUtils.showShort(R.string.location_server_is_launch);
//                } else {
//                    ToastUtils.showShort(R.string.location_server_not_launch);
//                }
            }
        });
    }

    /**
     * 打卡位置权限
     *
     * @param activity
     * @param launcher
     */
    public static void openLocationInfo(AppCompatActivity activity, ActivityResultLauncher launcher) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            //如果用户没有打开定位服务引导用户打开定位
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
               if (locationPop == null){
                   locationPop  =  new XPopup.Builder(activity)
                           .dismissOnBackPressed(false)
                           .dismissOnTouchOutside(false)
                           .asConfirm("", StringUtils.getString(R.string.launch_location_service_tips), new OnConfirmListener() {
                               @Override
                               public void onConfirm() {
                                   //java写法
                                   launcher.launch(intent);
                               }
                           }, null).show();
               }else {
                   locationPop.show();
               }
            }
        }
    }

    /**
     * 定位是否开启
     * @return
     */
    public static boolean isOpenLocation(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
    }

    public static boolean useLocation(boolean connect) {
        if (connect) {
            return !BlePermissionsUtils.moreAndroid12();
        } else {
            return false;
        }
    }
}
