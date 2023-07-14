package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.LayoutStatus
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BaseFragmentTest :
    BaseBindingFragment<FragmentBaseFragmentBinding>(R.layout.fragment_base_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showStateLayout(LayoutStatus.LOADING)
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                dismissPopLoading(true)
                showStateLayout(LayoutStatus.EMPTY)
            }
            delay(2000)
            withContext(Dispatchers.Main) {
                showStateLayout(LayoutStatus.CONTENT)
            }
        }

        registerRefreshBroadcast("ljwx5", "ljwx3", "ljwx4")

        mBinding.button.setOnClickListener {
            sendRefreshBroadcast("ljwx3", "cao")
//            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("ljwx2"))
        }

//        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(object :BroadcastReceiver(){
//            override fun onReceive(p0: Context?, p1: Intent) {
//                Log.d("ljwx2", p1.action!!)
//            }
//
//        }, IntentFilter("ljwx2"))
    }

    override fun onBroadcastPageRefresh(type: String?) {
        super.onBroadcastPageRefresh(type)
        Log.d("ljwx2", "刷新:"+type)
    }

    override fun onPullRefresh() {
        super.onPullRefresh()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                pullRefreshFinish()
            }
        }
    }

}