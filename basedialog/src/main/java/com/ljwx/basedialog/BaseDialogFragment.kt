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

        builder?.apply {
            title?.let {
                view.findViewById<TextView>(R.id.base_dialog_title)?.text = it
            }
            content?.let {
                view.findViewById<TextView>(R.id.base_dialog_content_string)?.text = it
            }
            positive?.let {
                view.findViewById<TextView>(R.id.base_dialog_positive)?.apply {
                    text = it
                    positiveListener?.let {
                        setOnClickListener(it)
                    }
                }
            }
            negative?.let {
                view.findViewById<TextView>(R.id.base_dialog_negative)?.apply {
                    text = it
                    negativeListener?.let {
                        setOnClickListener(it)
                    }
                }
            }
        }

    }

    protected fun initWidth() {
        // 隐藏标题栏, 不加弹窗上方会一个透明的标题栏占着空间
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        //getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

        var title: String? = null
            get() = title
        var content: String? = null
            get() = content
        var contentId: Int? = null
            get() = contentId
        var positive: String? = null
            get() = positive
        var positiveListener: View.OnClickListener? = null
            get() = positiveListener
        var negative: String? = null
            get() = negative
        var negativeListener: View.OnClickListener? = null
            get() = negativeListener
        var tag: String? = null
        var dialog: BaseDialogFragment? = null

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
            onClickListener: View.OnClickListener
        ): IBaseDialogBuilder {
            this.positive = text.toString()
            positiveListener = onClickListener
            return this
        }

        override fun setPositiveButton(
            stringRes: Int,
            onClickListener: View.OnClickListener
        ): IBaseDialogBuilder {
            this.positive = getStringRes(stringRes)
            positiveListener = onClickListener
            return this
        }

        override fun setNegativeButton(
            text: CharSequence,
            onClickListener: View.OnClickListener
        ): IBaseDialogBuilder {
            this.negative = text.toString()
            negativeListener = onClickListener
            return this
        }

        override fun setNegativeButton(
            stringRes: Int,
            onClickListener: View.OnClickListener
        ): IBaseDialogBuilder {
            this.negative = getStringRes(stringRes)
            negativeListener = onClickListener
            return this
        }

        override fun create(): BaseDialogFragment {
            return BaseDialogFragment().setBuilder(this)
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