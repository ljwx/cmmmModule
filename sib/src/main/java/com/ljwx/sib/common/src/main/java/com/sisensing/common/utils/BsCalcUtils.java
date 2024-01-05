package com.sisensing.common.utils;

import android.util.ArrayMap;

import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.entity.BloodGlucoseEntity.BgEvent;
import com.sisensing.common.entity.personalcenter.PersonalInfoEntity;
import com.sisensing.common.user.UserInfoUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.utils
 * @Author: l.chenlu
 * @CreateDate: 2021/6/2 10:48
 * @Description:
 */
public class BsCalcUtils {


    /**
     * 后端接口返回的糖尿病类型与算法的不一致，需要转换
     * @return
     */
    private static int getChangeDrType(){
        int result = 0;
        int type = UserInfoUtils.getDrType();
        switch (type){
            case 1:
            case 2:
            case 3:
                result = 1;
                break;
            case 4:
                result = 2;
                break;
            case 5:
            case 6:
                result = 3;
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }

    private static float[] getThresholdArray(){
        float[] threshold = new float[4];
        PersonalInfoEntity entity = UserInfoUtils.getPersonalInfo();
        if(ObjectUtils.isEmpty(entity)){
            return threshold;
        }
        PersonalInfoEntity.TargetDTO targetDTO = entity.getTarget();
        if (ObjectUtils.isEmpty(targetDTO)){
            return threshold;
        }
        threshold[0] = targetDTO.getLower_2();
        threshold[1] = targetDTO.getLower_1();
        threshold[2] = targetDTO.getUpper_1();
        threshold[3] = targetDTO.getUpper_2();
        return threshold;
    }

    /**
     *计算TIR
     * tirArray、tirTimeArray数组长度5
     * @param glucose 输入的血糖数组
     * @param tirArray tir百分比，tir[0]对应极低，tir[1]对应低，tir[2]对应正常，tir[3]对应高，tir[4]对应极高
     * @param tirTimeArray tir时间长度,单位秒
     */
    public static void getTirOld(float[] glucose, float[] tirArray, long[] tirTimeArray){
        int[] tirCodeArray = new int[glucose.length];

        int result = JniUtils.genTirCodeArray(glucose,tirCodeArray, getChangeDrType(),getThresholdArray());
        if (result!=200){
            return;
        }
        JniUtils.calcTirOld(tirCodeArray, tirArray, tirTimeArray);
    }

    /**
     *计算TIR
     * tirArray、tirTimeArray数组长度5
     * @param glucose 输入的血糖数组
     * @param tirArray tir百分比，tir[0]对应极低，tir[1]对应低，tir[2]对应正常，tir[3]对应高，tir[4]对应极高
     * @param tirTimeArray tir时间长度,单位秒
     */
    public static void getTir(float[] glucose,float[] tirArray,long[] tirTimeArray){
        int[] tirCodeArray = new int[glucose.length];

        int result = JniUtils.genTirCodeArray(glucose,tirCodeArray, getChangeDrType(),getThresholdArray());
        if (result!=200){
            return;
        }
        JniUtils.calcTir(tirCodeArray, tirArray);
    }

    /**
     * 变异系数
     * @param glucose 输入的血糖数组
     * @return
     */
    public static float getCv(float[] glucose){
        float mean = JniUtils.calcMean(glucose);
        float calcStd = JniUtils.calcStd(glucose);
        float[] calcCv = new float[1];
        int statusCode = JniUtils.calcCv(calcStd,mean,calcCv);

        if(statusCode!=200){
            return 0;
        }
        if(Float.isInfinite(calcCv[0])||Float.isNaN(calcCv[0])){
            return 0;
        }
        return BigDecimalUtils.round(calcCv[0]*100,1);
    }

    /**
     * 计算均值
     * @param glucose 输入的血糖数组
     * @return
     */
    public static float getMean(float[] glucose, boolean round){
        float calMean = JniUtils.calcMean(glucose);
        if(Float.isInfinite(calMean)||Float.isNaN(calMean)){
            return 0;
        }
        if (round) {
            return BigDecimalUtils.round(calMean,1);
        } else {
            return calMean;
        }
    }

    /**
     * 返回预估糖化血红蛋白
     * @param glucose 输入的血糖数组
     * @return
     */
    public static float geteHb1c(float[] glucose){
        float mean = JniUtils.calcMean(glucose);
        if(Float.isInfinite(mean)||Float.isNaN(mean)){
            return 0;
        }
        float mg2eA1c = JniUtils.mg2eA1c(mean);
        if(Float.isInfinite(mg2eA1c)||Float.isNaN(mg2eA1c)){
            return 0;
        }
        return BigDecimalUtils.round(mg2eA1c,1);
    }

    /**
     * 平均血糖值转换为葡萄糖管理指标
     * @param glucose 平均血糖
     * @return
     */
    public static float getGMI(float[] glucose){
        int type = ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId()) ? 1 : 0;
        float mean = JniUtils.mg2gmi(getMean(glucose, false), 1);
        BigDecimal bigDecimal = new BigDecimal(mean).setScale(1, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.floatValue();
    }

    /**
     @brief: 计算平均血糖波动幅度。
     @param: array：血糖数组，float*
     @return: 平均血糖波动幅度,float
     */
//    public static float getMage(float[] glucose){
//        float std = JniUtils.calcStd(glucose);
//        if(Float.isInfinite(std)||Float.isNaN(std)){
//            return 0;
//        }
//        float calcMage = JniUtils.calcMage(glucose,std);
//        if(Float.isInfinite(calcMage)||Float.isNaN(calcMage)){
//            return 0;
//        }
//        return BigDecimalUtils.round(calcMage,1);
//    }

    /**
     *
     * @param currentGlucose
     * @param codeCurrentArray
     * @param refGlucose
     * @param codeRefArray
     * @return
     */
//    public static float getModd(float[] currentGlucose,long[] codeCurrentArray,float[] refGlucose,long[] codeRefArray){
//
////        int currentRemainder = JniUtils.calcRemainder(codeCurrentArray[0]);
////        int refRemainder = JniUtils.calcRemainder(codeRefArray[0]);
//
//        int[] codearray = new int[codeCurrentArray.length];
////        long[] correctTstp = new long[codeCurrentArray.length];
//        JniUtils.genTimecodeCorrectDatetime(codeCurrentArray,codearray);
//
//        int[] refCodearray = new int[codeRefArray.length];
//        long[] refCorrectTstp = new long[codeRefArray.length];
//        JniUtils.genTimecodeCorrectDatetime(codeRefArray,refCodearray);
//
//        float modd = JniUtils.calcModd(currentGlucose,codearray,refGlucose,refCodearray);
//        if(Float.isInfinite(modd)||Float.isNaN(modd)){
//            return 0;
//        }
//        return BigDecimalUtils.round(modd,1);
//    }

    /**
     * 计算高低血糖风险
     * @param currentGlucose
     * @return 返回长度为2的float数组，0：低血糖风险，1：高血糖风险
     */
//    public static float[] getLbgiHbgi(float[] currentGlucose){
//        float[] lbgiHbgiArray = new float[2];
//        float[] lbgi = new float[1];
//        float[] hbgi = new float[1];
//        int result = JniUtils.calcLbgiHbgi(currentGlucose,lbgi,hbgi);
//        if (result==200){
//            lbgiHbgiArray[0] = lbgi[0];
//            lbgiHbgiArray[1] = hbgi[0];
//        }
//        return lbgiHbgiArray;
//    }
    /**
     *
     * @param currentGlucose 当前的血糖数据
     * @param codeCurrentArray 当前血糖数据的时间（单位秒）
     * @return 返回长度为3的int型数组，0：低血糖事件，1：夜间低血糖事件，2：高血糖事件
     */
    public static Map<Integer,List<BgEvent>> getEvent(float[] currentGlucose,long[] codeCurrentArray){
        //全天低血糖事件
        List<BgEvent> tbrEvent = new ArrayList<>();
        //夜间低血糖事件
        List<BgEvent> tbrEventNight = new ArrayList<>();
        //全天高血糖事件
        List<BgEvent> tarEvent = new ArrayList<>();

        Map<Integer,List<BgEvent>> map = new ArrayMap<>();
        map.put(0,tbrEvent);
        map.put(1,tbrEventNight);
        map.put(2,tarEvent);
//        int remainder = JniUtils.calcRemainder(codeCurrentArray[0]);
        int[] correntCodeArray = new int[codeCurrentArray.length];
        long[] correctTstp = new long[codeCurrentArray.length];
        JniUtils.genTimecodeCorrectDatetime(codeCurrentArray,correntCodeArray);

        int[] codeArray = new int[currentGlucose.length];
        int result = JniUtils.genTirCodeArray(currentGlucose, codeArray,getChangeDrType(),getThresholdArray());
        if(result!=200){
            return map;
        }
        int arrayLength = (int)(currentGlucose.length*2/3);
        //通过calcEvent计算出来的高低血糖事件（1是高血糖，-1是低血糖)
        int[] tirTypeArray = new int[arrayLength];
        //通过calcEvent计算出来的高低血糖事件对应的起始时间
        long[] startArray = new long[arrayLength];
        //通过calcEvent计算出来的高低血糖事件对应的结束时间
        long[] endArray = new long[arrayLength];
        //返回的数组极值（最高、最低血糖）
        float[] extreValues = new float[arrayLength];
        //通过calcEvent计算出来的高低血糖事件总数
        int[] eventNum = new int[1];
        int eventResult = JniUtils.calcEvent(currentGlucose,codeArray,correctTstp,tirTypeArray,startArray,endArray,extreValues,eventNum);
        if (eventResult != 200){
            return map;
        }
        //统计血糖事件
        for (int i=0;i<eventNum[0];i++){
            if(Float.isInfinite(extreValues[i])||Float.isNaN(extreValues[i])){
                continue;
            }
            if (tirTypeArray[i] == 1){
                BgEvent event = new BgEvent(startArray[i]*1000, BigDecimal.valueOf(extreValues[i]),BigDecimal.valueOf((endArray[i]-startArray[i])/60));
                tarEvent.add(event);
            }
            if(tirTypeArray[i] == -1){
                BgEvent event1 = new BgEvent(startArray[i]*1000, BigDecimal.valueOf(extreValues[i]),BigDecimal.valueOf((endArray[i]-startArray[i])/60));
                tbrEvent.add(event1);
            }
        }
        map.put(0,tbrEvent);
        map.put(1,tbrEventNight);
        map.put(2,tarEvent);
        return map;
    }

    /**
     *
     * @param currentGlucose 当前的血糖数据
     * @param codeCurrentArray 当前血糖数据的时间（单位秒）
     * @return 返回长度为2的int型数组，0：低血糖事件，1：高血糖事件
     */
//    public static Map<Integer,List<BgEvent>> getNightEvent(float[] currentGlucose,long[] codeCurrentArray){
//        //全天低血糖事件
//        List<BgEvent> tbrEvent = new ArrayList<>();
//        //全天高血糖事件
//        List<BgEvent> tarEvent = new ArrayList<>();
//
//        Map<Integer,List<BgEvent>> map = new ArrayMap<>();
//        map.put(0,tbrEvent);
//        map.put(1,tarEvent);
////        int remainder = JniUtils.calcRemainder(codeCurrentArray[0]);
//        int[] correntCodeArray = new int[codeCurrentArray.length];
//        long[] correctTstp = new long[codeCurrentArray.length];
//        JniUtils.genTimecodeCorrectDatetime(codeCurrentArray,correntCodeArray);
//
//        int[] codeArray = new int[currentGlucose.length];
//        int result = JniUtils.genTirCodeArray(currentGlucose, codeArray,getChangeDrType(),getThresholdArray());
//        if(result!=200){
//            return map;
//        }
//        int arrayLength = (int)(currentGlucose.length*2/3);
//        //通过calcEvent计算出来的高低血糖事件（1是高血糖，-1是低血糖)
//        int[] tirTypeArray = new int[arrayLength];
//        //通过calcEvent计算出来的高低血糖事件对应的起始时间
//        long[] startArray = new long[arrayLength];
//        //通过calcEvent计算出来的高低血糖事件对应的结束时间
//        long[] endArray = new long[arrayLength];
//        //返回的数组极值（最高、最低血糖）
//        float[] extreValues = new float[arrayLength];
//        //通过calcEvent计算出来的高低血糖事件总数
//        int[] eventNum = new int[1];
//        int eventResult = JniUtils.calceventnight(currentGlucose,codeArray,correctTstp,tirTypeArray,startArray,endArray,extreValues,eventNum);
//        if (eventResult != 200){
//            return map;
//        }
//        //统计血糖事件
//        for (int i=0;i<eventNum[0];i++){
//            if(Float.isInfinite(extreValues[i])||Float.isNaN(extreValues[i])){
//                continue;
//            }
//            if (tirTypeArray[i] == 1){
//                BgEvent event = new BgEvent(startArray[i]*1000, BigDecimal.valueOf(extreValues[i]),BigDecimal.valueOf((endArray[i]-startArray[i])/60));
//                tarEvent.add(event);
//            }
//            if(tirTypeArray[i] == -1){
//                BgEvent event1 = new BgEvent(startArray[i]*1000, BigDecimal.valueOf(extreValues[i]),BigDecimal.valueOf((endArray[i]-startArray[i])/60));
//                tbrEvent.add(event1);
//            }
//        }
//        map.put(0,tbrEvent);
//        map.put(1,tarEvent);
//        return map;
//    }
}
