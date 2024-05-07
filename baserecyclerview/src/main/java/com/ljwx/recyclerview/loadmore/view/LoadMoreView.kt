package com.ljwx.recyclerview.loadmore.view

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import com.ljwx.recyclerview.R

class LoadMoreView  : FrameLayout {

    companion object {

        const val STATE_LOADING = 0
        const val STATE_HAS_MORE = 3
        const val STATE_COMPLETE = 4
        const val STATE_ERROR = 5

        @RestrictTo(RestrictTo.Scope.LIBRARY)
        @PublishedApi
        internal val commonViews =
            mutableMapOf(
                Pair(STATE_ERROR, R.layout.common_rv_load_more_error),
                Pair(STATE_LOADING, R.layout.common_rv_load_more_loading),
                Pair(STATE_HAS_MORE, R.layout.common_rv_load_more_loading),
                Pair(STATE_COMPLETE, R.layout.common_rv_load_more_complete),
            )

        fun setStateLayout(state: Int, @LayoutRes layoutResId: Int) {
            commonViews[state] = layoutResId
        }

    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 内容布局
     */
    private var vContent: View? = null

    /**
     * 状态布局容器
     */
    private val views = mutableMapOf<Any, View>()

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        //动画,防止动画报错
//        val transition = LayoutTransition()
//        transition.setAnimator(LayoutTransition.APPEARING,
//            ObjectAnimator.ofFloat(null, View.ALPHA, 0f, 1f))
//        transition.setAnimator(LayoutTransition.DISAPPEARING,
//            ObjectAnimator.ofFloat(null, View.ALPHA, 1f, 0f))
//        layoutTransition = transition
    }

    /**
     * 显示加载中
     */
    inline fun showStateLoading(@LayoutRes layoutResId: Int?): View {
        val layout = layoutResId ?: commonViews[STATE_LOADING]
        return showStateView(layout!!) {
            LayoutInflater.from(context).inflate(layout, this, false)
        }
    }

    /**
     * 显示加载错误
     */
    inline fun showStateError(@LayoutRes layoutResId: Int?): View {
        val layout = layoutResId ?: commonViews[STATE_ERROR]
        return showStateView(layout!!) {
            LayoutInflater.from(context).inflate(layout, this, false)
        }
    }

    /**
     * 显示完成所有数据
     */
    inline fun showStateComplete(@LayoutRes layoutResId: Int?): View {
        val layout = layoutResId ?: commonViews[STATE_COMPLETE]
        return showStateView(layout!!) {
            LayoutInflater.from(context).inflate(layout, this, false)
        }
    }

    /**
     * 显示状态布局通用逻辑
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @PublishedApi
    internal fun showStateView(key: Any, create: () -> View): View {

        visibility = View.VISIBLE
        vContent?.visibility = View.GONE

        // 获取或者创建对应状态布局
        val newView = views.getOrPut(key, create) //需要显示的状态布局
        val oldView = if (childCount != 0) getChildAt(0) else null//之前显示的布局


        if (newView != oldView) { // 当前布局和需要显示的布局不相同,则切换显示布局
            if (vContent?.parent == this) { //内容布局的父布局就是当前控件
                removeViews(1, childCount - 1) //
            } else {
                removeAllViews()
            }
            addView(newView) // 添加新view
        }
        return newView
    }

    /**
     * view加载完xml之后执行的方法，相当于只是完成了布局的映射
     * 在setContentView之后、onMeasure之前
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 0) {
            return
        }
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        vContent = getChildAt(0)
    }

}