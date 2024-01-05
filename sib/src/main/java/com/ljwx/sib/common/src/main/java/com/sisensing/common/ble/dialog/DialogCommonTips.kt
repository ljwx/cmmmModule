package com.sisensing.common.ble.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.sisensing.common.R
import com.sisensing.common.databinding.DialogCommonTipsBinding

class DialogCommonTips(context: Context) : CustomDialog(context) {

    private var mBinding: DialogCommonTipsBinding
    private var title: String
    private var content: String
    private var button: String

    init {
        title = context.getString(R.string.tips)
        content = ""
        button = context.getString(R.string.common_know)
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_common_tips,
            null,
            false
        )
        setView(mBinding.root)
        setDimAmount(0.7f)
        mBinding.sure.setOnClickListener {
            dismiss()
        }
        setWidthMatch()
    }

    private fun showText() {
        mBinding.title.text = title
        mBinding.tips.text = content
        mBinding.sure.text = button
    }

    @JvmOverloads
    fun setText(title: String? = null, content: String? = null, button: String? = null) {
        this.title = title ?: this.title
        this.content = content ?: this.content
        this.button = button ?: this.button
    }

    override fun show() {
        showText()
        super.show()
    }

}