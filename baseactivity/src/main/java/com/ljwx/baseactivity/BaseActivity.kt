package com.ljwx.baseactivity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ljwx.baseactivity.statusbar.BaseStatusBar
import com.ljwx.baseapp.constant.BaseConstBundleKey
import com.ljwx.baseapp.constant.BaseLogTag
import com.ljwx.baseapp.keyboard.KeyboardHeightProvider
import com.ljwx.baseapp.page.IPageActivity
import com.ljwx.baseapp.page.IPageDialogTips
import com.ljwx.baseapp.page.IPageKeyboardHeight
import com.ljwx.baseapp.page.IPageLocalEvent
import com.ljwx.baseapp.page.IPageProcessStep
import com.ljwx.baseapp.page.IPageStartPage
import com.ljwx.baseapp.page.IPageStatusBar
import com.ljwx.baseapp.page.IPageToolbar
import com.ljwx.baseapp.router.IPostcard
import com.ljwx.baseapp.util.BaseModuleLog
import com.ljwx.baseapp.util.LocalEventUtils
import com.ljwx.baseapp.view.IViewStatusBar
import com.ljwx.basedialog.common.BaseDialogBuilder
import com.ljwx.router.RouterPostcard

open class BaseActivity(@LayoutRes private val layoutResID: Int = com.ljwx.baseapp.R.layout.baseapp_state_layout_empty) :
    BaseToolsActivity(), IPageStatusBar, IPageToolbar, IPageLocalEvent,
    IPageDialogTips, IPageProcessStep, IPageActivity, IPageStartPage, IPageKeyboardHeight {

    open val TAG = this.javaClass.simpleName + BaseLogTag.ACTIVITY

    /**
     * 键盘
     */
    protected var mScreenHeight = -1//辅助计算键盘高度

    protected var keyboardHighProvider: KeyboardHeightProvider? = null

    private var hidePopBottom = 0

    private val mStatusBar by lazy { BaseStatusBar(this) }

    private var mStateSaved = false

    private var broadcastReceivers: HashMap<String, BroadcastReceiver>? = null

    private var onBackPressInterceptors: (ArrayList<() -> Boolean>)? = null

    protected val argumentsFromType by lazy {
        intent.getIntExtra(BaseConstBundleKey.FROM_TYPE, -10)
    }

    protected val argumentsDataId by lazy { intent.getStringExtra(BaseConstBundleKey.DATA_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseModuleLog.d(TAG, "生命周期onCreate")
        setStatusBarLight(true)
        requestedOrientation = getScreenOrientation()
        if (enableKeyboardHeightListener()) {
            createKeyboardHeightProvider()
            keyboardHeightRootView()?.post { keyboardHighProvider?.start() }
        }
    }

    open fun getLayoutRes(): Int {
        return layoutResID
    }

    /**
     * 路由快速跳转
     */
    open fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    override fun startActivity(clazz: Class<*>, requestCode: Int?) {
        if (requestCode == null) {
            startActivity(Intent(this, clazz))
        } else {
            startActivityForResult(Intent(this, clazz), requestCode)
        }
    }

    override fun routerTo(path: String): IPostcard {
        BaseModuleLog.d(TAG, "路由跳转到:$path")
        return RouterPostcard(path)
    }

    override fun getScreenOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun getStatusBar(): IViewStatusBar {
        return mStatusBar;
    }

    override fun setStatusBar(backgroundColor: Int, fontDark: Boolean): IViewStatusBar {
        return mStatusBar.setCustomStatusBar(backgroundColor, fontDark)
    }

    override fun setStatusBarLight(light: Boolean) {
        if (light) {
            mStatusBar.setCustomStatusBar(com.ljwx.baseapp.R.color.white, true)
        } else {
            mStatusBar.setCustomStatusBar(
                com.ljwx.baseapp.R.color.base_app_textColorSecondary,
                false
            )
        }
    }

    override fun setStatusBarTransparent(transparent: Boolean) {
        if (transparent) {
            mStatusBar.transparent(transparent)
        }
    }

    override fun initToolbar(toolbarId: Int): Toolbar? {
        BaseModuleLog.d(TAG, "通过id初始化toolbar")
        val toolbar = findViewById(toolbarId) as? Toolbar
        return setToolbar(toolbar)
    }

    override fun initToolbar(toolbar: Toolbar?): Toolbar? {
        BaseModuleLog.d(TAG, "通过Toolbar控件初始化toolbar")
        return setToolbar(toolbar)
    }

    private fun setToolbar(toolbar: Toolbar?): Toolbar? {
        toolbar?.let {
            BaseModuleLog.d(TAG, "设置Toolbar返回")
            setSupportActionBar(toolbar)
            toolbar?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
        return toolbar
    }

    override fun setToolbarTitle(title: CharSequence) {
        supportActionBar?.title = title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        BaseModuleLog.d(TAG, "生命周期onSaveInstanceState")
        mStateSaved = true
    }

    override fun onResume() {
        super.onResume()
        BaseModuleLog.d(TAG, "生命周期onResume")
        mStateSaved = false
        if (enableKeyboardHeightListener()) {
            setKeyboardHeightListener()
        }
    }

    override fun onStop() {
        super.onStop()
        BaseModuleLog.d(TAG, "生命周期onStop")
        mStateSaved = true
    }

    override fun onStart() {
        super.onStart()
        BaseModuleLog.d(TAG, "生命周期onStart")
        mStateSaved = false
    }

    override fun showDialogTips(
        title: String?,
        content: String?,
        positiveText: String?
    ): Dialog? {
        BaseModuleLog.d(TAG, "快速显示弹窗")
        return showDialogTips(title, content, positiveText, null, null, null, false, null, null)
    }


    /**
     * 快速显示dialog提示
     *
     * @param title 标题,为空不显示标题控件
     * @param content 内容
     * @param positiveText 积极的文案
     * @param positiveListener 积极的点击, 当文案和点击都为空,则不显示积极控件
     * @param negativeText 消极的文案,为空,则不显示消极控件
     */
    override fun showDialogTips(
        title: String?,
        content: String?,
        positiveText: String?,
        negativeText: String?,
        showClose: Boolean?,
        tag: String?,
        reversalButtons: Boolean,
        negativeListener: View.OnClickListener?,
        positiveListener: View.OnClickListener?
    ): Dialog? {
        BaseModuleLog.d(TAG, "快速显示弹窗")
//        if (tag.notNullOrBlank()) {
//            val cache = supportFragmentManager.findFragmentByTag(tag)
//            if (cache != null && cache is BaseDialogFragment) {
//                //报java.lang.IllegalStateException: Fragment already added
////                cache.show(supportFragmentManager, tag)
//                Log2.d(TAG, "$tag,dialog有缓存")
//                return cache.getBuilder()
//            }
//        }
        val builder = BaseDialogBuilder()
        builder.apply {
            showCloseIcon(showClose)
            if (title != null) {
                setTitle(title)
            }
            setContent(content)
            //是否显示确定等
            if (positiveText != null || positiveListener != null) {
                setPositiveButton(positiveText, positiveListener)
            }
            //是否显示取消等
            if (negativeText != null || negativeListener != null) {
                setNegativeButton(negativeText, negativeListener)
            }
            //是否反转按钮
            buttonsReversal(reversalButtons)
            return this.showDialog(this@BaseActivity)
        }
    }

    /**
     * 事件广播使用
     */
    override fun registerLocalEvent(
        action: String?,
        observer: (action: String, type: Long?, intent: Intent) -> Unit
    ) {
        if (action == null) {
            return
        }
        val intentFilter = IntentFilter(action)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                intent.action?.let {
                    BaseModuleLog.d(TAG, "接收到事件广播:$it")
                    if (intentFilter.matchAction(it)) {
                        val type =
                            intent.getLongExtra(BaseConstBundleKey.LOCAL_EVENT_COMMON_TYPE, -1)
                        observer(action, type, intent)
                    }
                }
            }
        }
        broadcastReceivers = broadcastReceivers ?: HashMap()
        broadcastReceivers?.put(action, receiver)
        BaseModuleLog.d(TAG, "注册事件广播:$action")
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun sendLocalEvent(action: String?, type: Long?) {
        LocalEventUtils.sendAction(action, type)
    }

    override fun unregisterLocalEvent(action: String?) {
        action?.let {
            broadcastReceivers?.get(it)?.let {
                BaseModuleLog.d(TAG, "注销事件广播:$action")
                LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
            }
            broadcastReceivers?.remove(it)
        }
    }

    fun addBackPressedInterceptor(block: () -> Boolean) {
        onBackPressInterceptors = onBackPressInterceptors ?: ArrayList()
        onBackPressInterceptors?.add(block)
    }

    override fun onBackPressed() {
        BaseModuleLog.d(TAG, "触发onBackPress")
        onBackPressInterceptors?.forEach {
            if (it.invoke()) {
                BaseModuleLog.d(TAG, "返回被拦截")
                return
            }
        }
        super.onBackPressed()
    }


    override fun commonProcessSteps() {
        getFirstInitData()
        initUIView()
        observeData()
        setClickListener()
        getAsyncData()
    }

    override fun getFirstInitData() {

    }

    override fun initUIView() {

    }

    override fun observeData() {

    }

    override fun setClickListener() {

    }

    override fun getAsyncData(refresh: Boolean) {

    }

    /*---------------------------------------------------------------------------------------*/


    override fun enableKeyboardHeightListener(): Boolean = false

    override fun createKeyboardHeightProvider() {
        keyboardHighProvider = keyboardHighProvider ?: KeyboardHeightProvider(this)
    }

    override fun keyboardHeightRootView(): View? = rootLayout

    override fun setKeyboardHeightListener() {
        keyboardHighProvider?.setKeyboardHeightListener { height, orientation ->
            var keyBoardHeight = 0
            if (mScreenHeight <= 0) {
                hidePopBottom = height
            } else {
                if (keyBoardHeight <= 0 && height > hidePopBottom && height - hidePopBottom > mScreenHeight / 4) {
                    keyBoardHeight = height - hidePopBottom
                }
                if (keyBoardHeight > mScreenHeight * 3 / 5) {
                    keyBoardHeight = height - hidePopBottom
                }
            }
            if (mScreenHeight <= 0) {
                mScreenHeight = keyboardHeightRootView()?.getHeight() ?: 2000
            }
            if (isKeyboardShow(height, hidePopBottom)) { //软键盘弹出
                onKeyboardHeightChange(true, keyBoardHeight)
            } else {
                onKeyboardHeightChange(false, 0)
            }
        }
    }

    override fun isKeyboardShow(height: Int, buffHeight: Int): Boolean {
        return height - buffHeight > mScreenHeight / 4
    }

    override fun onKeyboardHeightChange(show: Boolean, height: Int) {

    }

    override fun onPause() {
        super.onPause()
        BaseModuleLog.d(TAG, "生命周期onPause")
        keyboardHighProvider?.setKeyboardHeightListener(null)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        BaseModuleLog.d(TAG, "生命周期onConfigurationChanged")
    }

    override fun onRestart() {
        super.onRestart()
        BaseModuleLog.d(TAG, "生命周期onRestart")
    }

    inline fun <reified F : Fragment> fragmentInstance(fromType: Int): F? {
        return fragmentInstanceEx(fromType)
    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        super.overridePendingTransition(enterAnim, exitAnim)
//        overridePendingTransition(0, R.anim.bottom_out)
        BaseModuleLog.d(TAG, "进出动画")
    }

    override fun onDestroy() {
        super.onDestroy()
        BaseModuleLog.d(TAG, "生命周期onDestroy")
        broadcastReceivers?.keys?.toList()?.forEach {
            unregisterLocalEvent(it)
        }
        keyboardHighProvider?.close()
    }

}