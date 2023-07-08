package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ljwx.baseapp.extensions.lifecycle
import com.ljwx.basefragment.BaseMVVMFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentViewmodelBinding
import com.ljwx.basemodule.vm.TestViewModel
import io.reactivex.rxjava3.core.Observable

class ViewModelFragment :
    BaseMVVMFragment<FragmentViewmodelBinding, TestViewModel>(R.layout.fragment_viewmodel) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.mResponse.observe(viewLifecycleOwner) {
            Log.d("ljwx2", it)
        }
        mViewModel.requestTest()

        mViewModel.getUsers().observe(viewLifecycleOwner){

        }

        Observable.just(1).timeInterval().lifecycle(this).subscribe {

        }

    }

}