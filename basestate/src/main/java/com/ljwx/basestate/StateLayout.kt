package com.ljwx.basestate

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.view.IViewStateLayout

open class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), IViewStateLayout {

    private var contentView: View? = null

    private val stateViews = mutableMapOf<Any, View>()

    @LayoutRes
    var errorLayout: Int = NO_ID
        get() = if (field == NO_ID) StateLayoutConfig.errorLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 空页面布局 */
    @LayoutRes
    var emptyLayout: Int = NO_ID
        get() = if (field == NO_ID) StateLayoutConfig.emptyLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 加载中页面布局 */
    @LayoutRes
    var loadingLayout: Int = NO_ID
        get() = if (field == NO_ID) StateLayoutConfig.loadingLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 加载中页面布局 */
    @LayoutRes
    var offlineLayout: Int = NO_ID
        get() = if (field == NO_ID) StateLayoutConfig.offlineLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 加载中页面布局 */
    @LayoutRes
    var extendLayout: Int = NO_ID
        get() = if (field == NO_ID) StateLayoutConfig.extendLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    init {
        if (layoutParams == null) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        if (id == NO_ID) {
            id = com.ljwx.baseapp.R.id.base_app_quick_state_layout
        }
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        try {
            emptyLayout = attributes.getResourceId(R.styleable.StateLayout_stateLayoutEmpty, NO_ID)
            errorLayout = attributes.getResourceId(R.styleable.StateLayout_stateLayoutError, NO_ID)
            loadingLayout =
                attributes.getResourceId(R.styleable.StateLayout_stateLayoutLoading, NO_ID)
            offlineLayout =
                attributes.getResourceId(R.styleable.StateLayout_stateLayoutOffline, NO_ID)
            extendLayout =
                attributes.getResourceId(R.styleable.StateLayout_stateLayoutExtend, NO_ID)
        } finally {
            attributes.recycle()
        }
    }

    override fun setStateView(state: Int, view: View) {

    }

    override fun showStateView(state: Int, tag: Any?) {

    }

    override fun getView(): ViewGroup {
        return this
    }

    override fun addClickListener(state: Int, id: Int, listener: OnClickListener) {

    }

    fun showStateContent() {
        if (contentView?.parent == this) {
            removeViews(1, childCount - 1)
        } else {
            removeAllViews()
            if (contentView?.parent == null) {
                addView(contentView)
            } else {
                visibility = View.GONE
            }
        }
        contentView?.visibility = View.VISIBLE
    }

    open fun setStateContent(view: View) {
        if (contentView?.parent == this) {
            removeView(contentView)
        }
        contentView = view
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 0) {
            return
        }
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        contentView = getChildAt(0)
    }

}