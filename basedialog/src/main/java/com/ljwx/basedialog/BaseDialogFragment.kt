package com.ljwx.basedialog

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ljwx.baseapp.extensions.getStringRes
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.basedialog.quick.IBaseDialogBuilder

open class BaseDialogFragment() : DialogFragment() {

    companion object {
        private var commonLayout = R.layout.base_dialog_example
        fun setCommonLayout(@LayoutRes layoutRes: Int) {
            commonLayout = layoutRes
        }
    }

    private var builder: Builder? = null

    protected lateinit var mActivity: AppCompatActivity

    private var tag: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        val width = dm.widthPixels * 1
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initWidth()
        return LayoutInflater.from(requireContext()).inflate(commonLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataFromBuilder(view)
    }

    protected fun initWidth() {
        // 隐藏标题栏, 不加弹窗上方会一个透明的标题栏占着空间
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        //getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private fun setDataFromBuilder(view: View) {
        builder?.apply {
            showClose?.let {
                view.findViewById<View>(R.id.base_dialog_close)?.apply {
                    singleClick {
                        dismiss()
                    }
                    visibleGone(it)
                }
            }
            title?.let {
                view.findViewById<TextView>(R.id.base_dialog_title)?.text = it
            }
            content?.let {
                view.findViewById<TextView>(R.id.base_dialog_content_string)?.text = it
            }
            val positiveView = view.findViewById<TextView>(R.id.base_dialog_positive)
            if (showPositive) {
                if (positive.isNullOrBlank()) {
                    positiveView?.singleClick {
                        dismiss()
                    }
                } else {
                    positiveView?.apply {
                        text = positive
                        if (positiveListener == null) {
                            singleClick {
                                dismiss()
                            }
                        } else {
                            setOnClickListener(positiveListener)
                        }
                    }
                }
                positiveView?.visibleGone(true)
            } else {
                positiveView?.visibleGone(false)
            }

            val negativeView = view.findViewById<TextView>(R.id.base_dialog_negative)
            if (showNegative) {
                if (negative.isNullOrBlank()) {
                    negativeView?.singleClick {
                        dismiss()
                    }
                } else {
                    negativeView?.apply {
                        text = positive
                        if (negativeListener == null) {
                            singleClick {
                                dismiss()
                            }
                        } else {
                            setOnClickListener(negativeListener)
                        }
                    }
                }
                negativeView?.visibleGone(true)
            } else {
                negativeView?.visibleGone(false)
            }
        }
    }

    private fun setBuilder(builder: Builder): BaseDialogFragment {
        this.builder = builder
        return this
    }

    fun isShowing(): Boolean {
        return dialog?.isShowing == true
    }

    open fun show(manager: FragmentManager) {
        val tag = this.javaClass.simpleName
        show(manager, tag)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        this.tag = tag
        if (manager.isStateSaved) {
            setFieldValue(this, "mDismissed", false)
            setFieldValue(this, "mShownByMe", true)
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } else {
            super.show(manager, tag)
        }
    }

    private fun setFieldValue(obj: Any, fieldName: String, fieldValue: Any) {
        val field = DialogFragment::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        field[obj] = fieldValue
    }

    class Builder : IBaseDialogBuilder {

        var showClose: Boolean? = null
            private set
        var title: String? = null
            private set
        var content: String? = null
            private set
        var contentId: Int? = null
            private set
        var positive: String? = null
            private set
        var positiveListener: View.OnClickListener? = null
            private set
        var showPositive = true
            private set
        var negative: String? = null
            private set
        var negativeListener: View.OnClickListener? = null
            private set
        var showNegative = true
            private set
        var tag: String? = null
        var dialog: BaseDialogFragment? = null
        override fun setClose(show: Boolean): IBaseDialogBuilder {
            showClose = show
            return this
        }


        override fun setTitle(title: CharSequence): IBaseDialogBuilder {
            this.title = title.toString()
            return this
        }

        override fun setTitle(title: Int): IBaseDialogBuilder {
            this.title = getStringRes(title)
            return this
        }

        override fun setContent(content: CharSequence): IBaseDialogBuilder {
            this.content = content.toString()
            return this
        }

        override fun setContent(layout: Int): IBaseDialogBuilder {
            this.contentId = layout
            return this
        }

        override fun setPositiveButton(
            text: CharSequence,
            onClickListener: View.OnClickListener?
        ): IBaseDialogBuilder {
            this.positive = text.toString()
            positiveListener = onClickListener
            return this
        }

        override fun setPositiveButton(
            stringRes: Int,
            onClickListener: View.OnClickListener?
        ): IBaseDialogBuilder {
            this.positive = getStringRes(stringRes)
            positiveListener = onClickListener
            return this
        }

        override fun deletePositiveButton(): IBaseDialogBuilder {
            showPositive = false
            return this
        }

        override fun setNegativeButton(
            text: CharSequence,
            onClickListener: View.OnClickListener?
        ): IBaseDialogBuilder {
            this.negative = text.toString()
            negativeListener = onClickListener
            return this
        }

        override fun setNegativeButton(
            stringRes: Int,
            onClickListener: View.OnClickListener?
        ): IBaseDialogBuilder {
            this.negative = getStringRes(stringRes)
            negativeListener = onClickListener
            return this
        }

        override fun deleteNegativeButton(): IBaseDialogBuilder {
            showNegative = false
            return this
        }

        override fun create(): BaseDialogFragment {
            val dialog = this.dialog ?: BaseDialogFragment().setBuilder(this)
            return dialog
        }

        override fun show(manager: FragmentManager, tag: String): BaseDialogFragment {
            val dialog = this.dialog ?: BaseDialogFragment().setBuilder(this)
            this.tag = tag
            dialog.show(manager, tag)
            return dialog
        }

        override fun show(manager: FragmentManager, tag: Int): BaseDialogFragment {
            val dialog = this.dialog ?: BaseDialogFragment().setBuilder(this)
            this.tag = getStringRes(tag)
            dialog.show(manager, this.tag)
            return dialog
        }

        override fun show(manager: FragmentManager): BaseDialogFragment {
            val dialog = this.dialog ?: BaseDialogFragment().setBuilder(this)
            this.tag = content ?: title ?: positive ?: System.currentTimeMillis().toString()
            dialog.show(manager)
            return dialog
        }

    }

}