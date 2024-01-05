package com.sisensing.common.utils;

import com.sisensing.common.constants.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author y.xie
 * @date 2021/6/8 19:27
 * @desc 正则表达式判断
 */
public class RegexUtils {
    /**
     * 校验手机号是否
     * @param phone
     * @return
     */
    public static boolean isChinaMobile(String phone){
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(Constant.CHINA_MOBILE);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(phone);
        return m.find();
    }

    /**
     * 邮箱校验
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        Pattern r = Pattern.compile(Constant.EMAIL_FORMAT);
        Matcher m = r.matcher(email);
        return m.find();
    }
}
