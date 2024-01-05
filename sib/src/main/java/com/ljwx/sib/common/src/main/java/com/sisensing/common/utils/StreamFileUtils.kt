package com.sisensing.common.utils

import android.util.Log
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object StreamFileUtils {

    private val TAG = this.javaClass.simpleName

    fun toFile(responseBody: ResponseBody?, filePath: String) :Boolean{
        if (responseBody == null) {
            return false
        }
        val fileTotalLength = responseBody.contentLength()
        Log.d(TAG, "文件总长度:$fileTotalLength")
        var readTotalLength = 0L
        val buffer = ByteArray(4096)
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = responseBody.byteStream()
            outputStream = FileOutputStream(filePath)
            var readLength: Int
            while (inputStream.read(buffer).also { readLength = it } != -1) {
                outputStream.write(buffer, 0, readLength)
                readTotalLength += readLength
                val progress = (readTotalLength / fileTotalLength * 100f).toInt()
                Log.i(TAG, "文件下载进度:$progress")
            }
            outputStream.flush()
            Log.i(TAG, "文件下载完成:$filePath")
            return true
        } catch (e: IOException) {
            Log.i(TAG, "文件下载异常:$e")
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
        return false
    }

}