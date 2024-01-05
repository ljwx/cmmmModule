package com.ljwx.sib.base.src.main.java.com.sisensing.base;



public interface IBaseLogic {
    /**
     * 初始化界面传递参数
     */
    void initParam();
    /**
     * 初始化数据
     */
    void init();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
