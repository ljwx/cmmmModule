package com.ljwx.basemodule.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.SecondActivity
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding

class BaseFragmentTest :
    BaseBindingFragment<FragmentBaseFragmentBinding>(R.layout.fragment_base_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.button.setOnClickListener {
//            dialog.show(childFragmentManager)
            startActivity(Intent(requireContext(), SecondActivity::class.java))
        }

    }

}