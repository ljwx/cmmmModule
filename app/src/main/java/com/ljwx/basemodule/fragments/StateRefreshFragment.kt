package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentStateRefreshBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StateRefreshFragment :
    BaseBindingFragment<FragmentStateRefreshBinding>(R.layout.fragment_state_refresh) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        showStateLayout(LayoutStatus.LOADING)


    }

    override fun onRefreshData() {
        super.onRefreshData()
        Log.d("ljwx2", "执行刷新")
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                pullRefreshFinish()
            }
        }
    }

}