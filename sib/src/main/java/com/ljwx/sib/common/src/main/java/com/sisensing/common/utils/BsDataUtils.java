package com.sisensing.common.utils;


import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.sisensing.common.R;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.entity.BloodGlucoseEntity.BgEvent;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.BloodGlucoseEntity.DailyVO;
import com.sisensing.common.entity.BloodGlucoseEntity.GlucoseWave;
import com.sisensing.common.entity.BloodGlucoseEntity.ReportPointVO;
import com.sisensing.common.entity.DailyEventEntity;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.user.UserInfoUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author y.xie
 * @date 2021/4/14 10:33
 * @desc 血糖数据相关计算工具类
 */
public class BsDataUtils {
    /**
     * 获取当天的血糖数据
     * @param listener
     */
    public static void getCurrentDateBsData(final RoomResponseListener<List<BloodGlucoseEntity>> listener){
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) return;
        String deviceName = deviceEntity.getDeviceName();

        if (ObjectUtils.isEmpty(deviceName)) return;
        //当天的起始时间戳
        long startMills = CommonTimeUtils.getStartTime(new Date()).getTime();
        //当天的结束时间戳
        long endMills = CommonTimeUtils.getEndTime(new Date()).getTime();
        String userId = UserInfoUtils.getUserId();
        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getPeriodBloodGlucose(userId,deviceName, startMills, endMills),
                new RoomResponseListener<List<BloodGlucoseEntity>>() {
                    @Override
                    public void response(@Nullable List<BloodGlucoseEntity> bloodGlucoseEntities) {
                        if (ObjectUtils.isEmpty(bloodGlucoseEntities)) return;

                        listener.response(bloodGlucoseEntities);
                    }
                });
    }

    /**
     * 从数据库获取两个时间戳之间的血糖数据
     * @param startMills
     * @param endMills
     * @param listener
     */
    public static void getTwoMillsBsDataFromSql(long startMills, long endMills, final RoomResponseListener<List<BloodGlucoseEntity>> listener){
        String useId = UserInfoUtils.getUserId();
        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getPeriodBloodGlucose(useId,DeviceManager.getInstance().getDeviceEntity().getDeviceName(), startMills, endMills),
                new RoomResponseListener<List<BloodGlucoseEntity>>() {
                    @Override
                    public void response(@Nullable List<BloodGlucoseEntity> bloodGlucoseEntities) {
                        if (ObjectUtils.isEmpty(bloodGlucoseEntities)) return;

                        listener.response(bloodGlucoseEntities);
                    }
                });
    }

    /**
     * 从集合中筛选两个时间戳之间的血糖数据
     * @param startMills
     * @param endMills
     */
    public static List<BloodGlucoseEntity> getTwoMillsBsDataFromList(long startMills, long endMills, List<BloodGlucoseEntity> bloodGlucoseEntityList){
        List<BloodGlucoseEntity> entityList = new ArrayList<>();
        for (int i = 0; i < bloodGlucoseEntityList.size(); i++) {
            BloodGlucoseEntity bloodGlucoseEntity = bloodGlucoseEntityList.get(i);
            long mills = bloodGlucoseEntity.getProcessedTimeMill();
            if (startMills<=mills && mills<=endMills){
                entityList.add(bloodGlucoseEntity);
            }
        }
        return entityList;
    }

    /**
     * 筛选出两个血糖值区间的血糖数据
     * @param minValue 区间起点
     * @param maxValue 区间终点
     * @param bloodGlucoseEntityList 数据源
     * @return
     */
    public static List<BloodGlucoseEntity> getIntervalBsData(float minValue,float maxValue,List<BloodGlucoseEntity> bloodGlucoseEntityList){
        List<BloodGlucoseEntity> bsList = new ArrayList<>();
        for (int i = 0; i < bloodGlucoseEntityList.size(); i++) {
            BloodGlucoseEntity bloodGlucoseEntity = bloodGlucoseEntityList.get(i);
            float bsValue = bloodGlucoseEntity.getGlucoseValue();
            if (bsValue>=minValue && bsValue<=maxValue){
                bsList.add(bloodGlucoseEntity);
            }
        }
        return bsList;
    }

    /**
     * 删除集合中异常情况的血糖数据
     * @param bloodGlucoseEntityList 数据源
     * @return
     */
    public static List<BloodGlucoseEntity> deleteExceptionBsData(List<BloodGlucoseEntity> bloodGlucoseEntityList){
        Iterator<BloodGlucoseEntity> iterator = bloodGlucoseEntityList.iterator();

        while (iterator.hasNext()){
            BloodGlucoseEntity bloodGlucoseEntity = iterator.next();
            float value = bloodGlucoseEntity.getGlucoseValue();
            if (value<2.2f){
                bloodGlucoseEntity.setGlucoseValue(2.2f);
            }
            if (value>25f){
                bloodGlucoseEntity.setGlucoseValue(25f);
            }
            int alarmStatus = bloodGlucoseEntity.getAlarmStatus();
            //去除电流，温度异常的数据
            if (alarmStatus == 2 || alarmStatus == 3 || alarmStatus ==4){
                iterator.remove();
            }
        }
        return bloodGlucoseEntityList;
    }



    public static String getPercent(int num,int totalNUm,int newValue){
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后1位
        numberFormat.setMaximumFractionDigits(newValue);
        return numberFormat.format(((float)num)/(float) totalNUm*100);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DailyVO calcDailyData(Long timeStamp, List<BloodGlucoseEntity> glucoseList, BigDecimal targetUpper, BigDecimal targetLower, List<BloodGlucoseEntity> lastGlucoseList) {
        DailyVO dailyVO = new DailyVO(timeStamp);
        List<ReportPointVO> list = glucoseList.stream().map(t-> new ReportPointVO(t.getProcessedTimeMill(), BigDecimal.valueOf(t.getGlucoseValue()))).collect(Collectors.toList());
        dailyVO.setData(new ArrayList<>(list));
        // 血糖累加值
        BigDecimal valueSum = BigDecimal.ZERO;
        // 血糖总数
        int valueCount = 0;
        // TAR高于目标范围的点数
        int tarCount = 0;
        // TBR低于目标范围的点数
        int tbrCount = 0;
        // 低血糖事件
        Integer tbrEventStart = null;
        Long tbrStartDatetime = null;
        Integer tbrEventEnd = null;
        Long tbrEndDatetime = null;
        BigDecimal tbrEventMinValue = null;
        BigDecimal tbrEventNightMinValue = null;
        // 高血糖事件
        Integer tarEventStart = null;
        Long tarStartDatetime = null;
        Integer tarEventEnd = null;
        BigDecimal tarEventMinValue = null;

        List<BgEvent> tbrEvent = new ArrayList<>();
        List<BgEvent> tbrEventNight = new ArrayList<>();
        List<BgEvent> tarEvent = new ArrayList<>();

        BigDecimal value = new BigDecimal(String.valueOf(glucoseList.get(0).getGlucoseValue()));
        dailyVO.setLower(value);
        dailyVO.setUpper(value);
        for (BloodGlucoseEntity info : glucoseList) {
            value = new BigDecimal(String.valueOf(info.getGlucoseValue()));
            valueSum = valueSum.add(value);
            if (value.compareTo(targetUpper) > 0) {
                tarCount ++;
            } else if (value.compareTo(targetLower) < 0) {
                tbrCount ++;
            }
            if (value.compareTo(dailyVO.getUpper()) > 0) {
                dailyVO.setUpper(value);
            } else if (value.compareTo(dailyVO.getLower()) < 0) {
                dailyVO.setLower(value);
            }
            // 统计低血糖事件
            Integer index = info.getIndex();
            if (value.compareTo(targetLower)<0) {
                // 低血糖
                if (tbrEventStart==null) {
                    tbrStartDatetime = info.getProcessedTimeMill();
                    tbrEventStart = index;
                    tbrEventMinValue = value;
                } else {
                    if (value.compareTo(tbrEventMinValue)<0) {
                        tbrEventMinValue = value;
                    }
                    LocalDateTime nowDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(info.getProcessedTimeMill()), ZoneId.systemDefault());
                    if (nowDateTime.getHour()<=6) {
                        // 六点前认为夜间血糖
                        if (tbrEventNightMinValue==null || value.compareTo(tbrEventNightMinValue)<0) {
                            tbrEventNightMinValue = value;
                        }
                    }
                }
                // 重置结束时间
                tbrEventEnd = null;
            } else {
                // 非低血糖
                if (tbrEventStart!=null) {
                    if (tbrEventEnd == null) {
                        // 触发结束判定逻辑
                        tbrEventEnd = index;
                        tbrEndDatetime = info.getProcessedTimeMill();
                    } else {
                        if (index-tbrEventEnd>=15) {
                            // 恢复正常达到15分钟
                            int duration = tbrEventEnd-tbrEventStart;
                            if (duration>=15) {
                                //大于等于15分钟认为触发了低血糖事件
                                tbrEvent.add(new BgEvent(tbrStartDatetime, tbrEventMinValue, BigDecimal.valueOf(duration)));
                                LocalDateTime startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(tbrStartDatetime), ZoneId.systemDefault());
                                LocalDateTime endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(tbrEndDatetime), ZoneId.systemDefault());
                                if (startDateTime.getHour()<=6) {
                                    // TODO: 2021/4/2 夜间低血糖应该按照实际打卡时间计算，目前先按0-6时计算，后期待处理
                                    // 夜间低血糖事件（00：00-06：00）
                                    LocalDateTime endTime = LocalDateTime.of(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDayOfMonth(), 6, 0);
                                    if (endTime.isAfter(endDateTime)) {
                                        endTime = endDateTime;
                                    }
                                    long nightMin = Duration.between(startDateTime, endTime).toMinutes();
                                    if (nightMin>=15) {
                                        tbrEventNight.add(new BgEvent(tbrStartDatetime, tbrEventNightMinValue, BigDecimal.valueOf(nightMin)));
                                    }
                                }
                            }
                            tbrStartDatetime = null;
                            tbrEndDatetime = null;
                            tbrEventStart = null;
                            tbrEventEnd = null;
                            tbrEventMinValue = null;
                            tbrEventNightMinValue = null;
                        }
                    }
                }
            }
            // 统计高血糖事件
            if (value.compareTo(targetUpper)>0) {
                // 高血糖
                if (tarEventStart==null) {
                    tarStartDatetime = info.getProcessedTimeMill();
                    tarEventStart = index;
                    tarEventMinValue = value;
                } else {
                    if (value.compareTo(tarEventMinValue)>0) {
                        tarEventMinValue = value;
                    }
                }
                // 重置结束时间
                tarEventEnd = null;
            } else {
                // 非高血糖
                if (tarEventStart!=null) {
                    if (tarEventEnd == null) {
                        // 触发结束判定逻辑
                        tarEventEnd = index;
                    } else {
                        if (index-tarEventEnd>=15) {
                            // 恢复正常达到15分钟
                            int duration = tarEventEnd-tarEventStart;
                            if (duration>=15) {
                                //大于等于15分钟认为触发了高血糖事件
                                tarEvent.add(new BgEvent(tarStartDatetime, tarEventMinValue, BigDecimal.valueOf(duration)));
                            }
                            tarStartDatetime = null;
                            tarEventStart = null;
                            tarEventEnd = null;
                            tarEventMinValue = null;
                        }
                    }
                    if (tarEventStart!=null && (index-tarEventStart)<15) {
                        tarStartDatetime = null;
                        tarEventStart = null;
                        tarEventEnd = null;
                        tarEventMinValue = null;
                    }
                }
            }
            valueCount ++;
        }
        dailyVO.setTbrEvent(tbrEvent);
        dailyVO.setTbrEventNight(tbrEventNight);
        dailyVO.setTarEvent(tarEvent);
        List<BigDecimal> glucoseValueList = glucoseList.stream().map(bloodGlucoseEntity ->new BigDecimal(String.valueOf(bloodGlucoseEntity.getGlucoseValue()))).collect(Collectors.toList());
        // 探头点数
        dailyVO.setCount((long) valueCount);
        // MG平均葡萄糖值
        dailyVO.setMg(valueSum.divide(BigDecimal.valueOf(valueCount), 2, BigDecimal.ROUND_HALF_UP));
        // TAR高于目标范围的时间
        dailyVO.setTar(BigDecimal.valueOf(tarCount*5));
        // TBR低于目标范围的时间
        dailyVO.setTbr(BigDecimal.valueOf(tbrCount*5));
        // TIR目标范围内时间
        dailyVO.setTir(BigDecimal.valueOf((valueCount-tarCount-tbrCount)*5));
        // LAGE最大血糖波动振幅
        dailyVO.setLage(dailyVO.getUpper().subtract(dailyVO.getLower()));
        // SD葡萄糖标准差
        dailyVO.setSd(standardDeviationValue(glucoseValueList, dailyVO.getMg()));
        // MAGE平均血糖波动幅度
        dailyVO.setMage(MAGE(glucoseValueList, dailyVO.getSd()));
        // CV变异系数
        dailyVO.setCv(dailyVO.getSd().divide(dailyVO.getMg(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)));
        // MODD日间血糖平均绝对差
        if (lastGlucoseList!=null && lastGlucoseList.size()>0) {
            dailyVO.setModd(
                     MODD(
                            glucoseList.stream().collect(Collectors.toMap(bloodGlucoseEntity -> bloodGlucoseEntity.getIndex(), bloodGlucoseEntity -> new BigDecimal(String.valueOf(bloodGlucoseEntity.getGlucoseValue())))),
                            lastGlucoseList.stream().collect(Collectors.toMap(bloodGlucoseEntity -> bloodGlucoseEntity.getIndex(), bloodGlucoseEntity -> new BigDecimal(String.valueOf(bloodGlucoseEntity.getGlucoseValue()))))
                    )
            );
        } else {
            dailyVO.setModd(BigDecimal.ZERO);
        }
        return dailyVO;
    }


    /**
     *获取血糖数据时间区间内的MODD
     * @param glucoseMap 当日血糖数据
     * @param lastGlucoseMap 前一天血糖数据
     * @return MODD日间血糖平均绝对差
     */
    public static BigDecimal MODD(Map<Integer, BigDecimal> glucoseMap, Map<Integer, BigDecimal> lastGlucoseMap) {
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (Integer index: lastGlucoseMap.keySet()) {
            // 仅对两天数据重合部分进行计算
            int offset = index + 1440;
            if (glucoseMap.containsKey(offset)) {
                sum = sum.add(lastGlucoseMap.get(index).subtract(glucoseMap.get(offset)).abs());
                count++;
            }
        }
        if (count == 0){
            return new BigDecimal("0");
        }
        return sum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal standardDeviationValue(List<BigDecimal> list, BigDecimal avg) {
        return BigDecimal.valueOf(Math.sqrt(varianceValue(list, avg).doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private static BigDecimal varianceValue(List<BigDecimal> list, BigDecimal avg) {
        BigDecimal var = BigDecimal.ZERO;
        for (BigDecimal value : list) {
            var = var.add(value.subtract(avg).pow(2));
        }
        return var.divide(BigDecimal.valueOf(list.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 根据有效波动列表计算MAGE指标
     * @param waveList 有效波动列表
     * @param sd 标准差
     * @return MAGE平均血糖波动幅度
     */
    private static BigDecimal calcMage(List<GlucoseWave> waveList, BigDecimal sd) {
        // 有效波动
        int index = 0;
        for (int i=1; i<waveList.size(); i++) {
            BigDecimal age = waveList.get(i).getValue().subtract(waveList.get(i-1).getValue()).abs();
            if (age.compareTo(sd)>0) {
                // 有效波动
                index = i;
                break;
            }
        }
        // 计算age
        BigDecimal ageSum = BigDecimal.ZERO;
        int ageCount = 0;
        for (int i=index; i<waveList.size(); i+=2) {
            if (i>0 && i<waveList.size()-1) {
                // index 符合要求
                GlucoseWave wave = waveList.get(i);
                BigDecimal d1 = wave.getValue().subtract(waveList.get(i-1).getValue()).abs();
                BigDecimal d2 = wave.getValue().subtract(waveList.get(i+1).getValue()).abs();
                if (d1.compareTo(sd)>0 && d2.compareTo(sd)>0) {
                    // 形成一个有效的age
                    ageSum = ageSum.add(d1);
                    ageCount ++;
                }
            }
        }
        if (ageCount>0){
            return ageSum.divide(BigDecimal.valueOf(ageCount), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取血糖数据时间区间内的MAGE
     * @param glucoseList 血糖列表
     * @param sd 标准差
     * @return MAGE平均血糖波动幅度
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static BigDecimal MAGE(List<BigDecimal> glucoseList, BigDecimal sd) {
        // 寻找极值点
        List<GlucoseWave> peaks = findPeaks(glucoseList);
        // 寻找有效波动
        peaks = removePeaks(peaks, sd);
        return calcMage(peaks, sd);
    }

    /**
     * 寻找血糖数组的极值点
     * @param list 血糖数组
     * @return 极值点列表
     */
    private static List<GlucoseWave> findPeaks(List<BigDecimal> list) {
        //List<GlucoseWave> waves = Lists.newArrayList();
        List<GlucoseWave> waves = new ArrayList();
        int direction = list.get(0).compareTo(BigDecimal.ZERO) > 0 ? -1 : 1;
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                BigDecimal data1 = list.get(i + 1), data0 = list.get(i);
                if ((data1.subtract(data0)).multiply(new BigDecimal(direction)).compareTo(BigDecimal.ZERO) > 0) {
                    direction *= -1;
                    if (direction == 1) {
                        //极大值
                        waves.add(new GlucoseWave(1, data0));
                    } else {
                        //极小值
                        waves.add(new GlucoseWave(2, data0));
                    }
                }
            } else {
                // 最后一个点也作为极值处理
                if (direction == -1) {
                    //极大值
                    waves.add(new GlucoseWave(1, list.get(i)));
                } else {
                    //极小值
                    waves.add(new GlucoseWave(2, list.get(i)));
                }
            }
        }
        return waves;
    }

    /**
     * 去除不满足条件的极值点
     * @param list 极值点列表
     * @param sd 标准差
     * @return 新的极值点列表
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<GlucoseWave> removePeaks(List<GlucoseWave> list, BigDecimal sd) {
        List<GlucoseWave> removeList = new ArrayList<>();
        for (int i=1; i<list.size()-1; i++) {
            GlucoseWave wave = list.get(i);
            if (wave.getFlag()==1) {
                // 仅处理极大值的情况
                BigDecimal d1 = list.get(i-1).getValue().subtract(wave.getValue()).abs();
                BigDecimal d2 = list.get(i+1).getValue().subtract(wave.getValue()).abs();
                switch (d1.compareTo(d2)) {
                    case -1:
                        // d1<d2
                        // 如果 D1 绝对值小于 D2 绝对值， 则比较 D1 绝
                        // 对值与 SDBG 的大小; 如果 D1 绝对值小于 SDBG， 则
                        // 记录当前极大值点相邻左侧的极小值点。
                        if (d1.compareTo(sd)<0) {
                            removeList.add(list.get(i-1));
                        }
                        break;
                    case 1:
                    case 0:
                        // d1>d2
                        // 如果 D1 绝对值大于 D2 绝对值， 则
                        // 比较 D2 绝对值与 SDBG 的大小; 如果 D2 绝对值小
                        // 于 SDBG， 则记录当 前 极大值 点相 邻 右 侧 的 极 小 值 点。
                        // d1=d2
                        // 如果 D1 绝对值等于 D2 绝对值， 则比较 D2 绝对值与 SDBG
                        // 的大小; 如果 D2 绝对值小于 SDBG， 则记录当前极
                        // 大值点相邻右侧的极小值点。
                        if (d2.compareTo(sd)<0) {
                            removeList.add(list.get(i+1));
                        }
                        break;
                    default:
                        throw new RuntimeException("Unexpected error while removing peaks.");
                }
            }
        }
        if (removeList.size()==0) {
            // 不存在不符合规则的极值点，结束递归
            return list;
        } else {
            // 存在需要剔除的极值点，继续处理
            // 步骤四，删除记录的极值点
            removeList.forEach(list::remove);
            // 步骤五，保留相邻极小值之间最大的极大值，删除其余极大值
            removeList.clear();
            int maxIndex = -1;
            BigDecimal maxValue = BigDecimal.ZERO;
            for (int i=0; i<list.size(); i++) {
                GlucoseWave wave = list.get(i);
                if (wave.getFlag()==2) {
                    //极小值
                    maxIndex = -1;
                    maxValue = BigDecimal.ZERO;
                } else {
                    //极大值
                    if (wave.getValue().compareTo(maxValue)>0) {
                        // 去除小的极大值
                        if (maxIndex>=0) {
                            removeList.add(list.get(maxIndex));
                        }
                        maxIndex = i;
                        maxValue = wave.getValue();
                    } else {
                        removeList.add(wave);
                    }
                }
            }
            // 删除多余的极大值
            removeList.forEach(list::remove);
            // 继续递归
            return removePeaks(list, sd);
        }
    }

    /**
     * 获取高低血糖事件、持续时间
     *
     * @return
     */
    public static DailyEventEntity getDailyEvent(List<BloodGlucoseEntity> entities, float highTop, float targetHigh, float targetLow, float lowBottom) {
        DailyEventEntity dailyEvent = new DailyEventEntity();
        //高于目标值的事件数
        int highEventCount = 0;
        //低于目标值的事件数
        int lowEventCount = 0;
        //高于某个目标值时间
        long highEventTime = 0;
        //高于目标范围时间
        long rangeHighEventTime = 0;
        //目标范围时间
        long rangeTargetTime = 0;
        //低于某个目标值时间
        long lowEventTime = 0;
        //低于目标范围时间
        long rangeLowEventTime = 0;
        //高于最大值
        long startHighTime = 0, lastHighIndex = 0;
        //高于目标最高值小于最大值
        long startHighRangeTime = 0, lastHighRangeIndex = 0;
        //低于最小值
        long startLowTime = 0, lastLowIndex = 0;
        //低于目标最小值大于最低值
        long startLowRangeTime = 0, lastLowRangeIndex = 0;
        for (int i = 0; i < entities.size(); i++) {
            BloodGlucoseEntity entity = entities.get(i);
            //血糖值
            float value = GlucoseUtil.getTransferValue(entity.getGlucoseValue());
            if (value > highTop) { //高于最大值
                //高于最高事件开始
                if (startHighTime == 0) {
                    startHighTime = entity.getProcessedTimeMill();
                } else {
                    //判断是否连续，若不连续重置初始时间并判断是否持续15分钟
                    if (++lastHighIndex != i) {
                        long diffTime = entity.getProcessedTimeMill() - startHighTime;
                        if (diffTime >= 15 * 60 * 1000) {
                            highEventCount++;
                            highEventTime += diffTime;
                        }
                        startHighTime = 0;
                    }
                }
                lastHighIndex = i;
            } else if (value < highTop && value > targetHigh) {//高于目标上限小于最大值
                if (startHighRangeTime == 0) {
                    startHighRangeTime = entity.getProcessedTimeMill();
                } else {
                    if (++lastHighRangeIndex != i) {
                        long diffTime = entity.getProcessedTimeMill() - startHighRangeTime;
                        if (diffTime >= 15 * 60 * 1000) {
                            highEventCount++;
                            rangeHighEventTime += diffTime;
                        }
                        startHighRangeTime = 0;
                    }
                }
                lastHighRangeIndex = i;
            } else if (value < targetLow && value > lowBottom) {//低于目标下限大于最小值
                if (startLowRangeTime == 0) {
                    startLowRangeTime = entity.getProcessedTimeMill();
                } else {
                    if (++lastLowRangeIndex != i) {
                        long diffTime = entity.getProcessedTimeMill() - startLowRangeTime;
                        if (diffTime >= 15 * 60 * 1000) {
                            lowEventCount++;
                            rangeLowEventTime += diffTime;
                        }
                        startLowRangeTime = 0;
                    }
                }
                lastLowRangeIndex = i;

            } else if (value < lowBottom) {//小于最小值
                if (startLowTime == 0) {
                    startLowTime = entity.getProcessedTimeMill();
                } else {
                    if (++lastLowIndex != i) {
                        long diffTime = entity.getProcessedTimeMill() - startLowTime;
                        if (diffTime >= 15 * 60 * 1000) {
                            lowEventCount++;
                            lowEventTime += diffTime;
                        }
                        startLowTime = 0;
                    }
                }
                lastLowIndex = i;
            }
        }

        long amountTime = entities.get(entities.size() - 1).getProcessedTimeMill() - entities.get(0).getProcessedTimeMill();
        rangeTargetTime = amountTime - lowEventTime - highEventTime - rangeLowEventTime - rangeHighEventTime;
        dailyEvent.setTotalTime(amountTime);
        dailyEvent.setHighEventCount(highEventCount);
        dailyEvent.setHighEventTime(highEventTime);
        dailyEvent.setRangeHighEventTime(rangeHighEventTime);
        dailyEvent.setLowEventCount(lowEventCount);
        dailyEvent.setLowEventTime(lowEventTime);
        dailyEvent.setRangeLowEventTime(rangeLowEventTime);
        dailyEvent.setRangeTargetTime(rangeTargetTime);
        return dailyEvent;
    }

    /**
     * 获取一天的某一种行为数据集合(去除一小时内重复打卡的数据)
     * @param list
     * @return
     */
    public static List<ActionRecordEntity> getOneDayBehaviorEventList(List<ActionRecordEntity> list){
        List<ActionRecordEntity> actionRecordEntities = new ArrayList<>();
        List<Map<Integer,ActionRecordEntity>> mapList = new ArrayList<>();
        //创建24条数据 0-23对应时间戳中的小时
        for (int i = 0; i < 24; i++) {
            Map<Integer,ActionRecordEntity> map = new HashMap<>();
            for (int j = 0; j < list.size(); j++) {
                ActionRecordEntity entity = list.get(j);
                long mill = entity.getStartTime();
                String time = TimeUtils.millis2String(mill,"HH:mm");
                //取出其中的小时(0-23小时)
                int hour = Integer.parseInt(time.split(":")[0]);
                if (i == hour){
                    //取出1个小时内第一次出现的数据
                    map.put(i,entity);
                    break;
                }
            }
            mapList.add(map);
        }

        for (int i = 0; i < mapList.size(); i++) {
            Map<Integer, ActionRecordEntity> map = mapList.get(i);
            for (Integer key:map.keySet()){
                ActionRecordEntity actionRecordEntity = map.get(key);
                if (actionRecordEntity!=null){
                    actionRecordEntities.add(actionRecordEntity);
                }
            }
        }

        return actionRecordEntities;
    }

    /**
     * 血糖值描述 偏低，正常，偏高
     * @param bsValue
     * @return
     */
//    public static String getBsDesc(Context context, float bsValue){
//        String desc ;
//        float upper = UserInfoUtils.getBsRangeUpper();
//        float lower = UserInfoUtils.getBsRangeLower();
//        if (lower<bsValue){
//            desc = context.getString(R.string.bsmonitoring_low);
//        }else if (bsValue>=lower&&bsValue<=upper){
//            desc = context.getString(R.string.bsmonitoring_normal);
//        }else {
//            desc = context.getString(R.string.bsmonitoring_high);
//        }
//        return desc;
//    }

    /**
     * 血糖值描述 偏低，正常，偏高
     * @param bsValue
     * @return
     */
    public static String getBsDesc(Context context, float bsValue){
        //1表示1型糖尿病、2表示2型糖尿病、4表示妊娠糖尿病、3表示特殊糖尿病、0表示未确诊/无糖尿病史 7其他)
        int diabetesType = UserInfoUtils.getDrType();
        String desc ;
        if (diabetesType == 0 || diabetesType == 7){
            //正常人

            if (bsValue<=GlucoseUtil.getTransferValue(3.9f)){
                //低血糖一级tbr
                desc = context.getString(R.string.bsmonitoring_low);
            }else if (bsValue>=GlucoseUtil.getTransferValue(3.9f) && bsValue<GlucoseUtil.getTransferValue(7.8f)){
                //正常血糖
                desc = StringUtils.getString(R.string.bsmonitoring_normal);
            }else {
                //高血糖一级tar
                desc = context.getString(R.string.bsmonitoring_high);
            }
        }else if (diabetesType == 1 || diabetesType == 2 || diabetesType == 3){
            //I型、II型糖尿病 3.表示特殊糖尿病、
            if (bsValue<GlucoseUtil.getTransferValue(3.0f)){
                //二级tbr
                desc = context.getString(R.string.bsmonitoring_Extremely_low);
            }else if (bsValue>=GlucoseUtil.getTransferValue(3.0f) && bsValue<GlucoseUtil.getTransferValue(3.9f)){
                //一级tbr
                desc = context.getString(R.string.bsmonitoring_low);
            }else if (bsValue>=GlucoseUtil.getTransferValue(3.9f) && bsValue<=GlucoseUtil.getTransferValue(10.0f)){
                //正常
                desc = StringUtils.getString(R.string.bsmonitoring_normal);
            }else if (bsValue>GlucoseUtil.getTransferValue(10.0f) && bsValue<=GlucoseUtil.getTransferValue(13.9f)){
                //一级tar
                desc = context.getString(R.string.bsmonitoring_high);
            }else {
                //二级tar
                desc = context.getString(R.string.bsmonitoring_Extremely_high);
            }
        }else if (diabetesType == 5 || diabetesType == 6) {
            if (bsValue < GlucoseUtil.getTransferValue(3.9f)) {
                desc = context.getString(R.string.bsmonitoring_low);
            } else if (bsValue >= GlucoseUtil.getTransferValue(3.9f) && bsValue <= GlucoseUtil.getTransferValue(10.0f)) {
                //正常
                desc = StringUtils.getString(R.string.bsmonitoring_normal);
            } else if (bsValue > GlucoseUtil.getTransferValue(10.0f) && bsValue <= GlucoseUtil.getTransferValue(13.9f)) {
                //偏高
                desc = context.getString(R.string.bsmonitoring_high);
            } else {
                //二级tar
                desc = context.getString(R.string.bsmonitoring_Extremely_high);
            }
        }else {
            //4.妊娠糖尿病

            if (bsValue<GlucoseUtil.getTransferValue(3.0f)){
                //低血糖二级tbr
                desc = context.getString(R.string.bsmonitoring_Extremely_low);
            }else if (bsValue>=GlucoseUtil.getTransferValue(3.0f) && bsValue<GlucoseUtil.getTransferValue(3.9f)){
                desc = context.getString(R.string.bsmonitoring_low);
                //低血糖一级tbr
            }else if (bsValue>=GlucoseUtil.getTransferValue(3.9f) && bsValue<=GlucoseUtil.getTransferValue(7.8f)){
                //正常血糖
                desc = StringUtils.getString(R.string.bsmonitoring_normal);
            }else {
                //高血糖一级tar
                desc = context.getString(R.string.bsmonitoring_high);
            }
        }

        return desc;
    }

    /**
     * 血糖值描述 偏低，正常，偏高
     * @param bsValue
     * @return
     */
    public static String getBsDesc(Context context, float bsValue,float low,float high){
        String desc;
        if (bsValue<=GlucoseUtil.getTransferMgValue(low)){
            //低血糖一级tbr
            desc = context.getString(R.string.bsmonitoring_low);
        }else if (bsValue>=GlucoseUtil.getTransferMgValue(low) && bsValue<=GlucoseUtil.getTransferMgValue(high)){
            //正常血糖
            desc = StringUtils.getString(R.string.bsmonitoring_normal);
        }else {
            //高血糖一级tar
            desc = context.getString(R.string.bsmonitoring_high);
        }
        return desc;
    }
}
