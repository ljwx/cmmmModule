package com.ljwx.basefragment

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.keyboard.KeyboardHeightProvider

abstract class BaseMVVMPadKeyboardFragment<Binding : ViewDataBinding, BindingPad : ViewDataBinding, ViewModel : BaseViewModel<*>>(
    @LayoutRes layoutRes: Int,
    @LayoutRes private val layoutResPad: Int
) : BaseMVVMPadFragment<Binding, BindingPad, ViewModel>(layoutRes, layoutResPad) {

    private val rootView by lazy { if (isPad) mBindingPad.root else mBinding.root }

    protected val keyboardHighProvider by lazy {
        KeyboardHeightProvider(
            requireActivity()
        )
    }

    private var hidePopBottom = 0

    override fun onResume() {
        super.onResume()
        setKeyboardHeightObserver()
    }

    open fun setKeyboardHeightObserver() {
        keyboardHighProvider.setKeyboardHeightObserver { height, orientation ->
            var keyBoardHeight = 0
            if (mScreenHeight <= 0) {
                hidePopBottom = height
            } else {
                if (keyBoardHeight <= 0 && height > hidePopBottom && height - hidePopBottom > mScreenHeight / 4) {
                    keyBoardHeight = height - hidePopBottom
                }
                if (keyBoardHeight > mScreenHeight * 3 / 5) {
                    keyBoardHeight = height - hidePopBottom
                }
            }
            if (mScreenHeight <= 0) {
                mScreenHeight = rootView.getHeight()
            }
            if (isKeyboardShow(height, hidePopBottom)) { //软键盘弹出
                keyboardHeightChange(true, keyBoardHeight)
            } else {
                keyboardHeightChange(false, 0)
            }
        }
    }

    open fun isKeyboardShow(height: Int, hidePopBottom: Int): Boolean {
        return height - hidePopBottom > mScreenHeight / 4
    }

    open fun keyboardHeightChange(show: Boolean, height: Int) {

    }

    override fun onPause() {
        super.onPause()
        keyboardHighProvider.setKeyboardHeightObserver(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardHighProvider.close()
    }

}