package com.sisensing.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.blankj.utilcode.util.ActivityUtils
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basedialog.common.BaseDialogBuilder
import com.sisensing.common.R

class ImageViewTips @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var content: String? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.ImageViewTips)
        try {
            title = attr.getString(R.styleable.ImageViewTips_ivt_title)
            content = attr.getString(R.styleable.ImageViewTips_ivt_content)
        } finally {
            attr.recycle()
        }
        this.singleClick {
            ActivityUtils.getTopActivity()?.let {
                if (title != null && content != null) {
                    BaseDialogBuilder().setTitle(title).setContent(content)
                        .setPositiveButton(context.getString(R.string.personalcenter_ok), null)
                        .showDialog(it)
                }
            }
        }
    }

}