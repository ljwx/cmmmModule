package com.ljwx.baseactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
     * 结束当前页的广播
     */
    private var mFinishReceiver: BroadcastReceiver? = null

    /**
     * 刷新广播
     */
    private var mRefreshReceiver: BroadcastReceiver? = null

    /**
     * 注册广播的Intent
     */
    private var mBroadcastIntentFilter: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun initToolbar(toolbarId: Int?): Toolbar? {
        // 使用通用id或自定义id
        return if (supportActionBar == null) {
            //默认通用id
            val toolbar = findViewById(toolbarId ?: R.id.base_activity_toolbar) as? Toolbar
            setSupportActionBar(toolbar)
            toolbar?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            toolbar
        } else {
            null
        }
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
                    onBroadcastPageRefresh(intent.type)
                }
            }
        })
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mRefreshReceiver!!, mBroadcastIntentFilter!!)
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

    override fun sendRefreshBroadcast(action: String?, type: String?) {
        if (action.isNullOrBlank()) {
            return
        }
        val intent = Intent(action)
        type?.let {
            intent.type = type
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onBroadcastPageFinish() {
        finish()
    }

    override fun onBroadcastPageRefresh(type: String?) {

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
        mBroadcastIntentFilter = null
    }


}