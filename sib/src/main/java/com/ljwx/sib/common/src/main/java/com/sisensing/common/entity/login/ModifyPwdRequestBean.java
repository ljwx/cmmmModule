package com.sisensing.common.entity.login;

/**
 * @author y.xie
 * @date 2021/3/16 18:51
 * @desc 修改密码成功实体类
 */
public class ModifyPwdRequestBean {
    private String password;
    private String code;
    private String email;
    private String confirmPwd;

    public ModifyPwdRequestBean(String email,String password, String code,String confirmPwd) {
        this.email = email;
        this.password = password;
        this.code = code;
        this.confirmPwd = confirmPwd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
