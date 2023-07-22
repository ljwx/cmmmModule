package com.ljwx.basemodule.fragments

import com.ljwx.basedialog.BaseBindingDialogFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.DialogTestBinding
import com.ljwx.basemodule.databinding.FragmentBaseFragmentBinding

class TestDialog :
    BaseBindingDialogFragment<DialogTestBinding>() {
    override fun layoutId(): Int {
        return R.layout.dialog_test
    }
}