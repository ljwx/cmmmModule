package com.ljwx.baseapp.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ljwx.baseapp.constant.BaseConstBundleKey

inline fun <reified F : Fragment> Any.getFragmentInstance(isConditionType: Boolean): F {
    val fragment = F::class.java.newInstance()
    val bundle = Bundle()
    bundle.putBoolean(BaseConstBundleKey.IS_CONDITION_TYPE, isConditionType)
    fragment.arguments = bundle
    return fragment
}

inline fun <reified F : Fragment> Any.getFragmentInstance(fromType: Int): F {
    val fragment = F::class.java.newInstance()
    val bundle = Bundle()
    bundle.putInt(BaseConstBundleKey.FROM_TYPE, fromType)
    fragment.arguments = bundle
    return fragment
}