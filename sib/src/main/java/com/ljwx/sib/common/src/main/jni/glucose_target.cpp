#include "glucose_target.h"

using namespace std;

char *get_target_vesion() {
    return (char *) global_algorithm_version;
}


int gen_timecode(long long *origin_tstp, int N, int *code_array) {
    int stats_code = 200;
    try {
        for (int i = 0; i < N; i++) {
            code_array[i] = 0;
        }

        for (int i = 0; i < N; i++) {
            long long temp_tstp = origin_tstp[i];
            temp_tstp = temp_tstp - temp_tstp % 60;//删除秒

            time_t timestamp1 = (time_t) temp_tstp;
            struct tm time1;
            //localtime_s(&time1, &timestamp1);//for windows
            localtime_r(&timestamp1, &time1);//for linux

            int hour = time1.tm_hour;
            code_array[i] = hour;
        }
    }
    catch (const std::exception &) {
        stats_code = 500;
    }
    return stats_code;
}


int gen_tir_code(float glucose, int type, float *threshold) {
    //	glucose = round(glucose * 10) / 10.0;
    int code = 0;
    switch (type) {
        case 0: {
            //			if (glucose <= 3.9)
            if (glucose - 3.9 < 0.0005)
                code = -1;
                //			else if (glucose < 7.8)
            else if (glucose - 7.8 < -0.0005)
                code = 0;
            else
                code = 1;
            break;
        }
        case 1: {
            //			if (glucose < 3.0)
            if (glucose - 3.0 < -0.005)
                code = -2;
                //			else if (glucose < 3.9)
            else if (glucose - 3.9 < -0.005)
                code = -1;
                //			else if (glucose <= 10.0)
            else if (glucose - 10.0 < 0.005)
                code = 0;
                //			else if (glucose <= 13.9)
            else if (glucose - 13.9 < 0.005)
                code = 1;
            else
                code = 2;
            break;
        }
        case 2: {
            //			if (glucose < 3.0)
            if (glucose - 3.0 < -0.005)
                code = -2;
                //			else if (glucose < 3.5)
            else if (glucose - 3.5 < -0.005)
                code = -1;
                //			else if (glucose <= 7.8)
            else if (glucose - 7.8 < 0.005)
                code = 0;
            else
                code = 1;
            break;
        }
        case 3: {
            //			if (glucose < 3.9)
            if (glucose - 3.9 < -0.005)
                code = -1;
                //			else if (glucose <= 10.0)
            else if (glucose - 10.0 < 0.005)
                code = 0;
                //			else if (glucose <= 13.9)
            else if (glucose - 13.9 < 0.005)
                code = 1;
            else
                code = 2;
            break;
        }
        case 4: {
            if (glucose - threshold[0] < 0.005)
                code = -2;
            else if (glucose - threshold[1] < -0.005)
                code = -1;
            else if (glucose - threshold[2] < -0.005)
                code = 0;
            else if (glucose - threshold[3] < -0.005)
                code = 1;
            else
                code = 2;
            break;
        }
        default: {
            //			if (glucose <= 3.9)
            if (glucose - 3.9 < 0.0005)
                code = -1;
                //			else if (glucose < 7.8)
            else if (glucose - 7.8 < -0.0005)
                code = 0;
            else
                code = 1;
            break;
        }
    }
    return code;
}


int gen_tir_code_array(float *glucose, int N, int *tir_code_array, int type, float *threshold) {
    int stats_code = 200;
    //	int N = length(glucose);
    if (N <= 0) {
        stats_code = 500;
    }
    try {
        for (int i = 0; i < N; i++) {
            tir_code_array[i] = 0;
        }
        for (int i = 0; i < N; i++) {
            tir_code_array[i] = gen_tir_code(glucose[i], type, threshold);
        }
    }
    catch (const std::exception &) {
        stats_code = 500;
    }
    return stats_code;
}


float calc_mean(float *bg_array, int N) {
    if (N <= 0) {
        return 0;
    }
    float mean_value = 0;
    float sum_value = 0;
    //	int N = length(bg);
    for (int i = 0; i < N; i++) {
        sum_value += bg_array[i];
    }
    if (sum_value != 0)
        mean_value = sum_value / N;
    return mean_value;
}


float calc_std(float *bg, int N) {
    if (N <= 0) {
        return 0;
    }
    float mean_value = calc_mean(bg, N);
    float var_sum = 0;
    float std = 0;
    for (int i = 0; i < N; i++) {
        var_sum += (bg[i] - mean_value) * (bg[i] - mean_value);
    }
    if (var_sum != 0)
        std = sqrt(var_sum / N);
    return std;

}


