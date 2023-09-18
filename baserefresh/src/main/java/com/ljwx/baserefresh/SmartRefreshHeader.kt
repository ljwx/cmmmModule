package com.ljwx.baserefresh

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import com.ljwx.baseapp.view.IViewRefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class SmartRefreshHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), RefreshHeader ,IViewRefreshHeader{

    val progress = ProgressBar(ContextThemeWrapper(context, R.style.refresh_header_progressbar))

    init {
        val gradientDrawable = GradientDrawable()
        val color = Color.parseColor("#FF8F40")
        gradientDrawable.setColor(color)
        gradientDrawable.cornerRadius = dp2px(5f)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, dp2px(80f).toInt())
        params.gravity = Gravity.CENTER
        progress.progressDrawable = gradientDrawable
        val padding = (15 * resources.displayMetrics.density).toInt()
        progress.setPadding(padding, padding, padding, padding)
        addView(progress, params)
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 500
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        Log.e("ljwx-refresh-release", height.toString())
    }

    override fun getView(): View {
        return this
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
//        if (newState == RefreshState.PullDownToRefresh) {
//            showListener.invoke(true)
//        }
//        if (newState == RefreshState.None) {
//            showListener.invoke(false)
//        }
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
//        if (offset < 1) {
//            showListener?.invoke(false)
//        } else {
//            showListener?.invoke(true)
//        }
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

}