package com.ljwx.baseapp.dialog

import android.app.Dialog
import android.content.Context
import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

interface IBaseDialogBuilder {

    fun setViewLayout(@LayoutRes rootLayout: Int):IBaseDialogBuilder

    fun showCloseIcon(show: Boolean?): IBaseDialogBuilder

    fun setTitle(title: CharSequence?): IBaseDialogBuilder

    fun setTitle(@StringRes title: Int): IBaseDialogBuilder

    fun setContent(content: CharSequence?): IBaseDialogBuilder

    fun setContent(@LayoutRes contentLayout: Int): IBaseDialogBuilder

    fun setPositiveButton(
        text: CharSequence?,
        onClickListener: OnClickListener?
    ): IBaseDialogBuilder

    fun setPositiveButton(
        @StringRes stringRes: Int,
        onClickListener: OnClickListener?
    ): IBaseDialogBuilder

    fun showNormalPositiveButton(show: Boolean = true): IBaseDialogBuilder

    fun setNegativeButton(
        text: CharSequence?,
        onClickListener: OnClickListener?
    ): IBaseDialogBuilder

    fun setNegativeButton(
        @StringRes stringRes: Int,
        onClickListener: OnClickListener?
    ): IBaseDialogBuilder

    fun showNormalNegativeButton(show: Boolean): IBaseDialogBuilder

    fun buttonsReversal(reversal: Boolean)

    fun createDialogFragment(): DialogFragment

    fun createDialog(context: Context): Dialog

    fun showDialog(context: Context): Dialog

    fun showDialogFragment(manager: FragmentManager, tag: String?): DialogFragment

    fun showDialogFragment(manager: FragmentManager, @StringRes tag: Int): DialogFragment

    fun showDialogFragment(manager: FragmentManager): DialogFragment

    fun isShowing(dialogFragment: Boolean = false): Boolean
}