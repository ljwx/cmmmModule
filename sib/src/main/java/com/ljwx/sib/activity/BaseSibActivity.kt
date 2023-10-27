package com.ljwx.sib.activity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ljwx.baseapp.page.IPageActivity
import com.ljwx.baseapp.page.IPageLocalEvent
import com.ljwx.baseapp.page.IPageDialogTips
import com.ljwx.baseapp.page.IPageProcessStep
import com.ljwx.baseapp.page.IPageStartPage
import com.ljwx.baseapp.page.IPageStatusBar
import com.ljwx.baseapp.page.IPageToolbar
import com.ljwx.baseapp.router.IPostcard
import com.ljwx.baseapp.view.IViewStatusBar
import com.ljwx.basedialog.common.BaseDialogBuilder
import com.ljwx.router.Postcard
import com.ljwx.sib.Log2
import com.ljwx.sib.activity.statusbar.BaseStatusBar
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

open class BaseSibActivity : RxAppCompatActivity(), IPageStatusBar, IPageToolbar, IPageLocalEvent,
    IPageDialogTips, IPageProcessStep, IPageActivity, IPageStartPage {

    open val TAG = this.javaClass.simpleName + "[Activity]"

    open val userNewBaseActivityLogic = false

    private val mStatusBar by lazy {
        BaseStatusBar(this)
    }

    private var mStateSaved = false

    private val broadcastReceivers by lazy {
        HashMap<String, BroadcastReceiver>()
    }

    private var onBackPressInterceptors: (ArrayList<() -> Boolean>)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = getScreenOrientation()
        if (userNewBaseActivityLogic) {
            setStatusBar()
        }
    }

    /**
     * 路由快速跳转
     */
    override fun startActivity(clazz: Class<*>, requestCode:Int?) {
        if (requestCode == null) {
            startActivity(Intent(this, clazz))
        } else {
            startActivityForResult(Intent(this, clazz), requestCode)
        }
    }

    override fun routerTo(path: String): IPostcard {
        Log2.d(TAG, "路由跳转到:$path")
        return Postcard(path)
    }

    override fun getScreenOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun getStatusBar(): IViewStatusBar {
        return mStatusBar;
    }

    override fun setStatusBarLight(light: Boolean) {
        if (light) {
            mStatusBar.setCustomStatusBar(com.ljwx.baseapp.R.color.white, true)
        } else {
            mStatusBar.setCustomStatusBar(com.ljwx.baseapp.R.color.base_app_textColorSecondary, false)
        }
    }

    override fun setStatusBar(backgroundColor: Int, fontDark: Boolean): IViewStatusBar {
        return mStatusBar.setCustomStatusBar(backgroundColor, fontDark)
    }

    override fun initToolbar(toolbarId: Int): Toolbar? {
        Log2.d(TAG, "通过id初始化toolbar")
        val toolbar = findViewById(toolbarId) as? Toolbar
        return setToolbar(toolbar)
    }

    override fun initToolbar(toolbar: Toolbar?): Toolbar? {
        Log2.d(TAG, "通过Toolbar控件初始化toolbar")
        return setToolbar(toolbar)
    }

    private fun setToolbar(toolbar: Toolbar?): Toolbar? {
        toolbar?.let {
            Log2.d(TAG, "设置Toolbar返回")
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
        mStateSaved = true
    }

    override fun onResume() {
        super.onResume()
        mStateSaved = false
    }

    override fun onStop() {
        super.onStop()
        mStateSaved = true
    }

    override fun onStart() {
        super.onStart()
        mStateSaved = false
    }

    override fun showDialogTips(
        title: String?,
        content: String?,
        positiveText: String?
    ): Dialog? {
        Log2.d(TAG, "快速显示Dialog提示弹窗,精简")
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
        Log2.d(TAG, "快速显示Dialog提示弹窗,详细")
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
            return this.showDialog(this@BaseSibActivity)
        }
    }

    /**
     * 事件广播使用
     */
    override fun registerLocalEvent(
        action: String?,
        observer: (action: String, intent: Intent) -> Unit
    ) {
        if (action == null) {
            return
        }
        val intentFilter = IntentFilter(action)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                intent.action?.let {
                    Log2.d(TAG, "接收到事件广播:$it")
                    if (intentFilter.matchAction(it)) {
                        observer(action, intent)
                    }
                }
            }
        }
        broadcastReceivers[action] = receiver
        Log2.d(TAG, "注册事件广播:$action")
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun sendLocalEvent(action: String?) {
        if (action == null) {
            return
        }
        Log2.d(TAG, "发送事件广播:$action")
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(action))
    }

    override fun unregisterLocalEvent(action: String?) {
        action?.let {
            broadcastReceivers[it]?.let {
                Log2.d(TAG, "注销事件广播:$action")
                LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
            }
            broadcastReceivers.remove(it)
        }
    }

    fun addBackPressedInterceptor(block: () -> Boolean) {
        onBackPressInterceptors = onBackPressInterceptors ?: ArrayList()
        onBackPressInterceptors?.add(block)
    }

    override fun onBackPressed() {
        Log2.d(TAG, "返回是否需要拦截")
        onBackPressInterceptors?.forEach {
            if (it.invoke()) {
                Log2.d(TAG, "返回被拦截")
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

    override fun getAsyncData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        Log2.d(TAG, "执行onDestroy")
        broadcastReceivers.keys.toList().forEach {
            unregisterLocalEvent(it)
        }
    }

}