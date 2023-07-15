package com.ljwx.baseactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gyf.immersionbar.ImmersionBar
import com.ljwx.baseapp.page.IPageBroadcast
import com.ljwx.baseapp.page.IPageStatusBar
import com.ljwx.baseapp.page.IPageToolbar

open class BaseActivity : AppCompatActivity(), IPageStatusBar, IPageToolbar, IPageBroadcast {

    open val TAG = this.javaClass.simpleName

    private val mStatusBar by lazy {
        ImmersionBar.with(this)
    }

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

    override fun getStatusBarUtils(): ImmersionBar {
        return mStatusBar;
    }

    override fun setStatusBar(backgroundColor: Int, fontDark: Boolean): ImmersionBar {
        mStatusBar
            .reset()//解决状态栏和布局重叠问题
            .fitsSystemWindows(true)//默认为false，当为true时一定要指定statusBarColor()
            .statusBarColor(backgroundColor)
            .statusBarDarkFont(fontDark)//状态栏字体是深色，不写默认为亮色
            .init()
        return mStatusBar
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
        mBroadcastIntentFilter = mBroadcastIntentFilter ?: IntentFilter(action)
        mOtherReceiver = mOtherReceiver ?: (object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (mBroadcastIntentFilter?.matchAction(intent.action) == true) {
                    onBroadcastOther()
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

    override fun onBroadcastOther() {

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