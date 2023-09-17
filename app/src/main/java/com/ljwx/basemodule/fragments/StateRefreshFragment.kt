package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.view.View
import com.ljwx.baseapp.constant.LayoutStatus
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentStateRefreshBinding

class StateRefreshFragment :BaseBindingFragment<FragmentStateRefreshBinding>(R.layout.fragment_state_refresh){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showStateLayout(LayoutStatus.LOADING)

    }

}