package com.sisensing.common.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ljwx.baseapp.extensions.isInt
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.basedialog.dialog.CustomDialog
import com.sisensing.common.R
import com.sisensing.common.databinding.DialogGlucoseAlarmBinding
import com.sisensing.common.entity.alarm.GlucoseAlarmDialogData
import com.sisensing.common.glucose.alarm.FriendAlarmUtils
import com.sisensing.common.entity.notification.FcmMessageBean
import com.sisensing.common.user.GlucoseAlarmGlobal
import com.sisensing.common.user.GlucoseAlarmGlobal.saveAlarmTime
import com.sisensing.common.utils.GlucoseAlarmSoundUtils
import com.sisensing.common.utils.GlucoseAlarmUtils
import com.sisensing.common.utils.Log

class DialogGlucoseAlarm(context: Context) : CustomDialog(context) {

    private var alarmType: Int? = null
    private var friendBean: FcmMessageBean? = null

    private val binding by lazy {
        DataBindingUtil.inflate<DialogGlucoseAlarmBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_glucose_alarm,
            null,
            false
        )
    }

    init {
        setView(binding.root)
        setDimAmount(0.6f)
        setWidthMatch()
        setCanceledOnTouchOutside(false)
        binding.ok.singleClick {
            alarmType?.let {
                GlucoseAlarmGlobal.pushServerKnow(it)
                saveAlarmTime(it)
                GlucoseAlarmGlobal.clearCount()
            }
            friendBean?.let {
                //点击通知已发送,是否再次发送需探讨
                GlucoseAlarmGlobal.pushServerKnow(it.eventType, it.id)
            }
            dismiss()
        }
    }

    fun changeData(type: Int?, value: String?) {
        val data = GlucoseAlarmDialogData(type, value)
        data.friendBean = null
        alarmType = type
        when (type) {
            GlucoseAlarmUtils.VERY_LOW -> {
                binding.titleIcon.visibleGone(true)
                binding.titleIcon.setImageResource(R.mipmap.glucose_alarm_dialog_icon)
                binding.title.setTextColor(context.resources.getColor(R.color.white))
                binding.time.visibleGone(true)
                binding.titleContainer.setBackgroundResource(R.drawable.shape_glucose_alarm_very_low)
                binding.ok.setBackgroundResource(R.drawable.shape_button_alarm_very_r10)
                Log.d("血糖报警", "低血糖弹窗数据修改")
                GlucoseAlarmSoundUtils.playSoundSelf(type)
            }

            GlucoseAlarmUtils.GLUCOSE_LOW -> {
                binding.titleIcon.visibleGone(true)
                binding.titleIcon.setImageResource(R.mipmap.glucose_alarm_dialog_icon)
                binding.title.setTextColor(context.resources.getColor(R.color.white))
                binding.time.visibleGone(true)
                binding.titleContainer.setBackgroundResource(R.drawable.shape_glucose_alarm_low)
                binding.ok.setBackgroundResource(R.drawable.shape_button_alarm_low_r10)
                GlucoseAlarmSoundUtils.playSoundSelf(type)
            }

            GlucoseAlarmUtils.GLUCOSE_HIGH -> {
                binding.titleIcon.visibleGone(true)
                binding.titleIcon.setImageResource(R.mipmap.glucose_alarm_dialog_icon)
                binding.title.setTextColor(context.resources.getColor(R.color.white))
                binding.time.visibleGone(true)
                binding.titleContainer.setBackgroundResource(R.drawable.shape_glucose_alarm_high)
                binding.ok.setBackgroundResource(R.drawable.shape_button_alarm_high_r10)
                GlucoseAlarmSoundUtils.playSoundSelf(type)
            }

            GlucoseAlarmUtils.SIGNAL_LOSS -> {
                binding.titleIcon.visibleGone(false)
                binding.title.setTextColor(context.resources.getColor(R.color.black))
                binding.time.visibleGone(false)
                binding.titleContainer.setBackgroundResource(R.color.transparent)
                binding.ok.setBackgroundResource(R.drawable.selector_button_theme_r10)
                GlucoseAlarmSoundUtils.playSoundSelf(type)
            }

        }
        binding.data = data
    }

    fun changeFriendData(bean: FcmMessageBean) {
        val type = bean.eventType
        val data = GlucoseAlarmDialogData(type, bean.v)
        friendBean = bean
        data.friendBean = bean
        val force = bean.forceRemind
        val style = bean.style
        val sound =
            if (bean.sound != null) if (bean.sound.isInt()) bean.sound.toInt() else null else null
        when (type) {
            FriendAlarmUtils.VERY_LOW -> {
                binding.titleIcon.visibleGone(true)
                binding.titleIcon.setImageResource(R.mipmap.friend_glucose_alarm_dialog_icon)
                binding.title.setTextColor(context.resources.getColor(R.color.white))
                binding.time.visibleGone(true)
                binding.titleContainer.setBackgroundResource(R.drawable.shape_glucose_alarm_very_low)
                binding.ok.setBackgroundResource(R.drawable.shape_button_alarm_very_r10)
                GlucoseAlarmSoundUtils.playStyleAndSound(force, style, sound)
            }

            FriendAlarmUtils.GLUCOSE_LOW -> {
                binding.titleIcon.visibleGone(true)
                binding.titleIcon.setImageResource(R.mipmap.friend_glucose_alarm_dialog_icon)
                binding.title.setTextColor(context.resources.getColor(R.color.white))
                binding.time.visibleGone(true)
                binding.titleContainer.setBackgroundResource(R.drawable.shape_glucose_alarm_low)
                binding.ok.setBackgroundResource(R.drawable.shape_button_alarm_low_r10)
                GlucoseAlarmSoundUtils.playStyleAndSound(force, style, sound)
            }

            FriendAlarmUtils.GLUCOSE_HIGH -> {
                binding.titleIcon.visibleGone(true)
                binding.titleIcon.setImageResource(R.mipmap.friend_glucose_alarm_dialog_icon)
                binding.title.setTextColor(context.resources.getColor(R.color.white))
                binding.time.visibleGone(true)
                binding.titleContainer.setBackgroundResource(R.drawable.shape_glucose_alarm_high)
                binding.ok.setBackgroundResource(R.drawable.shape_button_alarm_high_r10)
                GlucoseAlarmSoundUtils.playStyleAndSound(force, style, sound)
            }

            FriendAlarmUtils.SIGNAL_LOSS -> {
                binding.titleIcon.visibleGone(false)
                binding.title.setTextColor(context.resources.getColor(R.color.black))
                binding.time.visibleGone(false)
                binding.titleContainer.setBackgroundResource(R.color.transparent)
                binding.ok.setBackgroundResource(R.drawable.selector_button_theme_r10)
                GlucoseAlarmSoundUtils.playStyleAndSound(force, style, sound)
            }

        }
        binding.data = data
    }

}