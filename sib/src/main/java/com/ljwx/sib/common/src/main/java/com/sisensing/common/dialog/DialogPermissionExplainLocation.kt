package com.sisensing.common.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basedialog.dialog.CustomDialog
import com.sisensing.common.R
import com.sisensing.common.listener.CallbackType

class DialogPermissionExplainLocation(context: Context, callback: CallbackType) : CustomDialog(context) {

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_permission_explain_location, null, false)
        setView(view)
        setDimAmount(0.6f)
        setWidthMatch()
        view.findViewById<View>(R.id.tv_accept)?.singleClick {
            dismiss()
            callback.invoke(0)
        }
    }

}