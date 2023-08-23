package com.ljwx.basedialog.quick

import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.ljwx.basedialog.BaseDialogFragment

interface IBaseDialogBuilder {

    fun setTitle(title: CharSequence): IBaseDialogBuilder

    fun setTitle(@StringRes title: Int): IBaseDialogBuilder

    fun setContent(content: CharSequence): IBaseDialogBuilder

    fun setContent(@LayoutRes layout: Int): IBaseDialogBuilder

    fun setPositiveButton(text: CharSequence, onClickListener: OnClickListener): IBaseDialogBuilder

    fun setPositiveButton(
        @StringRes stringRes: Int,
        onClickListener: OnClickListener
    ): IBaseDialogBuilder

    fun setNegativeButton(text: CharSequence, onClickListener: OnClickListener): IBaseDialogBuilder

    fun setNegativeButton(
        @StringRes stringRes: Int,
        onClickListener: OnClickListener
    ): IBaseDialogBuilder

    fun create(): BaseDialogFragment

    fun show(manager: FragmentManager, tag: String): BaseDialogFragment

    fun show(manager: FragmentManager, @StringRes tag: Int): BaseDialogFragment

    fun show(manager: FragmentManager): BaseDialogFragment

}