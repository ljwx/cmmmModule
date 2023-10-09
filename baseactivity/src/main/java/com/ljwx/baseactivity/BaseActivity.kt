package com.ljwx.baseactivity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ljwx.baseactivity.statusbar.BaseStatusBar
import com.ljwx.baseapp.page.IPageBroadcast
import com.ljwx.baseapp.page.IPageDialogTips
import com.ljwx.baseapp.page.IPageStatusBar
import com.ljwx.baseapp.page.IPageToolbar
import com.ljwx.baseapp.view.IViewStatusBar
import com.ljwx.basedialog.common.BaseDialogBuilder

open class BaseActivity : AppCompatActivity(), IPageStatusBar, IPageToolbar, IPageBroadcast,
    IPageDialogTips {

    open val TAG = this.javaClass.simpleName

    private val mStatusBar by lazy {
        BaseStatusBar(this)
    }

    private var mStateSaved = false

    /**
     * 结束广播
     */
    private var mFinishReceiver: BroadcastReceiver? = null

    /**
     * 刷新广播
     */
    private var mRefreshReceiver: BroadcastReceiver? = null

    private var mOtherReceiver: BroadcastReceiver? = null

    /**
     * 注册广播
     */
    private var mBroadcastIntentFilter: IntentFilter? = null

    private var onBackPressInterceptors: (ArrayList<() -> Boolean>)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        requestedOrientation = getScreenOrientation()
    }

    open fun getScreenOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun getStatusBar(): IViewStatusBar {
        return mStatusBar;
    }

    override fun setStatusBar(backgroundColor: Int, fontDark: Boolean): IViewStatusBar {
        return mStatusBar.setCustomStatusBar(backgroundColor, fontDark)
    }

    override fun initToolbar(toolbarId: Int): Toolbar? {
        val toolbar = findViewById(toolbarId) as? Toolbar
        return setToolbar(toolbar)
    }

    override fun initToolbar(toolbar: Toolbar?): Toolbar? {
        return setToolbar(toolbar)
    }

    private fun setToolbar(toolbar: Toolbar?): Toolbar? {
        toolbar?.let {
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
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
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
        return showDialogTips(title, content, positiveText, null, null, null, null, null)
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
        negativeListener: View.OnClickListener?,
        positiveListener: View.OnClickListener?
    ): Dialog? {
//        if (tag.notNullOrBlank()) {
//            val cache = supportFragmentManager.findFragmentByTag(tag)
//            if (cache != null && cache is BaseDialogFragment) {
//                //报java.lang.IllegalStateException: Fragment already added
////                cache.show(supportFragmentManager, tag)
//                Log.d(TAG, "$tag,dialog有缓存")
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
            return this.showDialog(this@BaseActivity)
        }
    }

    override fun registerFinishBroadcast(vararg actions: String?) {
        mBroadcastIntentFilter = mBroadcastIntentFilter ?: IntentFilter()
        actions.forEach {
            mBroadcastIntentFilter?.addAction(it)
        }
        mFinishReceiver = mFinishReceiver ?: (object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (mBroadcastIntentFilter?.matchAction(intent.action) == true) {
                    onBroadcastPageFinish()
                }
            }
        })
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mFinishReceiver!!, mBroadcastIntentFilter!!)
    }

    override fun registerRefreshBroadcast(vararg actions: String?) {
        mBroadcastIntentFilter = mBroadcastIntentFilter ?: IntentFilter()
        actions.forEach {
            mBroadcastIntentFilter?.addAction(it)
        }
        mRefreshReceiver = mRefreshReceiver ?: (object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (mBroadcastIntentFilter?.matchAction(intent.action) == true) {
                    onBroadcastPageRefresh(intent.getStringExtra("params"))
                }
            }
        })
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mRefreshReceiver!!, mBroadcastIntentFilter!!)
    }

    override fun registerOtherBroadcast(action: String) {
        mBroadcastIntentFilter = mBroadcastIntentFilter ?: IntentFilter()
        mBroadcastIntentFilter?.addAction(action)
        mOtherReceiver = mOtherReceiver ?: (object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (mBroadcastIntentFilter?.matchAction(intent.action) == true) {
                    onBroadcastOther(intent.action)
                }
            }
        })
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mOtherReceiver!!, mBroadcastIntentFilter!!)
    }

    override fun unregisterBroadcast(action: String?) {
        if (action != null) {
            val iterator = mBroadcastIntentFilter?.actionsIterator()
            while (iterator?.hasNext() == true) {
                if (iterator.next() == action) {
                    iterator.remove()
                }
            }
        } else {
            mFinishReceiver?.let {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
            }
            mFinishReceiver = null
        }
    }

    override fun sendFinishBroadcast(action: String?) {
        if (action.isNullOrBlank()) {
            return
        }
        val intent = Intent(action)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun sendRefreshBroadcast(action: String?, params: String?) {
        if (action.isNullOrBlank()) {
            return
        }
        val intent = Intent(action)
        params?.let {
            intent.putExtra("params", it)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun sendOtherBroadcast(action: String) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(action))
    }

    override fun onBroadcastPageFinish() {
        finish()
    }

    override fun onBroadcastPageRefresh(type: String?) {

    }

    override fun onBroadcastOther(action: String?) {

    }

    fun addBackPressedInterceptor(block: () -> Boolean) {
        onBackPressInterceptors = onBackPressInterceptors ?: ArrayList()
        onBackPressInterceptors?.add(block)
    }

    override fun onBackPressed() {
        onBackPressInterceptors?.forEach {
            if (it.invoke()) return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        mFinishReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
        mFinishReceiver = null
        mRefreshReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
        mRefreshReceiver = null
        mOtherReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
        mOtherReceiver = null
        mBroadcastIntentFilter = null

    }

}