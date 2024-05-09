package com.ljwx.baseaosversion

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

object OSDiffFilePathUtils {

    fun getRealPathFromURINew(activity: Activity, imageUri: Uri): String? {
        // 如果你需要获取File对象，可以使用ContentResolver来查询。
        // 注意：从Android 10开始，直接访问设备上的文件路径被认为是不安全的。
        // 如果你需要持久访问文件，请考虑使用MediaStore API或者请求用户授权。
        var filePath: String? = null
        try {
            val resolver: ContentResolver = activity.getContentResolver()
            val cursor = resolver.query(imageUri, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    filePath = cursor.getString(columnIndex)
                    //File imageFile = new File(filePath);
                    // 使用imageFile对象
                }
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return filePath
    }

}