#include <iostream>
#include <fstream>
#include "stdlib.h"
#include <vector>
#include <math.h>
#include <algorithm>

#include <string>
#include <numeric>
#include <time.h>

const char global_algorithm_version[] = "V2.0.1";
const int DAILY_LEN_MAX = 576;//每日最大数据，288*2
const int DAYS_LEN_MAX = 91;//传感器最大天数， 90+1
const int HOUR_LEN_MAX = 1440;//每小时传感器探头值数量最大值，12*（90+30）
const int AGP_POINT = 24;//APG的点数，


char *get_target_vesion(void);


/*
@brief: 计算时间戳编码。
	编码逻辑：将一天24小时按1小时为间隔编码为0~23；00:00~00:59为0；01:00~01:59为1，以此类推。
@param:
	input:
		origin_tstp：输入时间数组，long long*
		N：数组长度
	output:
		code_array:时间戳编码值数组,int*
@ret: 状态，200：运行正常；500：运行异常
*/
int gen_timecode(long long *origin_tstp, int N, int *code_array);


/*
 @brief: 根据不同的糖尿病类型，将血糖值分类为极低(-2)、低(-1)、正常(0)、高(1)、极高(2)，我称之为血糖水平编码。
 @param:
		input:
			glucose：血糖值,float
			type：糖尿病类型。int,0：正常；1：1、2型糖尿病；2：妊娠糖；3：老年糖；4：自定义；默认正常。
			threshold:自定义值，包含4个值，分别为：极低阈值，低阈值，高阈值，极高阈值。仅在type为4时启用，float*
				如果自定义不需要极低阈值或极高阈值，则只需将对应阈值设置为超出血糖值的范围即可。
				例如，无需极低血糖阈值，则需threshold[0]=2.1(按照设定，血糖值不会低于2.2)；
				同理无需极高血糖阈值，则需threshold[0]=25.1.
 @ret: tir code,值为{-2，-1,0,1,2},int
*/
int gen_tir_code(float glucose, int type, float *threshold);

/*
@brief: 在gen_tir_code函数的基础上处理数组
@param:
	input:
		glucose：血糖值,float*
		type：糖尿病类型。0：正常；1：1、2型糖尿病；2：妊娠糖；3：老年糖；4：自定义。
		threshold：自定义值，包含4个值，分别为：极低阈值，低阈值，高阈值，极高阈值。仅在type为4时启用，float*
			如果自定义不需要极低阈值或极高阈值，则只需将对应阈值设置为超出血糖值的范围即可。
			例如，无需极低血糖阈值，则需threshold[0]=2.1(按照设定，血糖值不会低于2.2)；
			同理无需极高血糖阈值，则需threshold[0]=25.1.
	output:
		tir_code_array:编码后的数组，int *
@ret: 状态，200：运行正常；500：运行异常
*/
int gen_tir_code_array(float *glucose, int N, int *tir_code_array, int type, float *threshold);

/*
@brief: 计算均值。
@param:
	input:
		bg_array：血糖值数组，float*
		N:数组长度，int
@ret: 血糖均值,float
*/
float calc_mean(float *bg_array, int N);

/*
@brief: 计算标准差。
@param:
	input:
		bg_array：血糖值数组，float*
		N:数组长度，int
@ret: 血糖标准差，float
*/
float calc_std(float *bg, int N);

/*
@brief: 平均血糖值转换为葡萄糖管理指标。
@param:
	input:
		mg：平均血糖,float
		flag: 血糖单位，0为mg/dL,1为mmol/L，默认为1。int
@ret: 葡萄糖管理指标值,float
*/
float mg2gmi(float mg, int flag = 1);


/*
@brief: 计算变异系数Glucose Variability。
@param:
	input:
		sdbg：血糖标准差,float
		mg：平均血糖,float
	output:cv,float
@ret: 状态，200：运行正常；500：运行异常
*/
int calc_gv(float sdbg, float mg, float *cv);


/*
@brief: 数据点计数转换为时间长度。
@param:
	input:
		n：数据点个数,int
@ret: 时间段,long long
*/

long long num2dur(int n);


/*
@brief: 计算tir。
@param:
	input:
		code_array：血糖水平编码数组，int*
		N：数组长度，int
	output:
		tir:tir百分比，float*,tir[0]对应极低，tir[1]对应低，tir[2]对应正常，tir[3]对应高，tir[4]对应极高，
		tir_time:tir时间长度,long long*,同tir
@ret: 状态，200：运行正常；500：运行异常
*/
int calc_tir(int *code_array, int N, float *tir);


