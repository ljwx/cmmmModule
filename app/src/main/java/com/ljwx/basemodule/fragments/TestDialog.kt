package com.ljwx.basemodule.fragments

import com.ljwx.basedialog.BaseBindingDialogFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding

class TestDialog :
    BaseBindingDialogFragment<FragmentBaseFragmentBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_base_fragment
    }
}