package com.ljwx.basemodule.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.TimeUtils
import com.ljwx.baseedittext.filter.*
import com.ljwx.baseeventbus.SimpleFlowEventBus
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.SecondActivity
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding
import com.ljwx.basenotification.NotificationUtils
import com.ljwx.basescaffold.IntervalHandle

class BaseFragmentTest :
    BaseBindingFragment<FragmentBaseFragmentBinding>(R.layout.fragment_base_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val interval = IntervalHandle()
        mBinding.button.setOnClickListener {
//            dialog.show(childFragmentManager)
//            startActivity(Intent(requireContext(), SecondActivity::class.java))
//            SimpleFlowEventBus.post("ljwx2","caonima")
//            interval.start()
              NotificationUtils.sendFcmNotification("12345678","ljwx", "test")

        }

    }

}