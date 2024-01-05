package com.sisensing.common.utils;

import com.sisensing.common.entity.rxbusbean.RxBusFloatWindowBean;
import com.sisensing.common.entity.rxbusbean.RxBusGuardShipOpenOrClose;
import com.sisensing.common.entity.rxbusbean.RxBusGuardianshipHomeBean;
import com.sisensing.common.entity.rxbusbean.RxBusHomeBean;
import com.sisensing.common.entity.rxbusbean.RxBusShakeBean;
import com.sisensing.common.entity.rxbusbean.RxBusUnitChangeBean;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @ClassName RxBusCenterManager
 * @Description
 * @Author xieyang
 * @Date 2023/6/8 15:25
 */
public class RxBusManager {
    private static RxBusManager instance;
    private final RxBus bus;
    private Disposable mainActivityDispose;
    private Disposable guardianshipHomeDispose;
    private Disposable bsUnitChangeDisposable;
    private Disposable shakeDisposable;
    private Disposable floatWindowDisposable;
    private Disposable guardShipTimedOpenOrCloseDisposable;
    private RxBusManager() {
        bus = new RxBus();
    }

    public static RxBusManager getInstance() {
        if (instance == null) {
            instance = new RxBusManager();
        }
        return instance;
    }

    /**
     * MainActivity界面事件注册
     * @param consumeronsumer
     */
    public void registerMainActivityBus(Consumer<RxBusHomeBean> consumeronsumer){
        mainActivityDispose = bus.toObservable(RxBusHomeBean.class)
                .subscribe(consumeronsumer);
    }

    /**
     * MainActivity界面事件发送
     * @param rxBusHomeBean
     */
    public void postMainActivityBus(RxBusHomeBean rxBusHomeBean){
        bus.post(rxBusHomeBean);
    }

    /**
     * MainActivity界面事件取消注册
     */
    public void unRegisterMainActivityBus(){
        if (mainActivityDispose != null && !mainActivityDispose.isDisposed()) {
           mainActivityDispose.dispose();
        }
    }

    /**
     * 监护模式首页事件类型注册
     * @param consumer
     */
    public void registerGuardianshipHomeFragmentBus(Consumer<RxBusGuardianshipHomeBean> consumer){
        guardianshipHomeDispose = bus.toObservable(RxBusGuardianshipHomeBean.class)
                .subscribe(consumer);
    }

    /**
     * 监护模式首页事件类型发送
     * @param rxBusGuardianshipHomeBean
     */
    public void postGuardianshipHomeFragmentBus(RxBusGuardianshipHomeBean rxBusGuardianshipHomeBean){
        bus.post(rxBusGuardianshipHomeBean);
    }

    /**
     * 监护模式首页事件类型取消注册
     */
    public void unRegisterGuardianshipHomeFragmentBus(){
        if (guardianshipHomeDispose!=null && !guardianshipHomeDispose.isDisposed()){
            guardianshipHomeDispose.dispose();
        }
    }

    /**
     * 摇一摇事件注册
     * @param consumer
     */
    public void registerShakeBus(Consumer<RxBusShakeBean> consumer){
        shakeDisposable = bus.toObservable(RxBusShakeBean.class)
                .subscribe(consumer);
    }

    /**
     * 摇一摇事件发送
     * @param rxBusShakeBean
     */
    public void postShakeBus(RxBusShakeBean rxBusShakeBean){
        bus.post(rxBusShakeBean);
    }

    /**
     * 摇一摇事件取消注册
     */
    public void unRegisterShareBus(){
        if (shakeDisposable!=null && !shakeDisposable.isDisposed()){
            shakeDisposable.dispose();
        }
    }

    /**
     * 专业模式血糖悬浮窗事件注册
     * @param consumer
     */
    public void registerFloatWindowBus(Consumer<RxBusFloatWindowBean> consumer){
        floatWindowDisposable = bus.toObservable(RxBusFloatWindowBean.class)
                .subscribe(consumer);
    }

    /**
     * 专业模式血糖悬浮窗事件发送
     * @param rxBusFloatWindowBean
     */
    public void postFloatWindowBus(RxBusFloatWindowBean rxBusFloatWindowBean){
        bus.post(rxBusFloatWindowBean);
    }

    /**
     * 专业模式血糖悬浮窗事件取消注册
     */
    public void unRegisterFloatWindowBus(){
        if (floatWindowDisposable!=null && !floatWindowDisposable.isDisposed()){
            floatWindowDisposable.dispose();
        }
    }

    /**
     * 血糖单位切换事件注册
     * @param consumer
     */
    public void registerBsUnitChangeBus(Consumer<RxBusUnitChangeBean> consumer){
        bsUnitChangeDisposable = bus.toObservable(RxBusUnitChangeBean.class)
                .subscribe(consumer);
    }

    /**
     * 血糖单位切换事件发送
     * @param rxBusUnitChangeBean
     */
    public void postBsUnitChangeBus(RxBusUnitChangeBean rxBusUnitChangeBean){
        bus.post(rxBusUnitChangeBean);
    }

    /**
     * 血糖单位切换事件取消注册
     */
    public void unRegisterBsUnitChangeBus(){
        if (bsUnitChangeDisposable!=null && !bsUnitChangeDisposable.isDisposed()){
            bsUnitChangeDisposable.dispose();
        }
    }

    /**
     * 监护模式定时刷新开启或关闭事件注册
     * @param consumer
     */
    public void registerGuardShipOpenOrCloseBus(Consumer<RxBusGuardShipOpenOrClose> consumer){
        guardShipTimedOpenOrCloseDisposable = bus.toObservable(RxBusGuardShipOpenOrClose.class)
                .subscribe(consumer);
    }

    /**
     * 监护模式定时刷新开启或关闭事件发送
     * @param rxBusGuardShipOpenOrClose
     */
    public void postGuardShipOpenOrCloseBus(RxBusGuardShipOpenOrClose rxBusGuardShipOpenOrClose){
        bus.post(rxBusGuardShipOpenOrClose);
    }

    /**
     * 监护模式定时刷新开启或关闭事件取消注册
     */
    public void unRegisterGuardShipOpenOrCloseBus(){
        if (guardShipTimedOpenOrCloseDisposable!=null && !guardShipTimedOpenOrCloseDisposable.isDisposed()){
            guardShipTimedOpenOrCloseDisposable.dispose();
        }
    }
}
