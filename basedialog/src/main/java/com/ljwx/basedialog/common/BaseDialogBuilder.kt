package com.ljwx.basedialog.common

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.ljwx.baseapp.dialog.IBaseDialogBuilder
import com.ljwx.baseapp.extensions.getStringRes
import com.ljwx.basedialog.dialog.BaseDialog
import com.ljwx.basedialog.dialogfragment.BaseDialogFragment

class BaseDialogBuilder : IBaseDialogBuilder {

    var rootLayout: Int? = null
        private set

    var showClose: Boolean? = null
        private set
    var title: String? = null
        private set
    var showTitle: Boolean = false
        private set
    var content: String? = null
        private set
    var contentId: Int? = null
        private set
    var positiveText: CharSequence? = null
        private set
    var positiveListener: View.OnClickListener? = null
        private set
    var showPositiveButton = false
        private set
    var negativeText: CharSequence? = null
        private set
    var negativeListener: View.OnClickListener? = null
        private set
    var showNegativeButton = false
        private set
    var tag: String? = null
    var buttonsReversal = false
        private set
    private var dialogFragment: BaseDialogFragment? = null
    private var dialog: BaseDialog? = null


    override fun setViewLayout(rootLayout: Int): IBaseDialogBuilder {
        this.rootLayout = rootLayout
        return this
    }

    override fun showCloseIcon(show: Boolean?): IBaseDialogBuilder {
        showClose = show
        return this
    }

    override fun setTitle(title: CharSequence?): IBaseDialogBuilder {
        this.title = title.toString()
        showTitle = true
        return this
    }

    override fun setTitle(title: Int): IBaseDialogBuilder {
        this.title = getStringRes(title)
        showTitle = true
        return this
    }

    override fun setContent(content: CharSequence?): IBaseDialogBuilder {
        this.content = content.toString()
        return this
    }

    override fun setContent(layout: Int): IBaseDialogBuilder {
        this.contentId = layout
        return this
    }

    override fun setPositiveButton(
        text: CharSequence?,
        onClickListener: View.OnClickListener?
    ): IBaseDialogBuilder {
        this.positiveText = text
        positiveListener = onClickListener
        showPositiveButton = true
        return this
    }

    override fun setPositiveButton(
        stringRes: Int,
        onClickListener: View.OnClickListener?
    ): IBaseDialogBuilder {
        this.positiveText = getStringRes(stringRes)
        positiveListener = onClickListener
        showPositiveButton = true
        return this
    }

    override fun showNormalPositiveButton(show: Boolean): IBaseDialogBuilder {
        showPositiveButton = show
        return this
    }

    override fun setNegativeButton(
        text: CharSequence?,
        onClickListener: View.OnClickListener?
    ): IBaseDialogBuilder {
        this.negativeText = text
        negativeListener = onClickListener
        showNegativeButton = true
        return this
    }

    override fun setNegativeButton(
        stringRes: Int,
        onClickListener: View.OnClickListener?
    ): IBaseDialogBuilder {
        this.negativeText = getStringRes(stringRes)
        negativeListener = onClickListener
        showNegativeButton = true
        return this
    }

    override fun showNormalNegativeButton(show: Boolean): IBaseDialogBuilder {
        showNegativeButton = show
        return this
    }

    override fun buttonsReversal(reversal: Boolean) {
        buttonsReversal = reversal
    }

    override fun createDialog(context: Context): Dialog {
        val dialog = this.dialog ?: BaseDialog(context).setBuilder(this)
        dialog.setDataFromBuilder()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun createDialogFragment(): BaseDialogFragment {
        val dialog = this.dialogFragment ?: BaseDialogFragment().setBuilder(this)
        return dialog
    }

    override fun showDialog(context: Context): Dialog {
        val dialog = this.dialog ?: BaseDialog(context).setBuilder(this)
        dialog.setDataFromBuilder()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        return dialog
    }

    override fun showDialogFragment(manager: FragmentManager, tag: String?): BaseDialogFragment {
        val dialog = this.dialogFragment ?: BaseDialogFragment().setBuilder(this)
        this.tag = tag ?: content
        dialog.show(manager, this.tag)
        return dialog
    }

    override fun showDialogFragment(manager: FragmentManager, tag: Int): BaseDialogFragment {
        val dialog = this.dialogFragment ?: BaseDialogFragment().setBuilder(this)
        this.tag = getStringRes(tag)
        dialog.show(manager, this.tag)
        return dialog
    }

    override fun showDialogFragment(manager: FragmentManager): BaseDialogFragment {
        val dialog = this.dialogFragment ?: BaseDialogFragment().setBuilder(this)
        this.tag = this.tag ?: content
        dialog.show(manager, this.tag)
        return dialog
    }

    override fun isShowing(dialoagFragment: Boolean): Boolean {
        if (dialoagFragment) {
            return this.dialogFragment?.isShowing() == true
        } else {
            return this.dialog?.isShowing == true
        }
    }

}