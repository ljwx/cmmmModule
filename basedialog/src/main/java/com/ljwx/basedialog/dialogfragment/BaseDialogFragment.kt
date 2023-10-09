package com.ljwx.basedialog.dialogfragment

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
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.baseapp.extensions.notNullOrBlank
import com.ljwx.basedialog.R
import com.ljwx.basedialog.common.BaseDialogBuilder

open class BaseDialogFragment : DialogFragment() {

    companion object {
        private var commonLayout = R.layout.base_dialog_example
        fun setCommonLayout(@LayoutRes layoutRes: Int) {
            commonLayout = layoutRes
        }
    }

    private var builder: BaseDialogBuilder? = null

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

    fun getBuilder(): BaseDialogBuilder? {
        return builder
    }

    private fun setDataFromBuilder(view: View) {
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

    internal fun setBuilder(builder: BaseDialogBuilder): BaseDialogFragment {
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

}