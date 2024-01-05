package com.sisensing.common.entity.register;

/**
 * @author y.xie
 * @date 2021/3/5 11:35
 * @desc
 */
public class RegisterRequestBean {
    private String email;
    private String password;
    private String confirmPwd;
    private String code;

    public String privacyPolicyVersion;

    public String userAgreementVersion;

    //private String countryCode;

    public RegisterRequestBean(String email, String password,String confirmPwd, String userAccount) {
        this.email = email;
        this.password = password;
        this.confirmPwd = confirmPwd;
        this.code = userAccount;
      //  this.countryCode = countryCode;
    }
}