float mg2gmi(float mg, int flag) {
    float gmi = 0;
    if (flag == 0)
        gmi = 3.31 + 0.02392 * mg;
    else
        gmi = 3.31 + 0.43099 * mg;
    return gmi;
}


int calc_gv(float sdbg, float mg, float *cv) {
    cv[0] = 0;
    int stats_code = 200;
    try {
        if (mg == 0) {
            stats_code = 500;
        }
        cv[0] = sdbg / mg;
    }
    catch (const std::exception &) {
        stats_code = 500;
    }
    return stats_code;
}


float
calc_modd(float *bg_current_array, int *code_current_array, int N_current, float *bg_ref_array,
          int *code_ref_array, int N_ref) {
    vector<float> current = {};
    vector<float> ref = {};
    for (int i = 0; i < N_current; i++) {
        int code = code_current_array[i];
        float glucose = bg_current_array[i];
        for (int j = 0; j < N_ref; j++) {
            int code_ref = code_ref_array[j];
            float glucose_ref = bg_ref_array[j];
            if (code == code_ref) {

                current.push_back(glucose);
                ref.push_back(glucose_ref);
                break;
            }
        }
    }
    float diff_sum = 0;
    for (int i = 0; i < current.size(); i++) {
        diff_sum += abs(current[i] - ref[i]);
    }
    float modd = 0;
    if (current.size() > 0) {
        modd = diff_sum / current.size();
    }
    current.clear();
    ref.clear();
    return modd;
}


int calc_max_index(float *bg_array, int N) {
    if (N <= 0) {
        return -1;
    }
    float max = bg_array[0];
    int idx = 0;

    for (int i = 1; i < N; i++) {
        float now = bg_array[i];
        if (now > max) {
            max = now;
            idx = i;
        }
    }
    return idx;
}

long long num2dur(int n)
{
    return n * 300;
}

int calc_tir_old(int* code_array, int N, float* tir, long long* tir_time)
{
    int stats_code = 200;
    vector<int> tir_counts = { 0,0,0,0,0 };
    try
    {
        if (N <= 0)
        {
            stats_code = 500;
        }

        for (int i = 0; i < N; i++)
        {
            tir_counts[code_array[i] + 2]++;
        }
        tir[0] = float(tir_counts[0]) / N;
        tir[1] = float(tir_counts[1]) / N;
        tir[2] = float(tir_counts[2]) / N;
        tir[3] = float(tir_counts[3]) / N;
        tir[4] = float(tir_counts[4]) / N;

        int max_idx = calc_max_index(tir, 5);
        tir[max_idx] = 1.00;
        for (int i = 0; i < 5; i++)
        {
            if(i!=max_idx)
            {
                tir[i] = round(tir[i]*1000)/1000;
                tir[max_idx] = tir[max_idx] - tir[i];
            }
        }

        tir_time[0] = num2dur(tir_counts[0]);
        tir_time[1] = num2dur(tir_counts[1]);
        tir_time[2] = num2dur(tir_counts[2]);
        tir_time[3] = num2dur(tir_counts[3]);
        tir_time[4] = num2dur(tir_counts[4]);
    }
    catch (const std::exception&)
    {
        stats_code = 500;
    }
    tir_counts.clear();
    return stats_code;
}

int calc_tir(int *code_array, int N, float *tir) {
    int stats_code = 200;
    vector<int> tir_counts = {0, 0, 0, 0, 0};
    try {
        if (N <= 0) {
            stats_code = 500;
        }

        for (int i = 0; i < N; i++) {
            tir_counts[code_array[i] + 2]++;
        }
        tir[0] = float(tir_counts[0]) / N;
        tir[1] = float(tir_counts[1]) / N;
        tir[2] = float(tir_counts[2]) / N;
        tir[3] = float(tir_counts[3]) / N;
        tir[4] = float(tir_counts[4]) / N;

        int max_idx = calc_max_index(tir, 5);
        tir[max_idx] = 1.00;
        for (int i = 0; i < 5; i++) {
            if (i != max_idx) {
                tir[i] = round(tir[i] * 1000) / 1000;
                tir[max_idx] = tir[max_idx] - tir[i];
            }
        }
    }
    catch (const std::exception &) {
        stats_code = 500;
    }
    tir_counts.clear();
    return stats_code;
}


