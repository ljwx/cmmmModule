package com.ljwx.baseview.button

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import com.ljwx.baseview.R

class DisableClickButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    style: Int = 0,
) : AppCompatButton(context, attributeSet, style) {

    private var disableClick = true

    init {
        val attr = context.obtainStyledAttributes(attributeSet, R.styleable.DisableClickButton)
        disableClick = attr.getBoolean(R.styleable.DisableClickButton_allowClickWhenDisabled, true)
        attr.recycle()
        gravity = Gravity.CENTER
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled && disableClick) {
            callOnClick()
            return true
        }
        return super.onTouchEvent(event)
    }

}