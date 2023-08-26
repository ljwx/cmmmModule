package com.ljwx.baseapp.dialog

import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

interface IBaseDialogBuilder {

    fun showCloseIcon(show: Boolean?): IBaseDialogBuilder

    fun setTitle(title: CharSequence?): IBaseDialogBuilder

    fun setTitle(@StringRes title: Int): IBaseDialogBuilder

    fun setContent(content: CharSequence?): IBaseDialogBuilder

    fun setContent(@LayoutRes layout: Int): IBaseDialogBuilder

    fun setPositiveButton(text: CharSequence?, onClickListener: OnClickListener?): IBaseDialogBuilder

    fun setPositiveButton(
        @StringRes stringRes: Int,
        onClickListener: OnClickListener?
    ): IBaseDialogBuilder

    fun showNormalPositiveButton(show: Boolean = true): IBaseDialogBuilder

    fun setNegativeButton(text: CharSequence?, onClickListener: OnClickListener?): IBaseDialogBuilder

    fun setNegativeButton(
        @StringRes stringRes: Int,
        onClickListener: OnClickListener?
    ): IBaseDialogBuilder

    fun showNormalNegativeButton(show: Boolean): IBaseDialogBuilder

    fun create(): DialogFragment

    fun show(manager: FragmentManager, tag: String?): DialogFragment

    fun show(manager: FragmentManager, @StringRes tag: Int): DialogFragment

    fun show(manager: FragmentManager): DialogFragment

}