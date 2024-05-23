package com.ljwx.basedialog.dialog

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ljwx.baseapp.dialog.IBaseDialog
import com.ljwx.baseapp.extensions.notNullOrBlank
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.basedialog.R
import com.ljwx.basedialog.common.BaseDialogBuilder

open class BaseDialog @JvmOverloads constructor(
    context: Context,
    @LayoutRes layoutRes: Int = commonLayout,
    theme: Int = 0
) :
    CustomDialog(context, theme), IBaseDialog {

    companion object {

        private var commonLayout = R.layout.base_dialog_example
        fun setCommonLayout(@LayoutRes layoutRes: Int) {
            commonLayout = layoutRes
        }
    }

    private var builder: BaseDialogBuilder? = null

    init {
        setDimAmount(0.7f)
        setView(layoutRes)
        setWidthMatch()
    }

    fun setBuilder(builder: BaseDialogBuilder): BaseDialog {
        this.builder = builder
        return this
    }

    open fun setDataFromBuilder() {
        val view = vRoot.rootView
        builder?.apply {
            view.findViewById<View>(R.id.base_dialog_close)?.apply {
                if (showClose != null) {
                    visibleGone(showClose!!)
                }
                singleClick {
                    dismiss()
                }
            }
            view.findViewById<TextView>(R.id.base_dialog_title)?.apply {
                visibleGone(showTitle)
                if (title != null) {
                    text = title
                }
            }
            view.findViewById<TextView>(R.id.base_dialog_content_string)?.apply {
                visibleGone(content != null)
                text = content ?: ""
            }
            //按钮
            if (showPositiveButton) {
                this@BaseDialog.setPositiveButton(positiveText, positiveListener)
            } else {
                view.findViewById<TextView>(R.id.base_dialog_positive)?.visibleGone(false)
            }
            if (showNegativeButton) {
                this@BaseDialog.setNegativeButton(negativeText, negativeListener)
            } else {
                view.findViewById<TextView>(R.id.base_dialog_negative)?.visibleGone(false)
            }
            //反转按钮位置
            if (buttonsReversal && showNegativeButton && showPositiveButton) {
                view.findViewById<TextView>(R.id.base_dialog_positive)?.apply {
                    setReversalButtons(this, true)
                }
                view.findViewById<TextView>(R.id.base_dialog_negative)?.apply {
                    setReversalButtons(this, false)
                }
            }
        }
    }

    open fun setReversalButtons(view: View, isPositive: Boolean) {
        view.apply {
            val params = layoutParams
            if (params is ConstraintLayout.LayoutParams) {
                params.rightToRight = if (isPositive) View.NO_ID else getParentId(this)
                params.leftToLeft = if (isPositive) getParentId(this) else View.NO_ID
                params.leftToRight = if (isPositive) View.NO_ID else R.id.base_dialog_positive
                params.rightToLeft = if (isPositive) R.id.base_dialog_negative else View.NO_ID
                params.verticalChainStyle = params.verticalChainStyle
                val left = params.leftMargin
                val right = params.rightMargin
                val start = params.marginStart
                val end = params.marginEnd
                params.leftMargin = right
                params.rightMargin = left
                params.marginStart = end
                params.marginEnd = start
                layoutParams = params
            }
        }
    }

    override fun setPositiveButton(
        positiveText: CharSequence?,
        positiveListener: OnClickListener?
    ) {
        vRoot.rootView.findViewById<TextView>(R.id.base_dialog_positive)?.apply {
            builder?.setPositiveButton(positiveText, positiveListener)
            visibleGone(true)
            if (positiveText.notNullOrBlank()) {
                text = positiveText
            }
            if (positiveListener != null) {
                setOnClickListener(positiveListener)
            } else {
                singleClick {
                    dismiss()
                }
            }
        }
    }

    override fun setNegativeButton(
        negativeText: CharSequence?,
        negativeListener: OnClickListener?
    ) {
        vRoot.rootView.findViewById<TextView>(R.id.base_dialog_negative)?.apply {
            builder?.setNegativeButton(negativeText, negativeListener)
            visibleGone(true)
            if (negativeText.notNullOrBlank()) {
                text = negativeText
            }
            if (negativeListener != null) {
                setOnClickListener(negativeListener)
            } else {
                singleClick {
                    dismiss()
                }
            }
        }
    }

    private fun getParentId(view: View): Int {
        (view.parent as? ViewGroup)?.let {
            if (it.id == View.NO_ID) {
                it.id = R.id.base_dialog_parent
                return it.id
            } else {
                return it.id
            }
        }
        return View.NO_ID
    }

}