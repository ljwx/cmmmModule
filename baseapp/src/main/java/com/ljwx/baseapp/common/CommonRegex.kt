package com.ljwx.baseapp.common

object CommonRegex {
    //邮箱
    val email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"

    //整数
    val integer = "^[0-9]*\$"

    //小数
    val decimal = "^(-?\\d+)(\\.\\d+)?\$"

    //英文和数字
    val letterAndNumber = "^[A-Za-z0-9]+\$"

    //字母
    val letter = "^[A-Za-z]+\$"

    //数字,字母,下划线
    val letterAndNumberAndUnderline = "^\\w+\$"

    //以字母开头，长度在6~18之间，只能包含字母、数字和下划线
    val password = "^[a-zA-Z]\\w{5,17}\$"

    //日期格式
    val commonDate = "^\\d{4}-\\d{1,2}-\\d{1,2}"

    //价格,不以0开头,可以有小数,1位或2位
    val price = "^[0-9]+(.[0-9]{1,2})?\$"
}
