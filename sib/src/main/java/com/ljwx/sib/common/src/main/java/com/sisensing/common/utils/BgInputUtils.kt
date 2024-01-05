package com.sisensing.common.utils

import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.widget.EditText
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.sisensing.common.R
import com.sisensing.common.constants.Constant

object BgInputUtils {

    @JvmStatic
    fun getMgFloat(input: String, isMmol: Boolean): Float {
        val float = LanguageNumberUtils.string2Float(input)
        return GlucoseUtils.getFloatValue(float, isMmol, false)
    }

    /**
     * 输入血糖值是否符合要求
     */
    @JvmStatic
    fun inputPass(alarmLower: String, alarmUpper: String, isMmol: Boolean): Boolean {
        if (ObjectUtils.isEmpty(alarmUpper) || ObjectUtils.isEmpty(alarmLower)) {
            ToastUtils.showShort(StringUtils.getString(R.string.personalcenter_complete_profile_not_empty_tips))
            return false
        }
        val minValue = GlucoseUtils.getConvertValueWithUnit(Constant.BS_MIN, true, isMmol)
        val maxValue = GlucoseUtils.getConvertValueWithUnit(Constant.BS_MAX, true, isMmol)

        if (GlucoseUtils.lessThanMin(alarmUpper, isMmol)) {
            ToastUtils.showShort(StringUtils.getString(R.string.alert_target_lower_than, minValue))
            Log.d("血糖输入", "最高比最小值低")
            return false
        }
        if (GlucoseUtils.moreThanMax(alarmUpper, isMmol)) {
            ToastUtils.showShort(StringUtils.getString(R.string.alert_target_higher_than, maxValue))
            Log.d("血糖输入", "最高比最大值高")
            return false
        }

        if (GlucoseUtils.lessThanMin(alarmLower, isMmol)) {
            ToastUtils.showShort(StringUtils.getString(R.string.alert_target_lower_than, minValue))
            Log.d("血糖输入", "最低比最小值低")
            return false
        }
        if (GlucoseUtils.moreThanMax(alarmLower, isMmol)) {
            ToastUtils.showShort(StringUtils.getString(R.string.alert_target_higher_than, maxValue))
            Log.d("血糖输入", "最低比最大值高")
            return false
        }
        if (LanguageNumberUtils.string2Float(alarmUpper) <= LanguageNumberUtils.string2Float(
                alarmLower
            )
        ) {
            ToastUtils.showShort(StringUtils.getString(R.string.alert_high_target_greater_than_low_target))
            Log.d("血糖输入", "最高小于最低")
            return false
        }
        return true
    }

    /**
     * 设置edittext属性
     */
    @JvmStatic
    fun initEditText(lower: EditText, upper: EditText?) {
        init(lower)
        upper?.let {
            init(it)
        }
    }

    private fun init(et: EditText) {
        val isMmol = ConfigUtils.getInstance().unitIsMmol()
        if (isMmol) {
            et.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        } else {
            et.inputType = InputType.TYPE_CLASS_NUMBER
        }
        if (isMmol) {
            et.keyListener = DigitsKeyListener.getInstance("0123456789.,")
            val filter = InputFilter { source, start, end, dest, dstart, dend ->
                if (dest.isNotEmpty()
                    && (source.contains(".") || source.contains(","))
                    && (dest.contains(".") || dest.contains(","))
                ) {
                    ""
                } else {
                    null
                }
            }
            et.filters = arrayOf(LengthFilter(6), filter)
        } else {
            et.filters = arrayOf(LengthFilter(6))
        }
        et.addTextChangedListener(object : TextWatcher {
            var deleteLastChar = false // 是否需要删除末尾

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if (s.toString().contains(".")) {
                    // 如果点后面有超过三位数值,则删掉最后一位
                    val length = s.length - s.toString().lastIndexOf(".")
                    // 说明后面有三位数值
                    deleteLastChar = length >= 3
                }
                if (s.toString().contains(",")) {
                    // 如果点后面有超过三位数值,则删掉最后一位
                    val length = s.length - s.toString().lastIndexOf(",")
                    // 说明后面有三位数值
                    deleteLastChar = length >= 3
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (deleteLastChar) {
                    // 设置新的截取的字符串
                    et.setText(s.toString().substring(0, s.toString().length - 1))
                    // 光标强制到末尾
                    et.setSelection(et.text.length)
                }
                // 以小数点开头，前面自动加上 "0"
                if (s.toString().startsWith(".")) {
                    et.setText("0$s")
                    et.setSelection(et.text.length)
                }
                if (s.toString().startsWith(",")) {
                    et.setText("0$s")
                    et.setSelection(et.text.length)
                }
            }
        })
    }

}