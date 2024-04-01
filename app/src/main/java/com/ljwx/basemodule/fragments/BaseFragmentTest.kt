package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ljwx.baseapp.debug.debugRun
import com.ljwx.baseapp.infochange.IBaseUserInfo
import com.ljwx.baseedittext.filter.*
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding
import com.ljwx.basemodule.debug.CustomRunDebug
import com.ljwx.basescaffold.IntervalHandle

class BaseFragmentTest :
    BaseBindingFragment<FragmentBaseFragmentBinding>(R.layout.fragment_base_fragment) {

    override var enableUserInfoChangeListener = false

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
            debugRun<CustomRunDebug>(9527)
            debugRun{

            }
        }

        mBinding.et.filters = arrayOf(DividerInputFilter())
//        mBinding.et.addTextChangedListener(LimitDecimalTextWatcher())

        logCheckDebug()
    }

    override fun userInfoChange(data: IBaseUserInfo?, type: Int) {
        super.userInfoChange(data, type)
        if (type == 3) {
            Log.d("asdf", "asdf4")
        }
    }

}