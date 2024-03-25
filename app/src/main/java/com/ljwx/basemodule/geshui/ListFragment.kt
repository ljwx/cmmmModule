package com.ljwx.basemodule.geshui

import android.os.Bundle
import android.view.View
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.GeshuiFragmentBinding
import com.ljwx.basemodule.databinding.GeshuiFragmentListBinding

class ListFragment : BaseBindingFragment<GeshuiFragmentListBinding>(R.layout.geshui_fragment_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.image.setImageResource(R.mipmap.list2)
    }

}