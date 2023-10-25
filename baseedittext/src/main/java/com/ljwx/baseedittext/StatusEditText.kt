package com.ljwx.baseedittext

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class StatusEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    AppCompatEditText(context, attrs) {

    private var invalid = false

    init {
//        setBgDrawable()
    }

    fun contentInvalid(invalid: Boolean) {
        if (this.invalid != invalid) {
            this.invalid = invalid
            refreshDrawableState()
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
//         super.onCreateDrawableState(extraSpace)
        // 注意，这里要给数组添加一个容量，用于储存自定义的状态
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (invalid) {
            mergeDrawableStates(drawableState, intArrayOf(R.attr.content_invalid))
        }
        return drawableState
    }

    /**
     * 设置button各种状态的颜色。
     *
     * @param pressDrawableId   按下去的颜色
     * @param focusDrawableId   获取焦点的颜色
     * @param disableDrawableId 不能点击的颜色
     * @param normalDrawableId  默认状态下颜色
     * @param radiusHeight      圆角的值
     */
    private fun setBgDrawable() {


        val stateListDrawable = StateListDrawable();
        val radiusHeight = 10f
        val buttonPress =
            context.resources.getDrawable(R.drawable.shape_press_test) as GradientDrawable
        if (radiusHeight > 0) {
            buttonPress.setCornerRadius(radiusHeight);
        }
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), buttonPress);

        val buttonFocus =
            context.getResources().getDrawable(R.drawable.shape_focuse_test) as GradientDrawable
        if (radiusHeight > 0) {
            buttonFocus.setCornerRadius(radiusHeight);
        }
        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), buttonFocus);

        val buttonDisable =
            context.resources.getDrawable(R.drawable.shape_disable_test) as GradientDrawable
        if (radiusHeight > 0) {
            buttonDisable.setCornerRadius(radiusHeight);
        }
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), buttonDisable)

        val buttonNormal =
            context.getResources().getDrawable(R.drawable.shape_normal_test) as GradientDrawable
        if (radiusHeight > 0) {
            buttonNormal.setCornerRadius(radiusHeight);
        }
        stateListDrawable.addState(intArrayOf(), buttonNormal);
        setBackground(stateListDrawable);
    }


}