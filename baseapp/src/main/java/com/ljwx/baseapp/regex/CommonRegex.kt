package com.ljwx.baseapp.regex

//https://github.com/any86/any-rule
object CommonRegex {
    //邮箱
    val email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"

    //整数
    val number = "^\\d+\$"

    //小数
    val decimal = "^\\d+\\.\\d+\$"

    //英文和数字
    val letterAndNumber = "^[A-Za-z0-9]+\$"

    //字母
    val letter = "^[A-Za-z]+\$"

    //数字,字母,下划线
    val letterAndNumberAndUnderline = "^\\w+\$"

    //以字母开头，长度在6~18之间，只能包含字母、数字和下划线
    val password = "^[a-zA-Z]\\w{5,17}\$"

}

object PriceRegex {
    //价格,最终值可以为0,可以有小数,1位或2位 0.12,0,10.2
    val normalPrice =
        "(?:^[1-9]([0-9]+)?(?:\\.[0-9]{1,2})?$)|(?:^(?:0)$)|(?:^[0-9]\\.[0-9](?:[0-9])?$)"
}

object DateTimeRegex {
    //24小时时间 23:34:55
    val day24 = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d\$"

    //12小时制 11:35:34
    val day12 = "^(?:1[0-2]|0?[1-9]):[0-5]\\d:[0-5]\\d\$"

    //日期格式
    val commonDate = "^\\d{4}-\\d{1,2}-\\d{1,2}"
}


object OtherRegex {
    //版本号 1.2.3(最少两个.)
    val versionName = "^\\d+(?:\\.\\d+){2}\$"
}

object ChineseRegex {
    //中文姓名 葛二蛋,凯文·杜兰特 德科·我·诺维斯基
    val name = "^(?:[\\u4e00-\\u9fa5·]{2,16})\$"

    //中文/汉字
    val char =
        "^(?:[\\u3400-\\u4DB5\\u4E00-\\u9FEA\\uFA0E\\uFA0F\\uFA11\\uFA13\\uFA14\\uFA1F\\uFA21\\uFA23\\uFA24\\uFA27-\\uFA29]|[\\uD840-\\uD868\\uD86A-\\uD86C\\uD86F-\\uD872\\uD874-\\uD879][\\uDC00-\\uDFFF]|\\uD869[\\uDC00-\\uDED6\\uDF00-\\uDFFF]|\\uD86D[\\uDC00-\\uDF34\\uDF40-\\uDFFF]|\\uD86E[\\uDC00-\\uDC1D\\uDC20-\\uDFFF]|\\uD873[\\uDC00-\\uDEA1\\uDEB0-\\uDFFF]|\\uD87A[\\uDC00-\\uDFE0])+\$"
}

object URLRegex {
    //"例如: www.qq.com, https://vuejs.org/v2/api/#v-model, www.qq.99, //www.qq.com, www.腾讯.cs,
    // ftp://baidu.qq, http://baidu.com,
    // https://www.amap.com/search?id=BV10060895&city=420111&geoobj=113.207951%7C29.992557%7C115.785782%7C31.204369
    //      &query_type=IDQ&query=%E5%85%89%E8%B0%B7%E5%B9%BF%E5%9C%BA(%E5%9C%B0%E9%93%81%E7%AB%99)&zoom=10.15,
    // 360.com:8080/vue/#/a=1&b=2 , 反例: ...."
    val URL =
        "^(((ht|f)tps?):\\/\\/)?([^!@#\$%^&*?.\\s-]([^!@#\$%^&*?.\\s]{0,63}[^!@#\$%^&*?.\\s])?\\.)+[a-z]{2,6}\\/?"

    //必须带端口号的网址(或ip)
    //"例如: https://www.qq.com:8080,
    // 127.0.0.1:5050, baidu.com:8001,
    // http://192.168.1.1:9090 ,
    // 反例: 192.168.1.1, https://www.jd.com"
    val HOST_PORT = "^((ht|f)tps?:\\/\\/)?[\\w-]+(\\.[\\w-]+)+:\\d{1,5}\\/?\$"
}