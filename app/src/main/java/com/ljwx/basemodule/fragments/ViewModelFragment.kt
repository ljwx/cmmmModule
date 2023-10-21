package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.extensions.lifecycle
import com.ljwx.basefragment.BaseMVVMFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentViewmodelBinding
import com.ljwx.basemodule.vm.TestViewModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
//                TestDialog().show(childFragmentManager)
            }
        }

        mViewModel.mIntervelTest.observe(viewLifecycleOwner){
            Log.d("ljwx2", it)
        }

    }

}