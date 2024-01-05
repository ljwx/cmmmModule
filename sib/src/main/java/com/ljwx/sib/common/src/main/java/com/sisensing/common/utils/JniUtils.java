package com.sisensing.common.utils;


public class JniUtils {

    static {
        System.loadLibrary("native-lib");
    }


    /**
    @brief: 根据第一个时间戳，计算与标准时间戳的差值。
        计算逻辑：
        删除秒数据： start_tstp = start_tstp - start_tstp % 60;
        分钟除5（即300秒）取余：remainder = minutes % 300。
    @param:
        input:
            start_datetime：第一个时间戳，long
    @ret:
        remainder：根据第一个时间戳计算得到余数，int。
    **/
//    public static native int calcRemainder(long tstp);

    /**
        @brief: 根据第一个时间戳校正时间戳，计算时间戳编码。
            编码逻辑：将一天24小时按5分钟为间隔编码为0~287；00:00~00:04为0；00:05~00:09为1，以此类推。
            时间戳校正逻辑：原始时间+remainder+diff
        @param:
            input:
                originTstp：输入时间数组，long*
                N：数组长度
                remainder：根据第一个时间戳计算得到余数，int；根据分数数值除以5分钟（300秒），取余数。
            output:
                codeArray:时间戳编码值数组,int*
                correctTstp：校正后的时间戳,long *
        @ret: void
    */
    public static native void genTimecodeCorrectDatetime(long[] originTstp,int[] codeArray);

    /**
        @brief: 在gen_tir_code函数的基础上处理数组
        @param:
            input:
                glucose：血糖值,float*
                type：糖尿病类型。0：正常；1：1、2型糖尿病；2：妊娠糖；3：老年糖；4：自定义；默认正常。
                threshold: 自定义值，包含4个值，分别为：极低阈值，低阈值，高阈值，极高阈值。仅在type为4时启用，float*
                            如果自定义不需要极低阈值或极高阈值，则只需将对应阈值设置为超出血糖值的范围即可。
                            例如，无需极低血糖阈值，则需threshold[0]=2.1(按照设定，血糖值不会低于2.2)；
                            同理无需极高血糖阈值，则需threshold[0]=25.1.
            output：
                tir_code_array,返回的编码后的数组
        @ret: 状态，200：运行正常；500：运行异常
        */
    public static native int genTirCodeArray(float[] glucose,int[] tirCodeArray,int type,float[] threshold);

    /**
        @brief: 计算均值。
        @param:
            input:
                array：血糖值数组，float*
        @ret: 血糖均值,float
        */
    public static native float calcMean(float[] array);

    /**
        @brief: 计算标准差。
        @param:
            input:
                array：血糖值数组，float*
        @ret: 血糖标准差，float
        */
    public static native float calcStd(float[] array);

    /**
        @brief: 平均血糖值转换为预估糖化血红蛋白。
        @param:
            input:
                mg：平均血糖,float
        @ret: 预估糖化血红蛋白值,float
        */
    public static native float mg2eA1c(float mg);

    /**
        @brief: 平均血糖值转换为葡萄糖管理指标。
        @param:
            input:
                mg：平均血糖,float
                flag: 血糖单位，0为mg/dL,1为mmol/L，默认为1。int
        @ret: 葡萄糖管理指标值,float
        */
    public static native float mg2gmi(float mg,int flag);

    /**
        @brief: 计算变异系数。
        @param:
            input:
                sdbg：血糖标准差,float
                mg：平均血糖,float
            output:
                返回的变异系数 cv(数组长度为1)
        @ret: 状态，200：运行正常；500：运行异常
        */
    public static native int calcCv(float sdbg, float mg,float[] cv);

    /**
        @brief: 计算平均血糖波动幅度。
        @param:
            input:
                 array：血糖数组，float*
                threshold：阈值，一般取24h血糖标准差
        @ret: 平均血糖波动幅度,float
        */
//    public static native float calcMage(float[] array, float threshold);

    /**
        @brief: 计算最大血糖波动幅度。
        @param:
            input:
                 array：血糖数组，float*
        @ret: 最大血糖波动幅度,float
        */
//    public static native float calcLage(float[] array);

    /**
        @brief: 计算日间血糖波动幅度。
        @param:
            input:
                 currentArray：当前血糖数据，float*
                codeCurrentArray：当前时间编码数据,int*
                 refArray：参考血糖数据，float*
                codeRefArray：参考时间编码数据,int*
        @ret: modd,float
        */
//    public static native float calcModd(float[] currentArray,int[] codeCurrentArray,float[] refArray,int[] codeRefArray);

