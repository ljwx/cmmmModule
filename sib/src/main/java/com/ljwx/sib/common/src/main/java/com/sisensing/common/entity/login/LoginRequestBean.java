package com.sisensing.common.entity.login;

/**
 * @author y.xie
 * @date 2021/3/5 11:39
 * @desc 登录请求参数javaBean
 */
public class LoginRequestBean {
    private String email;
    private String password;

    public LoginRequestBean(String account, String password) {
        this.email = account;
        this.password = password;
    }
}
