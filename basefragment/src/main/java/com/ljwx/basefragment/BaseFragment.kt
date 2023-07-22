package com.ljwx.basefragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.blankj.utilcode.util.Utils
import com.ljwx.baseapp.page.IPageBroadcast

open class BaseFragment(@LayoutRes private val layoutResID: Int) : Fragment(), IPageBroadcast {

    open val TAG = this.javaClass.simpleName

    protected var mActivity: AppCompatActivity? = null

    private var isLoaded = false

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(layoutResID, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
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
        LocalBroadcastManager.getInstance(requireContext())
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
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(mRefreshReceiver!!, mBroadcastIntentFilter!!)
    }

    override fun registerOtherBroadcast(action: String) {
        mBroadcastIntentFilter = mBroadcastIntentFilter ?: IntentFilter(action)
        mOtherReceiver = mOtherReceiver ?: (object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (mBroadcastIntentFilter?.matchAction(intent.action) == true) {
                    onBroadcastOther(intent.action)
                }
            }
        })
        LocalBroadcastManager.getInstance(requireContext())
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
                LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(it)
            }
            mFinishReceiver = null
        }
    }

    override fun sendFinishBroadcast(action: String?) {
        if (action.isNullOrBlank()) {
            return
        }
        val intent = Intent(action)
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
        }
    }

    override fun sendRefreshBroadcast(action: String?, params: String?) {
        if (action.isNullOrBlank()) {
            return
        }
        val intent = Intent(action)
        params?.let {
            intent.putExtra("params", it)
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
        }
    }

    override fun sendOtherBroadcast(action: String) {
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(Intent(action))
        }
    }

    override fun onBroadcastPageFinish() {
        activity?.finish()
    }

    override fun onBroadcastPageRefresh(type: String?) {

    }

    override fun onBroadcastOther(action: String?) {

    }

    open fun lazyInit() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mFinishReceiver?.let {
            LocalBroadcastManager.getInstance(Utils.getApp()).unregisterReceiver(it)
        }
        mFinishReceiver = null
        mRefreshReceiver?.let {
            LocalBroadcastManager.getInstance(Utils.getApp()).unregisterReceiver(it)
        }
        mRefreshReceiver = null
        mOtherReceiver?.let {
            LocalBroadcastManager.getInstance(Utils.getApp()).unregisterReceiver(it)
        }
        mOtherReceiver = null
        mBroadcastIntentFilter = null
    }

}