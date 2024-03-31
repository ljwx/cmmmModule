package com.ljwx.basenetwork

import android.util.Log
import com.ljwx.basenetwork.download.BaseDownloadCallback
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

object BaseDownloadUtils {

    private val Tag = this.javaClass.simpleName

    fun download(
        dirPath: String?,
        filename: String?,
        body: ResponseBody,
        callback: BaseDownloadCallback
    ) {
        if (dirPath == null || filename == null) {
            return
        }
        try {
            val file = initFile(dirPath, filename)
            streamToFile(body, file, callback)
        } catch (e: Exception) {
            callback.onFailure(e)
        }
    }

    private fun initFile(dirPath: String, filename: String): File {
        val filepath = File(dirPath)
        if (!filepath.exists()) {
            filepath.mkdirs()
        }
        val file = File(filepath.canonicalPath, filename)
        if (file.exists()) {
            val result = file.delete()
            Log.d(Tag, "文件下载,文件已存在,删除结果:" + result + "-" + file.path)
        }
        return file
    }

    private fun streamToFile(body: ResponseBody, file: File, callback: BaseDownloadCallback) {
        try {
            val buffer = ByteArray(1024)
            val contentLength: Long = body.contentLength()
            var lastProgress = 0
            body.byteStream().use { input ->
                FileOutputStream(file).use { fos ->
                    var length: Int
                    var sum: Long = 0
                    while (input.read(buffer).also { length = it } != -1) {
                        fos.write(buffer, 0, length)
                        sum += length.toLong()
                        val progress = (sum * 100 / contentLength).toInt()
                        if (progress > lastProgress) {
                            lastProgress = progress
                            callback.onProgress(progress)
                        }
                    }
                    fos.flush()
                }
            }
            callback.onSuccess(file)
            Log.d(Tag, "文件下载成功: ${file.path}")
        } catch (e: Exception) {
            Log.d(Tag, "文件下载写入异常: ${e}")
            if (file.exists()) {
                file.delete()
            }
            callback.onFailure(e)
        }
    }

}