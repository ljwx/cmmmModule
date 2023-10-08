package com.ljwx.basedialog.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
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
            view.findViewById<TextView>(R.id.base_dialog_positive)?.apply {
                visibleGone(showPositiveButton)
                if (showPositiveButton) {
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
            view.findViewById<TextView>(R.id.base_dialog_negative)?.apply {
                visibleGone(showNegativeButton)
                if (showNegativeButton) {
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
        }
    }

}