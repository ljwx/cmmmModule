package com.ljwx.basemediaselector

import com.blankj.utilcode.util.FileUtils
import com.luck.picture.lib.entity.LocalMedia
import java.util.*

class MediaSelectorFile(item: LocalMedia) {

    private val item = item


    val id = item.id

    /**
     * original path
     * 在android12 上为content://media/...
     */
    var path: String? = item.path

    /**
     * The real path，But you can't get access from AndroidQ
     * android 12 为真实文件地址
     */
    var realPath: String? = item.realPath

    /**
     * # Check the original button to get the return value
     * original path
     * 在Android12 上为null
     */
    var originalPath: String? = item.originalPath

    /**
     * compress path
     * 未开启压缩时,为null
     */
    var compressPath = item.compressPath

    /**
     * app sandbox path
     */
    var sandboxPath: String? = item.sandboxPath

    /**
     * The media resource type
     */
    var mimeType: String? = item.mimeType

    /**
     * image or video width
     *
     *
     * # If zero occurs, the developer needs to handle it extra
     */
    var width = item.width

    /**
     * image or video height
     *
     *
     * # If zero occurs, the developer needs to handle it extra
     */
    var height = item.height

    /**
     * file size
     */
    var size: Long = item.size

    /**
     * file name
     */
    var fileName: String? = item.fileName

    /**
     * Parent  Folder Name
     */
    var parentFolderName: String? = item.parentFolderName

    /**
     * media position of list
     * 相册列表中的位置
     */
    val albumPosition = item.position

    /**
     * 选中顺序
     */
    var sortPosition = -1

    /**
     * 文件的MD5
     */
    val fileMD5 =
        FileUtils.getFileMD5ToString(if (item.isCompressed) availablePath else item.realPath)


    /**
     * get real and effective resource path
     *
     * @return
     */
    val availablePath: String
        get() {
            var path: String? = path
            if (item.isCut()) {
                path = item.cutPath
            }
            if (item.isCompressed) {
                path = compressPath
            }
            if (item.isToSandboxPath) {
                path = sandboxPath
            }
            if (item.isOriginal) {
                path = originalPath
            }
            if (item.isWatermarkPath) {
                path = item.watermarkPath
            }
            return path ?: ""
        }

    /**
     * oss objectKey
     */
    val objectKey by lazy {
        "" + System.currentTimeMillis() +
                "_" +
                Random().nextInt(99999) +
                "." + FileUtils.getFileExtension(item.fileName)
    }

}