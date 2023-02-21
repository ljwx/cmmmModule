package com.ljwx.baseactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

open class BaseActivity : AppCompatActivity() {

    open val TAG = this.javaClass.simpleName

    /**
     * 结束当前页的广播
     */
    private var mFinishReceiver: BroadcastReceiver? = null

    /**
     * 结束广播的Intent
     */
    private var mFinishIntent: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    /**
     * 注册结束广播的action
     *
     * @param actions 多action,任一匹配则finish
     */
    fun registerFinishBroadcast(vararg actions: String) {
        mFinishIntent = mFinishIntent ?: IntentFilter()
        actions.forEach {
            mFinishIntent?.addAction(it)
        }
        mFinishReceiver = mFinishReceiver ?: (object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (mFinishIntent?.matchAction(intent.action) == true) {
                    finish()
                }
            }
        })
        LocalBroadcastManager.getInstance(this).registerReceiver(mFinishReceiver!!, mFinishIntent!!)
    }

    /**
     * 注销页面结束广播
     *
     * @param action 注册过的action,为空则注销所有action广播
     */
    fun unregisterFinishBroadcast(action: String?) {
        if (action != null) {
            val iterator = mFinishIntent?.actionsIterator()
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


    override fun onDestroy() {
        super.onDestroy()
        mFinishReceiver = null
        mFinishIntent = null
    }

}