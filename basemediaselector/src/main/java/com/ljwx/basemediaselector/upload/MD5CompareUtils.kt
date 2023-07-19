package com.ljwx.basemediaselector.upload

import com.ljwx.basemediaselector.MediaSelectorFile

object MediaFileMD5Utils {

    /**
     * 获取文件的md5集合
     *
     * @param files 文件集合
     * @return md5集合
     */
    fun getMD5List(files: List<MediaSelectorFile>?): ArrayList<String> {
        val md5List = ArrayList<String>()
        files?.forEach {
            md5List.add(it.fileMD5)
        }
        return md5List
    }

    /**
     * 移除文件集合里相同md5的文件
     *
     * @param localFiles 文件集合
     * @param remoteMD5 md5集合
     */
    fun removeSameFile(localFiles: ArrayList<MediaSelectorFile>?, remoteMD5: List<String>?) {
        val iterator = localFiles?.iterator()
        while (iterator?.hasNext() == true) {
            val file = iterator.next()
            val result: String? = remoteMD5?.find { file.fileMD5 == it }
            if (!result.isNullOrEmpty()) {
                iterator.remove()
            }
        }
    }

}