int calc_lbgi_hbgi(float *bg_array, int N, float *lbgi, float *hbgi) {
    lbgi[0] = 0;
    hbgi[0] = 0;
    int stats_code = 200;
    vector<float> fbg = {};
    vector<float> rbg = {};
    vector<float> rlbg = {};
    vector<float> rhbg = {};
    try {

        if (N <= 0) {
            stats_code = 500;
        }

        float rlbg_sum = 0;
        float rhbg_sum = 0;
        for (int i = 0; i < N; i++) {
            float now_bg = bg_array[i];
            float now_fbg = 1.794 * (pow(log(now_bg), 1.026) - 1.861);
            float now_rbg = 10 * now_fbg * now_fbg;
            fbg.push_back(now_fbg);
            rbg.push_back(now_rbg);
            if (now_fbg < 0) {
                rlbg.push_back(now_rbg);
                rlbg_sum += now_rbg;
            }

            if (now_fbg > 0) {
                rhbg.push_back(now_rbg);
                rhbg_sum += now_rbg;
            }

        }
        if (rlbg.size() > 0) {
            lbgi[0] = rlbg_sum / rlbg.size();
        }
        if (rhbg.size() > 0) {
            hbgi[0] = rhbg_sum / rhbg.size();
        }

    }
    catch (const std::exception &) {
        stats_code = 500;
    }
    //vector<float>(fbg).swap(fbg);
    //vector<float>(rbg).swap(rbg);
    //vector<float>(rlbg).swap(rlbg);
    //vector<float>(rhbg).swap(rhbg);
    fbg.clear();
    rbg.clear();
    rlbg.clear();
    rhbg.clear();
    return stats_code;
}


void sort_value(float *in_array, int N) {
    for (int i = 0; i < N - 1; ++i) {
        int f = 1;
        for (int j = 0; j < N - i - 1; ++j) {
            if (in_array[j] > in_array[j + 1]) {
                float t = in_array[j];
                in_array[j] = in_array[j + 1];
                in_array[j + 1] = t;
                f = 0;
            }
        }
        if (f == 1)
            break;
    }
}


float percentile(float *bg_t_array, int N, int p) {
    if (N <= 0) {
        return 0;
    }
    sort_value(bg_t_array, N);
    float p1 = p / 100.0;
    float idx = p1 * (N - 1);
    float idx_below_d = floor(idx);
    int idx_below = (int) (idx_below_d);
    int idx_above = idx_below + 1;
    float weights_above = idx - idx_below;
    float weights_below = 1.0 - weights_above;
    float v_below = bg_t_array[idx_below];
    float v_above = bg_t_array[idx_above];
    float value = v_below * weights_below + v_above * weights_above;
    return value;
}


int bg_group_time(float *bg_array, int *code_array, int N, float **bg_array_group,
                  int *code_len_array) {
    int stats_code = 200;
    try {
        if (N <= 0) {
            stats_code = 500;
        }
        for (int i = 0; i < AGP_POINT; i++)
            code_len_array[i] = 0;
        for (int i = 0; i < N; i++) {
            int code = code_array[i];
            int col = code_len_array[code];
            if (code_len_array[code] < HOUR_LEN_MAX) {
                bg_array_group[code][col] = bg_array[i];
                code_len_array[code]++;
            }
            //code_len_array[code]++;
        }
        for (int i = 0; i < AGP_POINT; i++) {
            if ((code_len_array[i] > HOUR_LEN_MAX) | (code_len_array[i] <= 0)) {
                stats_code = 500;
            }
        }
    }
    catch (const std::exception &) {
        stats_code = 500;
    }
    return stats_code;
}


int gen_agp(float *bg_array, int *code_array, int N, float *per5_array, float *per25_array,
            float *per50_array, float *per75_array, float *per95_array) {
    int stats_code = 200;
    //int daily_len_max = 576;//288*2
    //int days_len_max = 28;//14*2
    float **group_array = new float *[AGP_POINT];
    for (int i = 0; i < AGP_POINT; i++) {
        group_array[i] = new float[HOUR_LEN_MAX];
    }
    int *code_len_array = new int[AGP_POINT];

    try {
        stats_code = bg_group_time(bg_array, code_array, N, group_array, code_len_array);
        if (stats_code == 200) {
            for (int i = 0; i < AGP_POINT; i++) {
                /*float* temp_group = group_array[i];*/
                int code_N = code_len_array[i];
                per5_array[i] = percentile(group_array[i], code_N, 5);
                per25_array[i] = percentile(group_array[i], code_N, 25);
                per50_array[i] = percentile(group_array[i], code_N, 50);
                per75_array[i] = percentile(group_array[i], code_N, 75);
                per95_array[i] = percentile(group_array[i], code_N, 95);
                //delete[] temp_group;
            }
        }
        for (int i = 0; i < AGP_POINT; i++) {
            delete[] group_array[i];
        }
        delete[] group_array;
        delete[] code_len_array;

    }
    catch (const std::exception &) {
        stats_code = 500;
    }

    return stats_code;
}


