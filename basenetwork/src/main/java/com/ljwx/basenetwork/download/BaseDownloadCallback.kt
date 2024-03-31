package com.ljwx.basenetwork.download

import java.io.File

abstract class BaseDownloadCallback {

    abstract fun onSuccess(file: File)

    open fun onProgress(progress: Int) {

    }

    open fun onFailure(e: Exception) {

    }

}