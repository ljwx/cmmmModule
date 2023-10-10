package com.ljwx.basedialog.dialog

import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ljwx.baseapp.extensions.notNullOrBlank
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.basedialog.R
import com.ljwx.basedialog.common.BaseDialogBuilder

class BaseDialog @JvmOverloads constructor(context: Context, theme: Int = 0) :
    CustomDialog(context, theme) {

    companion object {
        private var commonLayout = R.layout.base_dialog_example
        fun setCommonLayout(@LayoutRes layoutRes: Int) {
            commonLayout = layoutRes
        }
    }

    private var builder: BaseDialogBuilder? = null

    init {
        setDimAmount(0.7f)
        setView(commonLayout)
        setWidthMatch()
    }

    internal fun setBuilder(builder: BaseDialogBuilder): BaseDialog {
        this.builder = builder
        return this
    }

    internal fun setDataFromBuilder() {
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
            if (buttonsReversal && showNegativeButton && showPositiveButton) {
                view.findViewById<TextView>(R.id.base_dialog_negative)?.apply {
                    val params = layoutParams
                    if (params is ConstraintLayout.LayoutParams) {
                        params.leftToLeft = View.NO_ID
                        params.rightToRight = getParentId(this)
                        params.rightToLeft = View.NO_ID
                        params.leftToRight = R.id.base_dialog_positive
                        params.verticalChainStyle = params.verticalChainStyle
                        params.leftMargin = params.rightMargin
                        params.marginStart = params.marginEnd
                        layoutParams = params
                    }
                }
                view.findViewById<TextView>(R.id.base_dialog_positive)?.apply {
                    val params = layoutParams
                    if (params is ConstraintLayout.LayoutParams) {
                        params.rightToRight = View.NO_ID
                        params.leftToLeft = getParentId(this)
                        params.leftToRight = View.NO_ID
                        params.rightToLeft = R.id.base_dialog_negative
                        params.verticalChainStyle = params.verticalChainStyle
                        params.rightMargin = params.leftMargin
                        params.marginEnd = params.marginStart
                        layoutParams = params
                    }
                }
            }
        }
    }

    open fun setPositiveButton(positiveText: CharSequence?, positiveListener: OnClickListener?) {
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

    open fun setNegativeButton(negativeText: CharSequence?, negativeListener: OnClickListener?) {
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