void agp_smooth(float *bg_array, int N, float *bg_smooth_array) {
    for (int i = 0; i < N; i++) {
        int idx_previous = i - 1;
        if (idx_previous == -1)
            idx_previous = N - 1;
        int idx_next = i + 1;
        if (idx_next == N)
            idx_next = 0;
        float weight = 4.0;
        float weight_previous = 1.0;
        float weight_next = 1.0;
        if (bg_array[idx_previous] <= 0.01)
            weight_previous = 0.0;
        if (bg_array[idx_next] <= 0.01)
            weight_next = 0.0;
        if (bg_array[i] > 0.01) {
            bg_smooth_array[i] = (bg_array[idx_previous] * weight_previous + bg_array[i] * weight +
                                  bg_array[idx_next] * weight_next) /
                                 (weight + weight_previous + weight_next);
        }
    }
}


/**权宜之计*/
float mg2eA1c(float mg) {
    float eA1c = (mg + 0.582) / 1.198;
    return eA1c;
}

int calc_event(float *bg_array, int *tir_code_array, long long *correct_datetime_array, int N,
               int *tir_type_array, long long *start_array, long long *end_array,
               float *extre_values, int *event_num) {
    int stats_code = 200;
    event_num[0] = 0;
    try {
        if (N <= 0) {
            stats_code = 500;
        }
        if (N != 0) {
            long long start_time = correct_datetime_array[0];
            long long end_time = correct_datetime_array[0];
            float maximum = bg_array[0];
            long long dur = 0;

            //高血糖事件
            int count_p = 0;
            int start_flag = 0;
            for (int i = 0; i < N - 2; i++) {
                count_p = int(tir_code_array[i] > 0) + int(tir_code_array[i + 1] > 0) +
                          int(tir_code_array[i + 2] > 0);
                if (start_flag == 0) {
                    if (count_p == 3) {
                        tir_type_array[event_num[0]] = 1;
                        start_array[event_num[0]] = correct_datetime_array[i];
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = max(max(bg_array[i], bg_array[i + 1]),
                                                         max(bg_array[i + 2], bg_array[i + 1]));
                        start_flag = 1;
                    }
                } else {
                    if (count_p == 0) {
                        end_array[event_num[0]] = correct_datetime_array[i - 1];
                        event_num[0]++;
                        start_flag = 0;
                    } else {
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = max(extre_values[event_num[0]],
                                                         bg_array[i + 2]);
                        //cout << tir_type_array[event_num[0]] << "\t" << correct_datetime_array[i + 2] << "\t" << bg_array[i + 2] << endl;
                    }
                }
            }
            if (start_flag == 1) {
                //end_array[event_num[0]] = correct_datetime_array[N - 1];
                event_num[0]++;
                start_flag == 0;
            }

            //低血糖事件
            count_p = 0;
            start_flag = 0;
            for (int i = 0; i < N - 2; i++) {
                count_p = int(tir_code_array[i] < 0) + int(tir_code_array[i + 1] < 0) +
                          int(tir_code_array[i + 2] < 0);
                if (start_flag == 0) {
                    if (count_p == 3) {
                        tir_type_array[event_num[0]] = -1;
                        start_array[event_num[0]] = correct_datetime_array[i];
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = min(min(bg_array[i], bg_array[i + 1]),
                                                         min(bg_array[i + 2], bg_array[i + 1]));
                        start_flag = 1;
                    }
                } else {
                    if (count_p == 0) {
                        end_array[event_num[0]] = correct_datetime_array[i - 1];
                        event_num[0]++;
                        start_flag = 0;
                    } else {
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = min(extre_values[event_num[0]],
                                                         bg_array[i + 2]);
                    }
                }
            }
            if (start_flag == 1) {
                //end_array[event_num[0]] = correct_datetime_array[N - 1];
                event_num[0]++;
                start_flag == 0;
            }

            //极高血糖事件
            count_p = 0;
            start_flag = 0;
            for (int i = 0; i < N - 2; i++) {
                count_p = int(tir_code_array[i] == 2) + int(tir_code_array[i + 1] == 2) +
                          int(tir_code_array[i + 2] == 2);
                if (start_flag == 0) {
                    if (count_p == 3) {
                        tir_type_array[event_num[0]] = 2;
                        start_array[event_num[0]] = correct_datetime_array[i];
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = max(max(bg_array[i], bg_array[i + 1]),
                                                         max(bg_array[i + 2], bg_array[i + 1]));
                        start_flag = 1;
                    }
                } else {
                    if (count_p == 0) {
                        end_array[event_num[0]] = correct_datetime_array[i - 1];
                        event_num[0]++;
                        start_flag = 0;
                    } else {
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = max(extre_values[event_num[0]],
                                                         bg_array[i + 2]);
                    }
                }
            }
            if (start_flag == 1) {
                //end_array[event_num[0]] = correct_datetime_array[N - 1];;
                event_num[0]++;
                start_flag == 0;
            }

            //极低血糖事件
            count_p = 0;
            start_flag = 0;
            for (int i = 0; i < N - 2; i++) {
                count_p = int(tir_code_array[i] == -2) + int(tir_code_array[i + 1] == -2) +
                          int(tir_code_array[i + 2] == -2);
                if (start_flag == 0) {
                    if (count_p == 3) {
                        tir_type_array[event_num[0]] = -2;
                        start_array[event_num[0]] = correct_datetime_array[i];
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = min(min(bg_array[i], bg_array[i + 1]),
                                                         min(bg_array[i + 2], bg_array[i + 1]));
                        start_flag = 1;
                    }
                } else {
                    if (count_p == 0) {
                        end_array[event_num[0]] = correct_datetime_array[i - 1];
                        event_num[0]++;
                        start_flag = 0;
                    } else {
                        end_array[event_num[0]] = correct_datetime_array[i + 2];
                        extre_values[event_num[0]] = min(extre_values[event_num[0]],
                                                         bg_array[i + 2]);
                    }
                }
            }
            if (start_flag == 1) {
                //end_array[event_num[0]] = correct_datetime_array[N - 1];
                event_num[0]++;
                start_flag == 0;
            }


            for (int k = 0; k < event_num[0]; k++) {
                end_array[k] = end_array[k] + 300;
            }
        }
    }
    catch (const std::exception &) {
        stats_code = 500;
    }
    return stats_code;
}

