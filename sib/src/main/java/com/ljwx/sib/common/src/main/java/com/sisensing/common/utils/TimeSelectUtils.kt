package com.sisensing.common.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.sisensing.common.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object TimeSelectUtils {

    /**
     * 年月日时分时间选择
     * @param context
     * @param showColumns 年月日时分秒列显示与隐藏
     * @param listener
     */
    @JvmStatic
    @JvmOverloads
    fun showTimePicker(
        context: Context,
        showColumns: BooleanArray?,
        selectedDate: Date? = null,
        listener: OnTimeSelectListener?
    ) {
        //时间选择器
        val pvTime1 = TimePickerBuilder(
            context
        ) { date, v ->
            listener?.onTimeSelect(date, v)
        }.setType(showColumns) //年月日时分秒 的显示与否，不设置则默认全部显示
            .setCancelColor(Color.parseColor("#999999"))
            .setSubmitColor(Color.parseColor("#00D5B8"))
            .setTitleBgColor(Color.parseColor("#FFFFFF"))
            .setLabel("y", "m", "d", "h", "min", "") //默认设置为年月日时分秒
            .isCenterLabel(false)
            .setTextColorCenter(Color.BLACK) //设置选中项的颜色
            .setTextColorOut(Color.GRAY) //设置没有被选中项的颜色
            .setDate(getSelectedDate(selectedDate))
            .setLineSpacingMultiplier(1.2f)
            .setTextXOffset(-10, 0, 10, 0, 0, 0) //设置X轴倾斜角度[ -90 , 90°]
            //时间范围
            .setRangDate(getStartDate(), getEndDate())
//            .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
            .setDecorView(null)
            .setCancelText(context.getString(R.string.common_cancel))
            .setSubmitText(context.getString(R.string.common_know))
            .isDialog(true)
            .build()
        pvTime1.dialog?.window?.setWindowAnimations(R.style.picker_view_slide_anim)
        pvTime1.show()
        showByDialog(pvTime1)
    }

    private fun getSelectedDate(selectedDate: Date?): Calendar {
        val calendar = Calendar.getInstance()
        if (selectedDate == null) {
            calendar.time = Date(System.currentTimeMillis())
        } else {
            calendar.time = selectedDate
        }
        return calendar
    }

    private fun getStartDate(): Calendar {
        val startDate = Calendar.getInstance()
        startDate[1900, 0] = 1
        return startDate
    }

    private fun getEndDate(): Calendar {
        val curDate = Date(System.currentTimeMillis()) //获取当前时间
        val formatter_year = SimpleDateFormat("yyyy ")
        val year_str = formatter_year.format(curDate)
        val year_int = year_str.toDouble().toInt()
        val formatter_mouth = SimpleDateFormat("MM ")
        val mouth_str = formatter_mouth.format(curDate)
        val mouth_int = mouth_str.toDouble().toInt()
        val formatter_day = SimpleDateFormat("dd ")
        val day_str = formatter_day.format(curDate)
        val day_int = day_str.toDouble().toInt()
        val endDate = Calendar.getInstance()
        endDate[year_int, mouth_int - 1] = day_int
        return endDate
    }

    private fun showByDialog(pvTime1: TimePickerView) {
        pvTime1.dialog.window?.setDimAmount(0.7f)
        pvTime1.dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val params = pvTime1.dialog.window!!.attributes
        params.gravity = Gravity.BOTTOM
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        pvTime1.dialog.window!!.attributes = params
//        pvTime1.dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
//        pvTime1.dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#bb222222")))
        val container =
            pvTime1.dialog.findViewById<View>(com.bigkoo.pickerview.R.id.content_container)
        val p2: FrameLayout.LayoutParams = container.layoutParams as FrameLayout.LayoutParams
        p2.leftMargin = 0
        p2.rightMargin = 0
        container.layoutParams = p2
    }

    fun getReversedDate(end: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = end
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DAY_OF_MONTH, -days + 1) //当前也算一天
        return calendar.time
    }

    fun getIntervalDate(start: String?, end: String?, pattern: String): Int {
        if (start.isNullOrBlank() || end.isNullOrBlank()) {
            return 0
        }
        val diff = TimeUtils.string2Date(end, pattern).time -
                TimeUtils.string2Date(start, pattern).time
        val days = diff / (1000 * 60) / (60) / (24) + 1 //当天剩余的也算一天
        return days.toInt()
    }

}