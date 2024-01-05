#include <jni.h>
#include "glucose_target.h"

extern "C" {
//JNIEXPORT jint JNICALL
//Java_com_sisensing_common_utils_JniUtils_calcRemainder(JNIEnv *jniEnv, jclass,jlong start_tstp){
//    int result = calc_remainder(start_tstp);
//    return result;
//}

JNIEXPORT void JNICALL
Java_com_sisensing_common_utils_JniUtils_genTimecodeCorrectDatetime(JNIEnv* env, jclass,jlongArray origin_tstp, jintArray code_array){

    jlong* origin_tstp_array = env->GetLongArrayElements(origin_tstp,JNI_FALSE);
    jint* code_array2 = env->GetIntArrayElements(code_array,JNI_FALSE);
//    jlong* corrent_tstp_array = env->GetLongArrayElements(correct_tstp,JNI_FALSE);
    int n = env->GetArrayLength(origin_tstp);
    gen_timecode((long long*)origin_tstp_array,n,code_array2);
    /*env->SetIntArrayRegion(code_array,0,n,code_array2);
    env->SetLongArrayRegion(correct_tstp,0,n,corrent_tstp_array);*/
    env->ReleaseLongArrayElements(origin_tstp,origin_tstp_array,0);
//    env->ReleaseLongArrayElements(correct_tstp,corrent_tstp_array,0);
    env->ReleaseIntArrayElements(code_array,code_array2,0);
}

JNIEXPORT jint JNICALL
Java_com_sisensing_common_utils_JniUtils_genTirCodeArray(JNIEnv* env, jclass,jfloatArray glucose,jintArray tir_code_array,jint type, jfloatArray threshold){

    float* glucosearray = env->GetFloatArrayElements(glucose,JNI_FALSE);
    int* tirCode = env->GetIntArrayElements(tir_code_array,JNI_FALSE);
    float* thresholdArray = env->GetFloatArrayElements(threshold,JNI_FALSE);
    int N = env->GetArrayLength(glucose);
    int result = gen_tir_code_array(glucosearray,N,tirCode,type,thresholdArray);
    env->ReleaseFloatArrayElements(glucose,glucosearray,0);
    env->ReleaseFloatArrayElements(threshold,thresholdArray,0);
    env->ReleaseIntArrayElements(tir_code_array,tirCode,0);
    return result;
}

JNIEXPORT jfloat JNICALL
Java_com_sisensing_common_utils_JniUtils_calcMean(JNIEnv* env,jclass,jfloatArray bg_array){
    float* array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
    int n = env->GetArrayLength(bg_array);
    float result = calc_mean(array,n);
    env->ReleaseFloatArrayElements(bg_array,array,0);
    return result;
}

JNIEXPORT jfloat JNICALL
Java_com_sisensing_common_utils_JniUtils_calcStd(JNIEnv* env,jclass,jfloatArray bg_array){
    float* array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
    int n = env->GetArrayLength(bg_array);
    float result = calc_std(array,n);
    env->ReleaseFloatArrayElements(bg_array,array,0);
    return result;
}

JNIEXPORT jfloat JNICALL
Java_com_sisensing_common_utils_JniUtils_mg2eA1c(JNIEnv* env,jclass,jfloat mg){

    return mg2eA1c(mg);
}

JNIEXPORT jfloat JNICALL
Java_com_sisensing_common_utils_JniUtils_mg2gmi(JNIEnv* env,jclass,jfloat mg,jint flag){

    return mg2gmi(mg,flag);
}


JNIEXPORT jint JNICALL
Java_com_sisensing_common_utils_JniUtils_calcCv(JNIEnv* env,jclass,jfloat sdbg, jfloat mg,jfloatArray cv){

    float* cvArray = env->GetFloatArrayElements(cv,JNI_FALSE);
    int result = calc_gv(sdbg,mg,cvArray);
    env->ReleaseFloatArrayElements(cv,cvArray,0);
    return result;
}

//JNIEXPORT jfloat JNICALL
//Java_com_sisensing_common_utils_JniUtils_calcMage(JNIEnv* env,jclass,jfloatArray bg_array, jfloat threshold){
//    float *array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
//    int n = env->GetArrayLength(bg_array);
//    float result = calc_mage(array,n,threshold);
//    env->ReleaseFloatArrayElements(bg_array,array,0);
//    return result;
//}

//JNIEXPORT jfloat JNICALL
//Java_com_sisensing_common_utils_JniUtils_calcLage(JNIEnv* env,jclass,jfloatArray bg_array){
//    float *array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
//    int n = env->GetArrayLength(bg_array);
//    float result = calc_lage(array,n);
//    env->ReleaseFloatArrayElements(bg_array,array,0);
//    return result;
//}

//JNIEXPORT jfloat JNICALL
//Java_com_sisensing_common_utils_JniUtils_calcModd(JNIEnv* env,jclass,jfloatArray bg_current_array,jintArray code_current_array,jfloatArray bg_ref_array,jintArray code_ref_array){
//    float *bg_current_array2 = env->GetFloatArrayElements(bg_current_array,JNI_FALSE);
//    int *code_current_array2 = env->GetIntArrayElements(code_current_array,JNI_FALSE);
//    float *bg_ref_array2 = env->GetFloatArrayElements(bg_ref_array,JNI_FALSE);
//    int *code_ref_array2 = env->GetIntArrayElements(code_ref_array,JNI_FALSE);
//    int n_current = env->GetArrayLength(bg_current_array);
//    int n_ref = env->GetArrayLength(bg_ref_array);
//    float result = calc_modd(bg_current_array2,code_current_array2,n_current,bg_ref_array2,code_ref_array2,n_ref);
//    env->ReleaseFloatArrayElements(bg_current_array,bg_current_array2,0);
//    env->ReleaseIntArrayElements(code_current_array,code_current_array2,0);
//    env->ReleaseFloatArrayElements(bg_ref_array,bg_ref_array2,0);
//    env->ReleaseIntArrayElements(code_ref_array,code_ref_array2,0);
//    return result;
//}

JNIEXPORT void JNICALL
Java_com_sisensing_common_utils_JniUtils_calcTir(JNIEnv* env,jclass,jintArray code_array, jfloatArray tir){
    int *array = env->GetIntArrayElements(code_array,JNI_FALSE);
    /*float *tir_array = env->GetFloatArrayElements(tir,JNI_FALSE);
    long *tir_time_array = (long *)env->GetLongArrayElements(tir_time,JNI_FALSE);*/
    float *tir_array = new float[5];
//    jlong *tir_time_array = new jlong[5];
    int n = env->GetArrayLength(code_array);
    calc_tir(array,n,tir_array);
    /*env->SetFloatArrayRegion(tir,0,5,tir_array);
    env->SetLongArrayRegion(tir_time,0,5,tir_time_array);*/
    env->ReleaseIntArrayElements(code_array,array,0);
    env->ReleaseFloatArrayElements(tir,tir_array,0);
//    env->ReleaseLongArrayElements(tir_time,tir_time_array,0);
}

JNIEXPORT void JNICALL
Java_com_sisensing_common_utils_JniUtils_calcTirOld(JNIEnv* env,jclass,jintArray code_array, jfloatArray tir, jlongArray tir_time){
    int *array = env->GetIntArrayElements(code_array,JNI_FALSE);
    /*float *tir_array = env->GetFloatArrayElements(tir,JNI_FALSE);
    long *tir_time_array = (long *)env->GetLongArrayElements(tir_time,JNI_FALSE);*/
    float *tir_array = new float[5];
    jlong *tir_time_array = new jlong[5];
    int n = env->GetArrayLength(code_array);
    calc_tir_old(array,n,tir_array,(long long*)tir_time_array);
    /*env->SetFloatArrayRegion(tir,0,5,tir_array);
    env->SetLongArrayRegion(tir_time,0,5,tir_time_array);*/
    env->ReleaseIntArrayElements(code_array,array,0);
    env->ReleaseFloatArrayElements(tir,tir_array,0);
    env->ReleaseLongArrayElements(tir_time,tir_time_array,0);
}

JNIEXPORT jint JNICALL
Java_com_sisensing_common_utils_JniUtils_calcEvent(JNIEnv* env,jclass,jfloatArray bg_array,jintArray tir_code_array, jlongArray correct_datetime_array,jintArray tir_type_array, jlongArray start_array,
                                                   jlongArray end_array,jfloatArray extre_values_array,jintArray event_num_array){
    float *bg = env->GetFloatArrayElements(bg_array,JNI_FALSE);
    float *extre_values = env->GetFloatArrayElements(extre_values_array,JNI_FALSE);
    int *tir_code = env->GetIntArrayElements(tir_code_array,JNI_FALSE);
    int *event_num = env->GetIntArrayElements(event_num_array,JNI_FALSE);
    jlong *correct_datetime = env->GetLongArrayElements(correct_datetime_array,JNI_FALSE);
    int n = env->GetArrayLength(tir_code_array);
    int tir_type_n = env->GetArrayLength(tir_type_array);
    int start_n = env->GetArrayLength(start_array);
    int end_n = env->GetArrayLength(end_array);
    int *tir_type = new int[tir_type_n];
    jlong *start = new jlong[start_n];
    jlong *end = new jlong[end_n];
    int result = calc_event(bg,tir_code,(long long*)correct_datetime,n,tir_type,(long long*)start,(long long*)end,extre_values,event_num);
    /*env->SetIntArrayRegion(tir_type_array,0,tir_type_n,tir_type);
    env->SetIntArrayRegion(event_num_array,0,event_num_n,event_num);
    env->SetLongArrayRegion(start_array,0,start_n,start);
    env->SetLongArrayRegion(end_array,0,end_n,end);*/
    env->ReleaseIntArrayElements(tir_code_array,tir_code,0);
    env->ReleaseIntArrayElements(event_num_array,event_num,0);
    env->ReleaseLongArrayElements(correct_datetime_array,correct_datetime,0);
    env->ReleaseIntArrayElements(tir_type_array,tir_type,0);
    env->ReleaseLongArrayElements(start_array,start,0);
    env->ReleaseLongArrayElements(end_array,end,0);
    env->ReleaseFloatArrayElements(bg_array,bg,0);
    env->ReleaseFloatArrayElements(extre_values_array,extre_values,0);
    return result;
}
//JNIEXPORT jint JNICALL
//Java_com_sisensing_common_utils_JniUtils_calceventnight(JNIEnv* env,jclass,jfloatArray bg_array,jintArray tir_code_array, jlongArray correct_datetime_array,jintArray tir_type_array, jlongArray start_array,
//                                                   jlongArray end_array,jfloatArray extre_values_array,jintArray event_num_array){
//    float *bg = env->GetFloatArrayElements(bg_array,JNI_FALSE);
//    float *extre_values = env->GetFloatArrayElements(extre_values_array,JNI_FALSE);
//    int *tir_code = env->GetIntArrayElements(tir_code_array,JNI_FALSE);
//    int *event_num = env->GetIntArrayElements(event_num_array,JNI_FALSE);
//    jlong *correct_datetime = env->GetLongArrayElements(correct_datetime_array,JNI_FALSE);
//    int n = env->GetArrayLength(tir_code_array);
//    int tir_type_n = env->GetArrayLength(tir_type_array);
//    int start_n = env->GetArrayLength(start_array);
//    int end_n = env->GetArrayLength(end_array);
//    int *tir_type = new int[tir_type_n];
//    jlong *start = new jlong[start_n];
//    jlong *end = new jlong[end_n];
//    int result = calc_event_night(bg,tir_code,(long long*)correct_datetime,n,tir_type,(long long*)start,(long long*)end,extre_values,event_num);
//    /*env->SetIntArrayRegion(tir_type_array,0,tir_type_n,tir_type);
//    env->SetIntArrayRegion(event_num_array,0,event_num_n,event_num);
//    env->SetLongArrayRegion(start_array,0,start_n,start);
//    env->SetLongArrayRegion(end_array,0,end_n,end);*/
//    env->ReleaseIntArrayElements(tir_code_array,tir_code,0);
//    env->ReleaseIntArrayElements(event_num_array,event_num,0);
//    env->ReleaseLongArrayElements(correct_datetime_array,correct_datetime,0);
//    env->ReleaseIntArrayElements(tir_type_array,tir_type,0);
//    env->ReleaseLongArrayElements(start_array,start,0);
//    env->ReleaseLongArrayElements(end_array,end,0);
//    env->ReleaseFloatArrayElements(bg_array,bg,0);
//    env->ReleaseFloatArrayElements(extre_values_array,extre_values,0);
//    return result;
//}

//JNIEXPORT jint JNICALL
//Java_com_sisensing_common_utils_JniUtils_calcLbgiHbgi(JNIEnv* env,jclass,jfloatArray bg_array,jfloatArray lbgi_array,jfloatArray hbgi_array){
//    float *array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
//    float *lbgi = env->GetFloatArrayElements(lbgi_array,JNI_FALSE);
//    float *hbgi = env->GetFloatArrayElements(hbgi_array,JNI_FALSE);
//    int n = env->GetArrayLength(bg_array);
//    int result = calc_lbgi_hbgi(array,n,lbgi,hbgi);
//    //env->SetFloatArrayRegion(lbgi_array, 0, 1, lbgi);
//    //env->SetFloatArrayRegion(hbgi_array, 0, 1, hbgi);
//    env->ReleaseFloatArrayElements(bg_array,array,0);
//    env->ReleaseFloatArrayElements(lbgi_array,lbgi,0);
//    env->ReleaseFloatArrayElements(hbgi_array,hbgi,0);
//    return result;
//}

JNIEXPORT void JNICALL
Java_com_sisensing_common_utils_JniUtils_genAgp(JNIEnv* env,jclass,jfloatArray bg_array,jintArray code_array,jfloatArray per5_array,jfloatArray per25_array,jfloatArray per50_array,jfloatArray per75_array,jfloatArray per95_array){

    float *array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
    int *codearray = env->GetIntArrayElements(code_array,JNI_FALSE);
    int n = env->GetArrayLength(bg_array);
    float *per5 = new float[24];
    float *per25 = new float[24];
    float *per50 = new float[24];
    float *per75 = new float[24];
    float *per95 = new float[24];
    gen_agp(array,codearray,n,per5,per25,per50,per75,per95);
    env->SetFloatArrayRegion(per5_array,0,24,per5);
    env->SetFloatArrayRegion(per25_array,0,24,per25);
    env->SetFloatArrayRegion(per50_array,0,24,per50);
    env->SetFloatArrayRegion(per75_array,0,24,per75);
    env->SetFloatArrayRegion(per95_array,0,24,per95);
}

JNIEXPORT void JNICALL
Java_com_sisensing_common_utils_JniUtils_genAgpOld(JNIEnv* env,jclass,jfloatArray bg_array,jintArray code_array,jfloatArray per5_array,jfloatArray per25_array,jfloatArray per50_array,jfloatArray per75_array,jfloatArray per95_array){

    float *array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
    int *codearray = env->GetIntArrayElements(code_array,JNI_FALSE);
    int n = env->GetArrayLength(bg_array);
    float *per5 = new float[288];
    float *per25 = new float[288];
    float *per50 = new float[288];
    float *per75 = new float[288];
    float *per95 = new float[288];
    gen_agp_old(array,codearray,n,per5,per25,per50,per75,per95);
    env->SetFloatArrayRegion(per5_array,0,288,per5);
    env->SetFloatArrayRegion(per25_array,0,288,per25);
    env->SetFloatArrayRegion(per50_array,0,288,per50);
    env->SetFloatArrayRegion(per75_array,0,288,per75);
    env->SetFloatArrayRegion(per95_array,0,288,per95);
}

JNIEXPORT jfloatArray JNICALL
Java_com_sisensing_common_utils_JniUtils_agpFilter(JNIEnv* env,jclass,jfloatArray array, jfloatArray smoothArray){
    float *bg_array = env->GetFloatArrayElements(array,JNI_FALSE);
    float *smooth_array = env->GetFloatArrayElements(smoothArray,JNI_FALSE);
    int n = env->GetArrayLength(array);
    agp_smooth(bg_array,n,smooth_array);
    //jfloatArray resultArray = env->NewFloatArray(n);
    //env->SetFloatArrayRegion(resultArray,0,n,result);
    env->ReleaseFloatArrayElements(array,bg_array,0);
    return array;
}

JNIEXPORT jfloatArray JNICALL
Java_com_sisensing_common_utils_JniUtils_agpFilterOld(JNIEnv* env,jclass,jfloatArray array, jfloatArray smoothArray){
    float *bg_array = env->GetFloatArrayElements(array,JNI_FALSE);
    int n = env->GetArrayLength(array);
    agp_filter_old(bg_array,n);
    //jfloatArray resultArray = env->NewFloatArray(n);
    //env->SetFloatArrayRegion(resultArray,0,n,result);
    env->ReleaseFloatArrayElements(array,bg_array,0);
    return array;
}

//JNIEXPORT jint JNICALL
//Java_com_sisensing_common_utils_JniUtils_groupByDay(JNIEnv* env,jclass,jfloatArray bg_array,jintArray bg_code_array,jlongArray bg_datetime_array,jobjectArray bg_group_array, jobjectArray code_group_array, jobjectArray datetime_group_array, jintArray jday_len_array,jintArray dur_days_array){
//    float *array = env->GetFloatArrayElements(bg_array,JNI_FALSE);
//    int *code_array = env->GetIntArrayElements(bg_code_array,JNI_FALSE);
//    int *dur_days = env->GetIntArrayElements(dur_days_array,JNI_FALSE);
//    jlong *datetime_array = env->GetLongArrayElements(bg_datetime_array,JNI_FALSE);
//    int n = env->GetArrayLength(bg_array);
//
//   /* int size = env->GetArrayLength(bg_group_array);//获得行数
//    jarray myarray = env->GetObjectArrayElement(bg_group_array, 0);
//    int col =env->GetArrayLength(myarray); //获得列数*/
//
//    //LOGE("JniUtils_groupByDay size=%d,col=%",size,col);
//    //按天分组
//    float **group_array = new float*[15]; //血糖分组数据
//    int **code_group_array2 = new int*[15]; //时间戳编码分组数据
//    jlong **datetime_group_array2 = new jlong*[15];//校正后时间戳 分组数据
//    int *day_len_array = new int[15]; //每天数据长度
//    for (int i = 0; i < 15; i++)
//    {
//        group_array[i] = new float[288];
//        code_group_array2[i] = new int[288];
//        datetime_group_array2[i] = new jlong[288];
//    }
//
//    jint status = group_by_day(array,n,code_array,(long long*)datetime_array,group_array,code_group_array2,(long long**)datetime_group_array2,day_len_array,dur_days);
//
//    if(status!=200){
//        return status;
//    }
//    for(int i=0;i<dur_days[0];i++){
//        int day_len = day_len_array[i];//第day天的数据长度
//        float *bg_day_array = group_array[i]; //第day天的血糖数据
//        int *code_day_array = code_group_array2[i];//第day天的时间戳编码数据
//        jlong *datetime_day_array = datetime_group_array2[i];//第day天的校正后时间戳
//
//        jfloatArray jbg_group_array = (jfloatArray)env->GetObjectArrayElement(bg_group_array,i);
//        jintArray jcode_group_array = (jintArray)env->GetObjectArrayElement(code_group_array,i);
//        jlongArray jdatetime_group_array = (jlongArray)env->GetObjectArrayElement(datetime_group_array,i);
//
//        env->SetFloatArrayRegion(jbg_group_array,0,day_len,bg_day_array);
//        env->SetIntArrayRegion(jcode_group_array,0,day_len,code_day_array);
//        env->SetLongArrayRegion(jdatetime_group_array,0,day_len,datetime_day_array);
//    }
//    env->SetIntArrayRegion(jday_len_array,0,15,day_len_array);
//    env->ReleaseIntArrayElements(dur_days_array,dur_days,0);
//    return status;
//}

}

/*JNIEXPORT jint JNICALL
Java_com_sisensing_common_utils_JniUtils_genTirCode(JNIEnv *, jclass,jfloat glucose, jint type, jfloat low, jfloat high){
    int result = gen_tir_code(glucose,type,low,high);
    return result;
}*/


