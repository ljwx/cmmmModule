package com.ljwx.baseapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import java.lang.reflect.ParameterizedType

open class BaseViewModelFactory() :
    ViewModelProvider.Factory {

//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        return when (modelClass) {
//
//        }
//
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//
//        val type = javaClass.genericSuperclass as ParameterizedType
//        val repositoryClass = type.actualTypeArguments.getOrNull(0) as Class<BDR>
//
//        return modelClass.getConstructor(repositoryClass).newInstance(new())
//    }
//
//    private inline fun <reified BDR : BaseDataRepository> new(): BDR {
//        val clz = BDR::class.java
//        val mCreate = clz.getDeclaredConstructor()
//        mCreate.isAccessible = true
//        return mCreate.newInstance()
//    }

}
