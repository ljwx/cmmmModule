package com.sisensing.common.utils

import android.app.Service
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Vibrator
import com.blankj.utilcode.util.Utils
import com.ljwx.baseapp.extensions.isInt
import com.sisensing.common.R
import com.sisensing.common.user.GlucoseAlarmGlobal
import kotlin.math.max
import kotlin.math.min

object GlucoseAlarmSoundUtils {

    //声音提示
//    private val soundPool by lazy {
//        //初始化soundPool,设置可容纳12个音频流，音频流的质量为5，
//        val builder = SoundPool.Builder()
//        //传入音频数量
//        builder.setMaxStreams(3)
//        //AudioAttributes是一个封装音频各种属性的方法
//        val attrBuilder = AudioAttributes.Builder()
//        //设置音频流的合适的属性
//        attrBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM) //STREAM_MUSIC
//        //加载一个AudioAttributes
//        builder.setAudioAttributes(attrBuilder.build())
//        builder.build()
//    }

    //震动
    private val vibrator by lazy {
        Utils.getApp().getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    }

//    private val soundMap by lazy {
//        soundPool.load(Utils.getApp(), R.raw.alarm_voice1, 1)
//        val map = HashMap<Int, Int>()
//        //通过load方法加载指定音频流，并将返回的音频ID放入musicId中
//        map.put(0, soundPool.load(Utils.getApp(), R.raw.alarm_voice1, 1))
//        map.put(1, soundPool.load(Utils.getApp(), R.raw.alarm_voice2, 1))
//        map.put(2, soundPool.load(Utils.getApp(), R.raw.alarm_voice3, 1))
//        map.put(3, soundPool.load(Utils.getApp(), R.raw.alarm_voice4, 1))
//        map.put(4, soundPool.load(Utils.getApp(), R.raw.alarm_voice5, 1))
//        map.put(5, soundPool.load(Utils.getApp(), R.raw.alarm_voice6, 1))
//        map.put(6, soundPool.load(Utils.getApp(), R.raw.alarm_voice7, 1))
//        map.put(7, soundPool.load(Utils.getApp(), R.raw.alarm_voice8, 1))
//        map.put(8, soundPool.load(Utils.getApp(), R.raw.alarm_voice9, 1))
//        map.put(9, soundPool.load(Utils.getApp(), R.raw.alarm_voice10, 1))
//        map
//    }

    //当前播放的声音
    private var cspId = 0

    fun playSoundSelf(type: Int) {
        Log.d("血糖报警", "自己血糖,播放声音")
        GlucoseAlarmGlobal.alarmSettings.value?.apply {
            val force = forceRemind == true
            when (type) {
                GlucoseAlarmUtils.GLUCOSE_LOW -> {
                    Log.d("血糖报警", "低血糖播放声音")
                    lowAlarm?.apply {
                        val sss =
                            if (sound.isInt()) sound!!.toInt() else GlucoseAlarmUtils.SOUND_DEFAULT
                        playStyleAndSound(force, style ?: GlucoseAlarmUtils.STYLE_DEFAULT, sss)
                    }
                }

                GlucoseAlarmUtils.GLUCOSE_HIGH -> {
                    highAlarm?.apply {
                        val sss =
                            if (sound.isInt()) sound!!.toInt() else GlucoseAlarmUtils.SOUND_DEFAULT
                        playStyleAndSound(force, style ?: GlucoseAlarmUtils.STYLE_DEFAULT, sss)
                    }
                }

                GlucoseAlarmUtils.SIGNAL_LOSS -> {
                    signalLostAlarm?.apply {
                        val sss =
                            if (sound.isInt()) sound!!.toInt() else GlucoseAlarmUtils.SOUND_DEFAULT
                        playStyleAndSound(force, style ?: GlucoseAlarmUtils.STYLE_DEFAULT, sss)
                    }
                }

                GlucoseAlarmUtils.VERY_LOW -> {
                    veryLowAlarm?.apply {
                        val sss =
                            if (sound.isInt()) sound!!.toInt() else GlucoseAlarmUtils.SOUND_DEFAULT
                        playStyleAndSound(force, style ?: GlucoseAlarmUtils.STYLE_DEFAULT, sss)
                    }
                }
            }
        }
    }

    fun playStyleAndSound(force: Boolean?, style: Int?, sound: Int?) {
        Log.d("血糖报警", "声音,强制:$force-风格:$style-sound:$sound")
        if (sound == null) {
            return
        }
        if (force == true) {
//            silenceForceSound()
        }

        if (sound - 1 < 10) {
//            val s = soundMap[sound-1]
//            Log.d("血糖报警", "播放map中的音频为:$s")
//            cspId = soundPool.play(soundMap[sound - 1]!!, 0.7f, 0.7f, 0, 0, 1f)
            val soundIndex = Math.max(0, sound - 1)
            Log.d("血糖报警", "soundIndex:$soundIndex")
            MediaPlayUtils.playResource(Utils.getApp(), MediaPlayUtils.getRawRes(soundIndex))
        }
        if (isSoundAndShake(style)) {
            //震动
            vibrator.vibrate(1000)
        }
    }

    private fun silenceForceSound() {
        if (AudioUtils.isSilenceModel(Utils.getApp())) {
            AudioUtils.setMusicVolume(Utils.getApp(), 12)
        }
    }

    fun isSound(style: Int): Boolean {
        return style == 1
    }

    fun isSoundAndShake(style: Int?): Boolean {
        return style == 3
    }

    fun stopPlay() {
//        soundPool.stop(cspId)
    }

    fun release() {
//        soundPool.release()
    }

}