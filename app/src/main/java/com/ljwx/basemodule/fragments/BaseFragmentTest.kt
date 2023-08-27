package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.basedialog.BaseBindingDialogFragment
import com.ljwx.basedialog.BaseDialogFragment
import com.ljwx.baseedittext.filter.*
import com.ljwx.baseedittext.watcher.LimitDecimalTextWatcher
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding
import com.ljwx.basenotification.NotificationUtils
import com.ljwx.basescaffold.IntervalHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            showDialogTips(null, "内容测试", "test", "是的")
            lifecycleScope.launch(Dispatchers.IO){
                delay(1000)
                withContext(Dispatchers.Main) {
                    showDialogTips(null, "内容测试", "test", "是的")
                }
            }
        }

        mBinding.et.filters = arrayOf(DividerInputFilter())
//        mBinding.et.addTextChangedListener(LimitDecimalTextWatcher())

    }

}