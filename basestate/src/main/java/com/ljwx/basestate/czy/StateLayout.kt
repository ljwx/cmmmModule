package com.ljwx.basestate.czy

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.constant.BaseLayoutStatus
import com.ljwx.baseapp.constant.BaseLayoutStatus.LayoutStatus
import com.ljwx.baseapp.view.IViewStateLayout
import com.ljwx.basestate.R

@Suppress("UNCHECKED_CAST")
open class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), IViewStateLayout {

    private var vContent: View? = null

    private val views = mutableMapOf<Any, View>()

    /** 错误页面布局 */
    @LayoutRes
    var errorLayout: Int = NO_ID
        get() = if (field == NO_ID) StateConfig.errorLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 空页面布局 */
    @LayoutRes
    var emptyLayout: Int = NO_ID
        get() = if (field == NO_ID) StateConfig.emptyLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 加载中页面布局 */
    @LayoutRes
    var loadingLayout: Int = NO_ID
        get() = if (field == NO_ID) StateConfig.loadingLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 加载中页面布局 */
    @LayoutRes
    var offlineLayout: Int = NO_ID
        get() = if (field == NO_ID) StateConfig.offlineLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    /** 加载中页面布局 */
    @LayoutRes
    var extendLayout: Int = NO_ID
        get() = if (field == NO_ID) StateConfig.extendLayout else field
        set(value) {
            if (field != value) {
                field = value
            }
        }

    init {
        //viewpager切换的时候可能会导致崩溃
        //java.lang.IllegalStateException: Page(s) contain a ViewGroup with a LayoutTransition (or animateLayoutChanges="true"), which interferes with the scrolling animation. Make sure to call getLayoutTransition().setAnimateParentHierarchy(false) on all ViewGroups with a LayoutTransition before an animation is started.
//        val transition = LayoutTransition()
//
//        transition.setAnimator(
//            LayoutTransition.APPEARING,
//            ObjectAnimator.ofFloat(null, View.ALPHA, 0f, 1f)
//        )
//        transition.setAnimator(
//            LayoutTransition.DISAPPEARING,
//            ObjectAnimator.ofFloat(null, View.ALPHA, 1f, 0f)
//        )
//
//        layoutTransition = transition

        if (layoutParams == null) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
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

        if (id == NO_ID) {
            id = com.ljwx.baseapp.R.id.base_app_quick_state_layout
        }

    }

    inline fun <reified V : View> showStateView(key: Any = V::class.java): V? {
        return showStateView(V::class.java, key)
    }

    fun <V : View> showStateView(clazz: Class<V>, key: Any = clazz): V? {
        return showStateView(getOrCreate(key) {
            clazz.getConstructor(Context::class.java).newInstance(context)
        }) as? V
    }

    fun <V : View> showStateView(@LayoutRes layoutResId: Int, key: Any = layoutResId): V? {
        return showStateView(getOrCreate(key) {
            LayoutInflater.from(context).inflate(layoutResId, this, false)
        }) as? V
    }

    private fun getOrCreate(key: Any, create: () -> View): View {
        return views.getOrPut(key, create)
    }

    private fun showStateView(stateView: View): View {


        visibility = View.VISIBLE
        vContent?.visibility = View.GONE

        val newView = stateView
        val oldView = if (childCount != 0) getChildAt(0) else null


        if (newView != oldView) {
            if (vContent?.parent == this) {
                removeViews(1, childCount - 1)
            } else {
                removeAllViews()
            }
            addView(newView)
        }
        return newView
    }

    override fun setStateView(@LayoutStatus state: Int, view: View) {
        views.put(state, view)
    }

    override fun showStateView(state: Int, view: View?, tag: Any?) {
        var layout: Int? = null
        when (state) {
            BaseLayoutStatus.EMPTY -> layout = emptyLayout
            BaseLayoutStatus.LOADING -> layout = loadingLayout
            BaseLayoutStatus.ERROR -> layout = errorLayout
            BaseLayoutStatus.OFFLINE -> layout = offlineLayout
            BaseLayoutStatus.EXTEND -> layout = extendLayout
        }
        if (layout == null) {
            showStateContent()
        } else {
            getOrCreate(state) {
                LayoutInflater.from(context).inflate(layout, this, false)
            }.let { showStateView(it) }
        }
    }

    override fun getView(): ViewGroup {
        return this
    }

    override fun addClickListener(
        @LayoutStatus state: Int,
        id: Int,
        listener: OnClickListener
    ) {
        var layout: Int? = null
        when (state) {
            BaseLayoutStatus.EMPTY -> layout = emptyLayout
            BaseLayoutStatus.LOADING -> layout = loadingLayout
            BaseLayoutStatus.ERROR -> layout = errorLayout
            BaseLayoutStatus.OFFLINE -> layout = offlineLayout
            BaseLayoutStatus.EXTEND -> layout = extendLayout
        }
        if (layout != null) {
            getOrCreate(state) {
                LayoutInflater.from(context).inflate(layout, this, false)
            }.findViewById<View>(id)?.setOnClickListener(listener)
        }
    }

    fun showStateContent() {
        if (vContent?.parent == this) {
            removeViews(1, childCount - 1)
        } else {
            removeAllViews()
            if (vContent?.parent == null) {
                addView(vContent)
            } else {
                visibility = View.GONE
            }
        }
        vContent?.visibility = View.VISIBLE
    }

    open fun setStateContent(view: View) {
        if (vContent?.parent == this) {
            removeView(vContent)
        }
        vContent = view
    }

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