int gen_agp_old(float *bg_array, int *code_array, int N, float *per5_array, float *per25_array,
                float *per50_array, float *per75_array, float *per95_array) {
    int stats_code = 200;

    float **group_array = new float *[288];
    for (int i = 0; i < 288; i++) {
        group_array[i] = new float[14];
    }
    int *code_len_array = new int[288];

    try {
        stats_code = bg_group_time(bg_array, code_array, N, group_array, code_len_array);
        if (stats_code == 200) {
            for (int i = 0; i < 288; i++) {
                /*float* temp_group = group_array[i];*/
                int code_N = code_len_array[i];
                per5_array[i] = percentile(group_array[i], code_N, 5);
                per25_array[i] = percentile(group_array[i], code_N, 25);
                per50_array[i] = percentile(group_array[i], code_N, 50);
                per75_array[i] = percentile(group_array[i], code_N, 75);
                per95_array[i] = percentile(group_array[i], code_N, 95);
                //delete[] temp_group;
            }
        }
        for (int i = 0; i < 288; i++) {
            delete[] group_array[i];
        }
        delete[] group_array;
        delete[] code_len_array;

    }
    catch (const std::exception &) {
        stats_code = 500;
    }

    return stats_code;
}

void agp_kalman_filter(float *bg_array, int N) {
    //float* bg_filtered_array = new float[N];
    float agp_state_kalman = 0.0;
    float agp_state_pre = 0.0;
    float agp_Pminus = 0.0;
    float agp_K = 0.0;
    float agp_R = 1.2;
    float agp_Q = 0.5;
    float agp_P = 0.01;

    for (int i = 0; i < N; i++) {
        float data = bg_array[i];
        if (i > 1) {
            agp_state_pre = agp_state_kalman;
            agp_Pminus = agp_P + agp_Q;
            agp_K = agp_Pminus / (agp_Pminus + agp_R);
            agp_state_kalman = agp_state_pre + agp_K * (data - agp_state_pre);
            agp_P = (1 - agp_K) * agp_Pminus;
        } else {
            agp_state_kalman = data;
        }
        bg_array[i] = agp_state_kalman;
    }
    //return bg_filtered_array;
}

void Reversal_function(float *bg_array, int N) {
    double temp_data = 0;
    int i = 0;
    for (i = 0; i < int(N / 2); i++) {
        temp_data = bg_array[i];
        bg_array[i] = bg_array[N - 1 - i];
        bg_array[N - 1 - i] = temp_data;
    }
}

void agp_filter_old(float *bg_array, int N) {
    //float* bg_filtered_array = new float[N];
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
    Reversal_function(bg_array, N);
    agp_kalman_filter(bg_array, N);
}