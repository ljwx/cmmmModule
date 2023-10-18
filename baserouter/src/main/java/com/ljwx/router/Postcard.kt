package com.ljwx.router

import android.os.Bundle
import android.os.Parcelable
import com.alibaba.android.arouter.launcher.ARouter
import com.ljwx.baseapp.router.IPostcard
import java.io.Serializable

class Postcard(private val path: String) : IPostcard {

    private var mBundle: Bundle? = null

    init {
        mBundle = Bundle()
    }

    private fun with(key: String, value: Any?): IPostcard {
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
        with(key, bundle)
        return this
    }

    override fun with(key: String, value: Parcelable?): IPostcard {
        with(key, value)
        return this
    }

    override fun with(key: String, value: Serializable?): IPostcard {
        with(key, value)
        return this
    }

    override fun withInt(key: String, value: Int?): IPostcard {
        with(key, value)
        return this
    }

    override fun withLong(key: String, value: Long?): IPostcard {
        with(key, value)
        return this
    }

    override fun withString(key: String, value: String?): IPostcard {
        with(key, value)
        return this
    }

    override fun withBoolean(key: String, value: Boolean?): IPostcard {
        with(key, value)
        return this
    }

    override fun withFloat(key: String, value: Float?): IPostcard {
        with(key, value)
        return this
    }

    override fun withDouble(key: String, value: Double?): IPostcard {
        with(key, value)
        return this
    }

    override fun withLongArray(key: String, value: LongArray?): IPostcard {
        with(key, value)
        return this
    }

    override fun withIntArray(key: String, value: IntArray?): IPostcard {
        with(key, value)
        return this
    }

    override fun withFloatArray(key: String, value: FloatArray?): IPostcard {
        with(key, value)
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

    override fun start() {
        ARouter.getInstance().build(path).with(mBundle).navigation()
    }


}