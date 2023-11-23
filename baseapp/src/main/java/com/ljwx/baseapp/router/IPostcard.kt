package com.ljwx.baseapp.router

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

interface IPostcard {

    fun withInt(key: String, value: Int?): IPostcard
    fun withLong(key: String, value: Long?): IPostcard
    fun withString(key: String, value: String?): IPostcard
    fun withBoolean(key: String, value: Boolean?): IPostcard
    fun withFloat(key: String, value: Float?): IPostcard
    fun withDouble(key: String, value: Double?): IPostcard

    fun withLongArray(key: String, value: LongArray?): IPostcard
    fun withIntArray(key: String, value: IntArray?): IPostcard
    fun withFloatArray(key: String, value: FloatArray?): IPostcard
    fun withStringArrayList(key: String, value: ArrayList<String>?): IPostcard
    fun withIntArrayList(key: String, value: ArrayList<Int>?): IPostcard


    fun with(key: String, bundle: Bundle?): IPostcard
    fun with(key: String, value: Parcelable?): IPostcard
    fun with(key: String, value: Serializable?): IPostcard

    fun withFromType(type: Int): IPostcard

    fun withDataId(id: String?): IPostcard

    fun start()

    fun start(activity: Activity, requestCode: Int)

}