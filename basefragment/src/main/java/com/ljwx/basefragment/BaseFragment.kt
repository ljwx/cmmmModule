package com.ljwx.basefragment

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ljwx.baseapp.page.IPageReceiveEvent
import com.ljwx.baseapp.page.IPageProcessStep
import com.ljwx.baseapp.page.IPageDialogTips
import com.ljwx.baseapp.page.IPageStartPage
import com.ljwx.baseapp.router.IPostcard
import com.ljwx.basedialog.common.BaseDialogBuilder
import com.ljwx.router.Postcard

open class BaseFragment(@LayoutRes private val layoutResID: Int) : Fragment(), IPageReceiveEvent,
    IPageDialogTips, IPageProcessStep, IPageStartPage {

    open val TAG = this.javaClass.simpleName + "[Fragment]"

    protected var mActivity: AppCompatActivity? = null

    private var isLoaded = false

    /**
     * 广播事件
     */
    private val broadcastReceivers by lazy {
        HashMap<String, BroadcastReceiver>()
    }

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

    /**
     * 路由快速跳转
     */
    override fun startActivity(clazz: Class<*>) {
        context?.startActivity(Intent(context, clazz))
    }

    override fun routerTo(path: String): IPostcard {
        Log2.d(TAG, "路由跳转到:$path")
        return Postcard(path)
    }

    override fun showDialogTips(
        title: String?,
        content: String?,
        positiveText: String?
    ): Dialog? {
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
        if (!isAdded) {
            return null
        }
//        if (tag.notNullOrBlank()) {
//            val cache = childFragmentManager.findFragmentByTag(tag)
//            if (cache != null && cache is BaseDialogFragment) {
//                //报java.lang.IllegalStateException: Fragment already added
//                //有时间再看 TODO
////                cache.show(childFragmentManager, tag)
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
            //是否反转按钮
            buttonsReversal(reversalButtons)
            Log.d(TAG, "${(tag ?: content) ?: "tag为空"},dialog新创建")
            return showDialog(requireContext())
        }
    }

    /**
     * 事件广播使用
     */
    override fun registerLocalEvent(action: String?) {
        if (action == null) {
            return
        }
        val intentFilter = IntentFilter(action)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                intent.action?.let {
                    Log2.d(TAG, "接收到事件广播:$it")
                    if (intentFilter.matchAction(it)) {
                        onReceiveLocalEvent(it)
                    }
                }
            }
        }
        broadcastReceivers.put(action, receiver)
        Log2.d(TAG, "注册事件广播:$action")
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver, intentFilter)
        }
    }

    override fun sendLocalEvent(action: String?) {
        if (action == null) {
            return
        }
        Log2.d(TAG, "发送事件广播:$action")
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(Intent(action))
        }
    }

    override fun unregisterLocalEvent(action: String?) {
        action?.let {
            broadcastReceivers[it]?.let {
                context?.let { c ->
                    Log2.d(TAG, "注销事件广播:$action")
                    LocalBroadcastManager.getInstance(c).unregisterReceiver(it)
                }
            }
            broadcastReceivers.remove(it)
        }
    }


    override fun onReceiveLocalEvent(action: String) {

    }

    open fun lazyInit() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        Log2.d(TAG, "执行onDestroyView")
        isLoaded = false
    }

    override fun onDetach() {
        super.onDetach()
        Log2.d(TAG, "执行onDetach")
        mActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log2.d(TAG, "执行onDestroy")
        broadcastReceivers.keys.toList().forEach {
            unregisterLocalEvent(it)
        }
    }

}