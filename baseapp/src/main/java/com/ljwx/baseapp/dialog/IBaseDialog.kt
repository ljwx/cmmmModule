package com.ljwx.baseapp.dialog

import android.view.View

interface IBaseDialog {

    fun setPositiveButton(positiveText: CharSequence?, positiveListener: View.OnClickListener?)

    fun setNegativeButton(negativeText: CharSequence?, negativeListener: View.OnClickListener?)
}