package com.ljwx.router

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import com.alibaba.android.arouter.launcher.ARouter
import com.ljwx.baseapp.constant.BaseConstBundleKey
import com.ljwx.baseapp.router.IPostcard
import java.io.Serializable

class RouterPostcard(private val path: String) : IPostcard {

    private var mBundle: Bundle? = null

    init {
        mBundle = Bundle()
    }

    private fun withAny(key: String, value: Any?): IPostcard {
        if (value == null) {
            return this
        }
        when (value) {
            is Int -> mBundle?.putInt(key, value)
            is IntArray -> mBundle?.putIntArray(key, value)
            is Long -> mBundle?.putLong(key, value)
            is LongArray -> mBundle?.putLongArray(key, value)
            is String -> mBundle?.putString(key, value)
            is Boolean -> mBundle?.putBoolean(key, value)
            is Float -> mBundle?.putFloat(key, value)
            is FloatArray -> mBundle?.putFloatArray(key, value)
            is Double -> mBundle?.putDouble(key, value)
            is DoubleArray -> mBundle?.putDoubleArray(key, value)
            is Bundle -> mBundle?.putBundle(key, value)
            is Parcelable -> mBundle?.putParcelable(key, value)
            is Serializable -> mBundle?.putSerializable(key, value)
        }
        return this
    }

    override fun with(key: String, bundle: Bundle?): IPostcard {
        withAny(key, bundle)
        return this
    }

    override fun with(key: String, value: Parcelable?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun with(key: String, value: Serializable?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withInt(key: String, value: Int?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withLong(key: String, value: Long?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withString(key: String, value: String?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withBoolean(key: String, value: Boolean?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withFloat(key: String, value: Float?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withDouble(key: String, value: Double?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withLongArray(key: String, value: LongArray?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withIntArray(key: String, value: IntArray?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withFloatArray(key: String, value: FloatArray?): IPostcard {
        withAny(key, value)
        return this
    }

    override fun withStringArrayList(key: String, value: ArrayList<String>?): IPostcard {
        mBundle?.putStringArrayList(key, value)
        return this
    }

    override fun withIntArrayList(key: String, value: ArrayList<Int>?): IPostcard {
        mBundle?.putIntegerArrayList(key, value)
        return this
    }

    override fun withFromType(type: Int): IPostcard {
        mBundle?.putInt(BaseConstBundleKey.FROM_TYPE, type)
        return this
    }

    override fun withDataId(id: String?): IPostcard {
        id?.let {
            mBundle?.putString(BaseConstBundleKey.DATA_ID, id)
        }
        return this
    }

    override fun start() {
        ARouter.getInstance().build(path).with(mBundle).navigation()
    }

    override fun start(activity: Activity, requestCode: Int) {
        ARouter.getInstance().build(path).with(mBundle).navigation(activity, requestCode)
    }


}