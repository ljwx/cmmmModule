package com.sisensing.common.entity.login;

/**
 * @author y.xie
 * @date 2021/3/4 16:40
 * @desc 登陆成功bean
 */
public class LoginBean {
    private String access_token;
    private long expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }
}
