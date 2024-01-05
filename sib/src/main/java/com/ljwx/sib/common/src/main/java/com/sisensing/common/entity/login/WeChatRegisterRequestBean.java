package com.sisensing.common.entity.login;

/**
 * @author y.xie
 * @date 2021/4/9 10:42
 * @desc 微信首次授权用户注册请求实体类
 */
public class WeChatRegisterRequestBean {

    /**
     * openId :
     * phone :
     * smsCode :
     */

    private String openId;
    private String phone;
    private String smsCode;

    public WeChatRegisterRequestBean(String openId, String phone, String smsCode) {
        this.openId = openId;
        this.phone = phone;
        this.smsCode = smsCode;
    }
}
