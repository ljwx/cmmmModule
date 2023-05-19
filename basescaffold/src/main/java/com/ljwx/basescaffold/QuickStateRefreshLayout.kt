package com.ljwx.basescaffold

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import com.drake.statelayout.StateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 多状态及刷新控件快捷使用
 *
 * @author ljwx
 * @since 2022-05-20
 */
open class QuickStateRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val TAG = this.javaClass.simpleName

    /**
     * 多状态布局
     */
    private var mStateLayout: StateLayout? = null

    /**
     * 刷新布局
     */
    private var mRefreshLayout: SpecialRefreshLayout? = null

    /**
     * 是否需要刷新或多状态
     */
    private var mShowModel = 0
    private val MODEL_NONE = 0
    private val MODEL_JUST_STATE = 1
    private val MODEL_JUST_REFRESH = 2
    private val MODEL_STATE_REFRESH = 3
    private val MODEL_REFRESH_STATE = 4

    /**
     * 自定义多状态布局
     */
    private var mLayoutLoading: Int? = null
    private var mLayoutEmpty: Int? = null
    private var mLayoutError: Int? = null
    private var mLayoutNetwork: Int? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.QuickStateRefreshLayout)
        try {
            mShowModel = attr.getInteger(R.styleable.QuickStateRefreshLayout_qsrl_layout_model,
                MODEL_JUST_STATE)
            getAttrLayoutId(attr)
        } finally {
            attr.recycle()
        }
        // 设置默认id
        id = R.id.base_scaffold_quick_state_refresh_layout
    }

    private fun getAttrLayoutId(attr: TypedArray) {
        if (mShowModel != MODEL_JUST_REFRESH && mShowModel != MODEL_JUST_STATE) {
            mLayoutLoading =
                attr.getResourceId(R.styleable.QuickStateRefreshLayout_qsrl_layout_loading, NO_ID)
            mLayoutEmpty =
                attr.getResourceId(R.styleable.QuickStateRefreshLayout_qsrl_layout_empty, NO_ID)
            mLayoutError =
                attr.getResourceId(R.styleable.QuickStateRefreshLayout_qsrl_layout_error, NO_ID)
            mLayoutNetwork =
                attr.getResourceId(R.styleable.QuickStateRefreshLayout_qsrl_layout_network, NO_ID)
        }
    }

    /**
     * 获取内容布局
     *
     * @return 内容布局
     */
    private fun getContentView(): View? {
        var contentChildIndex = 0
        // 是否包含toolbar
        if (childCount > 1 && getChildAt(0) is Toolbar) {
            contentChildIndex = 1
        }
        // 防止越界
        if (contentChildIndex >= childCount) {
            return null
        }
        // 将内容布局添加到多状态布局中
        val contentView = getChildAt(contentChildIndex)
        return contentView
    }


    /**
     * 添加父布局
     */
    private fun addParentView() {
        getContentView()?.let { content ->
            // 最外层布局
            val root = mShowModel == MODEL_JUST_REFRESH || mShowModel == MODEL_REFRESH_STATE
            addRootContainer(root, content)
            // 二级布局
            val child = mShowModel == MODEL_STATE_REFRESH || mShowModel == MODEL_REFRESH_STATE
            if (child) {
                val childIsRefresh = mShowModel == MODEL_REFRESH_STATE
                addChildContainer(childIsRefresh)
                addContentView(!childIsRefresh, content)
            } else {
                // 没有二级布局
                addContentView(mShowModel == MODEL_JUST_REFRESH, content)
            }
        }
    }

    /**
     * 添加最外层布局
     *
     * @param rootRefresh 最外层是否是刷新布局
     * @param contentParams 内容布局的大小
     */
    private fun addRootContainer(refreshLayout: Boolean, contentView: View) {

        // 将内容布局从当前布局移除
        removeView(contentView)

        if (refreshLayout) {
            createRefreshLayout()
            addView(mRefreshLayout, contentView.layoutParams)
        } else {
            createStateLayout()
            addView(mStateLayout, contentView.layoutParams)
        }
    }

    /**
     * 添加二级父布局
     *
     * @param rootRefresh 二级父布局是否是刷新布局
     */
    private fun addChildContainer(rootRefresh: Boolean) {
        if (rootRefresh) {
            createStateLayout()
            refreshLayoutSetContent(mStateLayout)
        } else {
            createRefreshLayout()
            stateLayoutSetContent(mRefreshLayout)
        }
    }

    /**
     * 添加内容布局
     *
     * @param parentIsRefresh 父布局是否是刷新布局
     * @param content 内容布局
     */
    private fun addContentView(parentIsRefresh: Boolean, content: View) {
        if (parentIsRefresh) {
            refreshLayoutSetContent(content)
        } else {
            stateLayoutSetContent(content)
        }
    }

    /**
     * 多状态布局设置内容布局
     *
     * @param content 内容布局
     */
    private fun stateLayoutSetContent(content: View?) {
        content?.let {
            mStateLayout?.setContent(it)
            if (it.parent == null){
                mStateLayout?.addView(it, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }
        }
    }

    /**
     * 刷新布局设置内容布局
     *
     * @param content 内容布局
     */
    private fun refreshLayoutSetContent(content: View?) {
        content?.let {
            mRefreshLayout?.addView(content, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            mRefreshLayout?.onFinishInflate()
        }
    }

    /**
     * 创建多状态布局
     */
    private fun createStateLayout() {
        // 创建多状态布局
        mStateLayout = mStateLayout ?: StateLayout(context)
        mStateLayout?.id = com.ljwx.baseapp.R.id.base_app_quick_state_layout
        mStateLayout?.setRetryIds(R.id.base_scaffold_state_retry_id)
        if (mLayoutLoading != null && mLayoutLoading != NO_ID) {
            mStateLayout?.loadingLayout = mLayoutLoading!!
        }
        if (mLayoutEmpty != null && mLayoutEmpty != NO_ID) {
            mStateLayout?.emptyLayout = mLayoutEmpty!!
        }
        if (mLayoutError != null && mLayoutError != NO_ID) {
            mStateLayout?.errorLayout = mLayoutError!!
        }
    }

    /**
     * 创建刷新布局
     */
    private fun createRefreshLayout() {
        // 创建刷新布局
        mRefreshLayout = mRefreshLayout ?: SpecialRefreshLayout(context)
        mRefreshLayout?.id = com.ljwx.baseapp.R.id.base_app_quick_refresh_layout
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mShowModel != MODEL_NONE) {
            if (childCount < 1) {
                throw UnsupportedOperationException("$TAG less than one child view")
            }
            addParentView()
        }
    }

    /**
     * 获取多状态布局类
     */
    fun getStateLayout(): StateLayout? {
        return mStateLayout
    }

    /**
     * 显示内容布局
     */
    fun showStateContent() {
        mStateLayout?.showContent()
    }

    /**
     * 显示空布局
     */
    fun showStateEmpty() {
        mStateLayout?.showEmpty()
    }

    /**
     * 显示加载中布局
     */
    fun showStateLoading() {
        mStateLayout?.showLoading()
    }

    /**
     * 显示错误布局
     */
    fun showStateError() {
        mStateLayout?.showError()
    }

    /**
     * 设置重试的控件id
     */
    fun setStateRetryId(@IdRes id: Int) {
        mStateLayout?.setRetryIds(id)
    }

    /**
     * showLoading时会回调 常见处理的是加载视图/动画
     *
     * @param block 回调执行的逻辑
     */
    private fun onStateLoading(block: View.(tag: Any?) -> Unit) {
        mStateLayout?.onLoading(block)
    }

    /**
     * showLoading时会回调 一般在其中执行加载网络或异步任务的逻辑, 而不是加载视图
     *
     * @param block 回调执行的逻辑
     */
    fun onStateRefresh(block: View.(tag: Any?) -> Unit) {
        mStateLayout?.onRefresh(block)
    }

    fun onPullRefresh() {

    }

    private inner class SpecialRefreshLayout : SmartRefreshLayout {

        constructor(context: Context) : this(context, null)

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

        public override fun onFinishInflate() {
            super.onFinishInflate()
        }
    }

}