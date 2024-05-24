package com.ljwx.basestate

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.constant.BaseLayoutStatus
import com.ljwx.baseapp.constant.BaseLogTag
import com.ljwx.baseapp.util.BaseModuleLog
import com.ljwx.baseapp.view.IViewStateLayout

open class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), IViewStateLayout {

    private var contentView: View? = null

    private val stateViews = mutableMapOf<Int, View>()

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

    private fun createLayoutView(
        @BaseLayoutStatus.LayoutStatus state: Int,
        @LayoutRes layout: Int
    ): View {
        val view = LayoutInflater.from(context).inflate(layout, this, false)
        stateViews[state] = view
        return view
    }

    private fun getStateView(@BaseLayoutStatus.LayoutStatus state: Int): View? {
        var view = stateViews[state]
        if (view == null) {
            var layout: Int? = null
            when (state) {
                BaseLayoutStatus.CONTENT -> return contentView
                BaseLayoutStatus.EMPTY -> layout = emptyLayout
                BaseLayoutStatus.LOADING -> layout = loadingLayout
                BaseLayoutStatus.ERROR -> layout = errorLayout
                BaseLayoutStatus.OFFLINE -> layout = offlineLayout
                BaseLayoutStatus.EXTEND -> layout = extendLayout
            }
            layout?.let { view = view ?: createLayoutView(state, layout) }
        }
        return view
    }

    override fun setStateView(state: Int, view: View) {
        stateViews[state] = view
    }

    override fun showStateView(state: Int, view: View?, tag: Any?) {
        if (state == BaseLayoutStatus.CONTENT) {
            showStateContent()
            return
        }
        visibility = View.VISIBLE
        contentView?.visibility = View.GONE

        val newView = getStateView(state)
        val oldView = if (childCount != 0) getChildAt(0) else null

        if (newView != oldView) {
            if (contentView?.parent == this) {
                removeViews(1, childCount - 1)
            } else {
                removeAllViews()
            }
            BaseModuleLog.d(BaseLogTag.STATE_LAYOUT, "showStateView,newView:$newView")
            addView(newView)
        }
    }

    override fun getView(): ViewGroup {
        return this
    }

    override fun addClickListener(state: Int, id: Int, listener: OnClickListener) {
        getStateView(state)?.findViewById<View>(id)?.setOnClickListener(listener)
    }

    fun showStateContent() {
        BaseModuleLog.d(BaseLogTag.STATE_LAYOUT, "showStateContent")
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
        BaseModuleLog.d(BaseLogTag.STATE_LAYOUT, "setContentView,contentView:$contentView")
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
        BaseModuleLog.d(BaseLogTag.STATE_LAYOUT, "onFinishInflate,contentView:$contentView")
    }

}