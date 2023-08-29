package com.ljwx.basemodule.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ljwx.baseedittext.filter.*
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding
import com.ljwx.basescaffold.IntervalHandle
import com.ljwx.basescreenrecord.ScreenRecordActivity

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
//              NotificationUtils.sendFcmNotification("12345678","ljwx", "test")

//            BaseDialogFragment.Builder().setContent("fuck")
//                .showNormalPositiveButton()
//                .showCloseIcon(true).show(childFragmentManager)
//            showDialogTips(null, "内容测试", "test", "是的")
        }

        mBinding.et.filters = arrayOf(DividerInputFilter())
//        mBinding.et.addTextChangedListener(LimitDecimalTextWatcher())

    }

}