    /**
        @brief: 计算tir。
        @param:
            input:
                codeArray：血糖水平编码数组，int*
            output:
                tirArray:tir百分比，float*,tir[0]对应极低，tir[1]对应低，tir[2]对应正常，tir[3]对应高，tir[4]对应极高，
                tirTimeArray:tir时间长度,long*,同tir
        @ret: void
        */
    public static native void calcTir(int[] codeArray,float[] tirArray);
    public static native void calcTirOld(int[] codeArray,float[] tirArray,long[] tirTimeArray);

    /**
        @brief: 计算血糖事件，两个测量点时间超过（等于）10分钟（即15分钟），记为1次血糖事件
        @param:
            input:
                bg_array:血糖数组，float*
                codeArray：血糖水平数组，int*
                correctDatetimeArray:时间戳数组，long*
            output:
                tirTypeArray:血糖水平数组，int*（-2：极低血糖事件，-1：低血糖事件，1：高血糖事件，2：极高血糖事件）
                startArray:开始时间,long*
                endArray:结束时间,long*
                extreValues:极值,float*
                eventNum:异常血糖事件计数，int
        @ret: 状态，200：运行正常；500：运行异常
        */
    public static native int calcEvent(float[] bg_array,int[] codeArray,long[] correctDatetimeArray,int[] tirTypeArray,long[] startArray,long[] endArray,float[] extreValues,int[] eventNum);

    /**
     @brief: 计算夜间血糖事件，两个测量点时间超过（等于）10分钟（即15分钟），记为1次血糖事件
     @param:
     input:
     bg_array:血糖数组，float*
     codeArray：血糖水平数组，int*
     correctDatetimeArray:时间戳数组，long*
     output:
     tirTypeArray:血糖水平数组，int*（-2：极低血糖事件，-1：低血糖事件，1：高血糖事件，2：极高血糖事件）
     startArray:开始时间,long*
     endArray:结束时间,long*
     extreValues:极值,float*
     eventNum:异常血糖事件计数，int
     @ret: 状态，200：运行正常；500：运行异常
     */
//    public static native int calceventnight(float[] bg_array,int[] codeArray,long[] correctDatetimeArray,int[] tirTypeArray,long[] startArray,long[] endArray,float[] extreValues,int[] eventNum);

    /**
     *
     * @param bg_array  血糖值数组，float*；（与calc_event的输入对应）
     * @param correctDatetimeArray 时间戳数组，long long*；（与calc_event的输入一致）
     * @param tirTypeArray calc的输出
     * @param startArray 起止时间
     * @param endArray 起止时间
     * @param eventNum 事件数
     * @param extreValues 异常血糖事件的极值数组，float*
     * @return 状态，200：运行正常；500：运行异常
     */
    //public static native int calcExtreValue(float[] bg_array,long[] correctDatetimeArray,int[] tirTypeArray,long[] startArray,long[] endArray,int eventNum,float[] extreValues);
    /**
        @brief: 计算低血糖风险指数、高血糖风险指数
        @param:
            input:
                bg_array：血糖数组
            output:
                lbgi[0]:低血糖风险指数
                hbgi[0]:高血糖风险指数
        @ret: 状态，200：运行正常；500：运行异常
        */
//    public static native int calcLbgiHbgi(float[] bg_array,float[] lbgi ,float[] hbgi);

    /**
        @brief: 根据分好的组，计算agp的5条曲线
        @param:
            input:
                bgArray：血糖数组，float*
                codeArray：时间戳编码数组，int*
            output：
                per5:5%血糖数组，float*
                per25:25%血糖数组，float*
                per50:50%血糖数组，float*
                per75:75%血糖数组，float*
                per95:95%血糖数组，float*
        @ret: void
        */
    public static native void genAgp(float[] bgArray,int[] codearray,float[] per5,float[] per25,float[] per50,float[] per75,float[] per95);
    public static native void genAgpOld(float[] bgArray,int[] codearray,float[] per5,float[] per25,float[] per50,float[] per75,float[] per95);

    /**
        @brief: 对agp曲线滤波,正反重复滤波14次
        @param:
            bg_array：输入的血糖值数组（分位数数组），float*
        @ret: 滤波后的数据
        */
    public static native float[] agpFilter(float[] bg_array, float[] smooth_array);
    public static native float[] agpFilterOld(float[] bg_array, float[] smooth_array);

    /**
        @brief: 按天将数据分组
        @param:
            input:
                bgArray：血糖值，float *
                codeArray:时间编码，int*
                datetimeArray:校正后时间戳，int*
            output:
                bgGroupArray:血糖分组，float **
                codeGroupArray：时间编码分组，int **
                datetimeGroupArray：时间戳分组，long **
                dayLenArray：每天探头值数量，int *
                durDays:天数，int
        @ret: 状态，200：运行正常；500：运行异常
        */
//    public static native int groupByDay(float[] bgArray,int[] codeArray,long[] dateTimeArray,float[][] bgGroupArray,int[][] codeGroupArray,long[][] datetimeGroupArray,int[] dayLenArray,int[] durDays);

}
