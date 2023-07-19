package com.ljwx.basemediaselector.alioss

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult

class AliOssUpload(context: Context, ossConfig: AliOssConfig) {

    private val TAG = this.javaClass.simpleName + "-AliOSS"

    /**
     * oss客户端
     */
    private val mOssClient by lazy {
        OSSClient(context, ossConfig.endpoint, getCredentialProvider(), getConfig())
    }

    /**
     * oss参数配置
     */
    private val ossConfig = ossConfig

    /**
     * 上传结果监听
     */
    private var mResultListener: OssUploadListener? = null

    /**
     * 身份校验
     */
    private fun getCredentialProvider(): OSSStsTokenCredentialProvider? {
        return OSSStsTokenCredentialProvider(
            ossConfig.accessKeyId,
            ossConfig.secretKeyId,
            ossConfig.securityToken)
    }

    /**
     * 上传的参数配置
     */
    private fun getConfig(): ClientConfiguration {
        val clientConfig = ClientConfiguration()
        clientConfig.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
        clientConfig.socketTimeout = 15 * 1000 // socket超时，默认15秒
        clientConfig.maxConcurrentRequest = 5 // 最大并发请求数，默认5个
        clientConfig.maxErrorRetry = 2 // 失败后最大重试次数，默认2次
        return clientConfig
    }

    /**
     * 上传的request
     */
    private fun getRequest(objectKey: String, uploadFilePath: String): PutObjectRequest {
        val putRequest = PutObjectRequest(ossConfig.bucketName, objectKey, uploadFilePath)
        return putRequest
    }

    /**
     * 设置上传结果监听
     */
    fun setResultListener(resultListener: OssUploadListener) {
        mResultListener = resultListener
    }

    /**
     * 执行上传逻辑
     */
    fun executeUpload(
        objectKey: String,
        uploadFilePath: String,
    ) {
        mResultListener?.onUploading(objectKey, uploadFilePath)
        mOssClient.asyncPutObject(getRequest(objectKey, uploadFilePath),
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {

                override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                    val fileUrl = mOssClient.presignPublicObjectURL(ossConfig.bucketName, objectKey)
                    mResultListener?.onSuccess(fileUrl, objectKey, uploadFilePath)
                }

                override fun onFailure(
                    request: PutObjectRequest?,
                    clientException: ClientException?,
                    serviceException: ServiceException?,
                ) {
                    // 请求异常。
                    clientException?.let {
                        // 本地异常，如网络异常等。
                        Log.e(TAG, "oss网络异常:$it")
                        mResultListener?.onFailure(AliOssException("oss网络异常",
                            "没有code",
                            it.message ?: "空的"),
                            uploadFilePath)
                    }
                    serviceException?.let {
                        // 服务异常。
                        Log.e(TAG, "oss服务异常:$it")
                        mResultListener?.onFailure(AliOssException("oss服务异常",
                            it.statusCode.toString(),
                            it.message ?: "空的"),
                            uploadFilePath)
                    }
                }
            })
    }

    interface OssUploadListener {
        fun onSuccess(fileUrl: String, objectKey: String, localFilePath: String)
        fun onUploading(objectKey: String, localFilePath: String)
        fun onFailure(exception: AliOssException, localFilePath: String)
    }

}