package com.ljwx.basemodule.fragments

import com.ljwx.basedialog.dialogfragment.BaseBindingDialogFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.DialogTestBinding

class TestDialog :
    BaseBindingDialogFragment<DialogTestBinding>() {
    override fun layoutId(): Int {
        return R.layout.dialog_test
    }
}