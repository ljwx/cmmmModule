package com.ljwx.basemediaselector.upload

import android.content.Context
import android.util.Log
import com.ljwx.basemediaselector.MediaSelectorFile
import com.ljwx.basemediaselector.alioss.AliOssConfig
import com.ljwx.basemediaselector.alioss.AliOssException
import com.ljwx.basemediaselector.alioss.AliOssUpload

/**
 * 阿里OSS文件上传,重复图片只上传一次
 */
class AliOssUploadUtils() {

    private val TAG = this.javaClass.simpleName + "-AliOss"

    /**
     * 选择类型
     */
    companion object {
        const val AVAILABLE_PATH = 1
        const val REAL_PATH = 2
        const val COMPRESS_PATH = 3
    }

    /**
     * 待上传的文件
     */
    private val mUploadFiles = LinkedHashSet<MediaSelectorFile>()

    /**
     * 完成结果监听
     */
    private var mResultListener: ResultListener? = null

    /**
     * 阿里oss上传
     */
    private var mOssUpload: AliOssUpload? = null

    /**
     * 剩余待上传文件数量
     */
    private var mUploadRemainCount = 0

    /**
     * 设置阿里oss上传配置
     *
     * @param context 上下文
     * @param aliOssConfig 阿里oss配置
     */
    fun setAliOssUpload(context: Context, aliOssConfig: AliOssConfig) {
        mOssUpload = AliOssUpload(context, aliOssConfig)
        // 设置回调
        mOssUpload?.setResultListener(mOssUploadResult)
    }

    private val mOssUploadResult by lazy {
        object : AliOssUpload.OssUploadListener {
            override fun onSuccess(fileUrl: String, objectKey: String, localFilePath: String) {
                Log.d(TAG, "单个文件上传成功:$localFilePath")
                // 单个文件成功回调
                val file = mUploadFiles.find { it.availablePath == localFilePath }
                mResultListener?.onSuccessItem(file)
                // 从待上传列表移除
                val iterator = mUploadFiles.iterator()
                while (iterator.hasNext()) {
                    val it = iterator.next()
                    if (it.availablePath == localFilePath) {
                        iterator.remove()
                    }
                }
                itemResult()
            }

            override fun onUploading(objectKey: String, localFilePath: String) {
                Log.d(TAG, "单个文件上传中:$localFilePath")
                val file = mUploadFiles.find { it.availablePath == localFilePath }
                mResultListener?.onUploading(file)
            }

            override fun onFailure(exception: AliOssException, localFilePath: String) {
                Log.d(TAG, "单个文件上传失败:$localFilePath")
                // 单个文件失败回调
                val file = mUploadFiles.find { it.availablePath == localFilePath }
                mResultListener?.onFailItem(file)
                itemResult()
            }
        }
    }

    /**
     * 清空待上传的文件
     */
    fun cleanUploadFiles() {
        mUploadFiles.clear()
    }

    /**
     * 添加待上传的文件
     *
     * @param mediaFiles 需要上传的文件集合
     */
    fun addUploadFiles(mediaFiles: List<MediaSelectorFile>?) {
        mediaFiles?.let {
            // 新添加的都当做失败
            mUploadFiles.addAll(it)
        }
    }

    /**
     * 设置最终结果监听
     *
     * @param listener 结果监听回调
     */
    fun setResultListener(listener: ResultListener) {
        mResultListener = listener
    }

    /**
     * 开始上传文件
     *
     * @param type 使用哪种文件路径
     */
    fun executeUpload(type: Int = AVAILABLE_PATH) {
        mUploadRemainCount = mUploadFiles.size
        // 待上传的文件
        mUploadFiles?.forEach {
            val filePath = when (type) {
                AVAILABLE_PATH -> it.availablePath
                REAL_PATH -> it.realPath
                COMPRESS_PATH -> it.compressPath
                else -> it.realPath
            }
            Log.d(TAG, "objectKey:${it.objectKey}--filePath:$filePath")
            mOssUpload?.executeUpload(it.objectKey, filePath ?: "")
        }
    }

    /**
     * 每个文件上传结果处理
     */
    private fun itemResult() {
        mUploadRemainCount -= 1
        if (mUploadRemainCount < 1) {
            mResultListener?.onFinalResult(mUploadFiles.isNullOrEmpty())
        }
    }


    /**
     * 上传结果监听器
     */
    abstract class ResultListener {

        /**
         * 上传中的文件
         */
        open fun onUploading(file: MediaSelectorFile?) {

        }

        /**
         * 单个失败
         */
        open fun onFailItem(file: MediaSelectorFile?){

        }

        /**
         * 单个成功
         */
        open fun onSuccessItem(file: MediaSelectorFile?){

        }

        /**
         * 最终结果
         */
        abstract fun onFinalResult(success: Boolean)
    }

}