/*
@brief: 升序排序，直接对原数组排序
@param:
	input:
		bg_array：血糖数组，float*
		N：数组长度,int
	output:
		in_array：直接对原数组排序，float*
@ret: void
*/
void sort_value(float *in_array, int N);

/*
@brief: 计算分位数
@param:
	input:
		bg_t_array：连续时间内每天t时刻的血糖数组，float*
		p：分位点*100，如果是25%分位数，则p=25,int
@ret: 分位数，float
*/
float percentile(float *bg_t_array, int N, int p);

/*
@brief: 将数据按照时间戳分组
@param:
	input:
		bg_array：血糖数组，float*
		code_array：时间戳编码数组，int*
		N:数组长度,int
	output：
		bg_array_group：分组结果，为288*天数，float**
		code_len_array：每个时间点（编码值）对应的数据个数，int*，长度为288
@ret: 状态，200：运行正常；500：运行异常
*/
int
bg_group_time(float *bg_array, int *code_array, int N, float **bg_array_group, int *code_len_array);

/*
@brief: 根据分好的组，计算agp的5条曲线
@param:
	input:
		bg_array：血糖数组，float*
		code_array：时间戳编码数组，int*
		N:数组长度，int
	output：
		per5:5%血糖数组，float*
		per25:25%血糖数组，float*
		per50:50%血糖数组，float*
		per75:75%血糖数组，float*
		per95:95%血糖数组，float*
@ret: 状态，200：运行正常；500：运行异常
*/
int gen_agp(float *bg_array, int *code_array, int N, float *per5_array, float *per25_array,
            float *per50_array, float *per75_array, float *per95_array);


/*
@brief: AGP平滑滤波，采用1-4-1加权
@param:
	input:
		bg_array：输入的血糖值数组（分位数数组），float*
		N:数组长度
	output:
		bg_smooth_array：平滑后的血糖数组，float*
@ret: void
*/
void agp_smooth(float *bg_array, int N, float *bg_smooth_array);





/**旧的,为了跑通,权宜之计*/
/*
@brief: 平均血糖值转换为预估糖化血红蛋白。
@param:
	input:
		mg：平均血糖,float
@ret: 预估糖化血红蛋白值,float
*/
float mg2eA1c(float mg);

/*
@brief: 计算血糖事件，两个测量点时间超过（等于）即15分钟，记为1次血糖事件，持续时间为结束时间-开始时间
@param:
	input:
		bg_array:血糖数组，float*
		tir_code_array：血糖水平数组，int*
		correct_datetime_array:时间戳数组，long long*
		N:数组长度,int
	output:
		tir_type:血糖水平数组，int*
		start:开始时间,long long*
		end:结束时间,long long*
		extre_values:极值，float*
		event_num:异常血糖事件计数，int*，为1*1指针。
@ret: 状态，200：运行正常；500：运行异常
*/
int calc_event(float *bg_array, int *tir_code_array, long long *correct_datetime_array, int N,
               int *tir_type_array, long long *start_array, long long *end_array,
               float *extre_values, int *event_num);


/*
@brief: 根据分好的组，计算agp的5条曲线
@param:
	input:
		bg_array：血糖数组，float*
		code_array：时间戳编码数组，int*
		N:数组长度，int
	output：
		per5:5%血糖数组，float*
		per25:25%血糖数组，float*
		per50:50%血糖数组，float*
		per75:75%血糖数组，float*
		per95:95%血糖数组，float*
@ret: 状态，200：运行正常；500：运行异常
*/
int gen_agp_old(float *bg_array, int *code_array, int N, float *per5_array, float *per25_array,
            float *per50_array, float *per75_array, float *per95_array);

/*
@brief: 对agp曲线滤波,正反重复滤波14次
@param:
	input:
		bg_array：输入的血糖值数组（分位数数组），float*
		N:数组长度
	output:
		bg_array：对指针操作，将原数组作为输出，float*
@ret: void
*/
void agp_filter_old(float* bg_array, int N);

/*
@brief: 数据点计数转换为时间长度。
@param:
	input:
		n：数据点个数,int
@ret: 时间段,long long
*/

long long num2dur(int n);

/*
@brief: 计算tir。
@param:
	input:
		code_array：血糖水平编码数组，int*
		N：数组长度，int
	output:
		tir:tir百分比，float*,tir[0]对应极低，tir[1]对应低，tir[2]对应正常，tir[3]对应高，tir[4]对应极高，
		tir_time:tir时间长度,long long*,同tir
@ret: 状态，200：运行正常；500：运行异常
*/
int calc_tir_old(int* code_array, int N, float* tir, long long* tir_time);