package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.ljwx.baseapp.LayoutStatus
import com.ljwx.baseapp.extensions.showToast
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
        mBinding.button.setOnClickListener {
            showToast("abc", repeat = false)
        }
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