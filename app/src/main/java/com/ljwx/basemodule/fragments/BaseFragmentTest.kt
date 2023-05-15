package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.view.View
import com.ljwx.baseapp.BaseViewModel
import com.ljwx.basefragment.BaseMVVMFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding

class BaseFragmentTest :
    BaseMVVMFragment<FragmentBaseFragmentBinding, BaseViewModel>(R.layout.fragment_base_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        showPopLoading(true)
//        lifecycleScope.launch(Dispatchers.IO){
//            delay(2000)
//            withContext(Dispatchers.Main){
//                dismissPopLoading(true)
//                showStateLayout(LayoutStatus.EMPTY)
//            }
//            delay(2000)
//            withContext(Dispatchers.Main){
//                showStateLayout(LayoutStatus.CONTENT)
//            }
//        }

    }

}