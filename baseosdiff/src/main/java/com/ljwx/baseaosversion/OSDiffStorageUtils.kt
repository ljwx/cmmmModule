package com.ljwx.baseaosversion

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IntDef
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import java.io.File


object OSDiffStorageUtils {

    /**
     * 6.0之前不需要动态申请权限，只需要在manifest文件中申请即可
     * 从6.0之后，app需要动态申请权限，即弹框询问用户，是否给用户授权
     * Android 11以后，对权限的控制进一步收紧，很多的权限申请发生改变，例如，此前操作文件，只需要声明读写权限即可，
     * 但是现在划分了图片、音频、视频等等，并且操作普通文件的权限也变为MANAGE_EXTERNAL_STORAGE
     *
     * <!--存储权限-->
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>
     *
     * <application
     * 	...
     *     android:requestLegacyExternalStorage="true"
     *     android:usesCleartextTraffic="true"
     *     ...>
     */

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun storagePermissionsGranted(context: Context): Boolean {
        if (OSVersionUtils.greaterOrEqual11()) {
            if (Environment.isExternalStorageManager()) {
                return true
            }
        } else {
            if (OSVersionUtils.greaterOrEqual6()) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                }
            } else {
                return true
            }
        }
        return false
    }

    fun requestStoragePermissions(
        activity: ComponentActivity,
        resultCallback: ActivityResultCallback<Map<String, Boolean>>
    ) {
        if (OSVersionUtils.greaterOrEqual11()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            activity.startActivity(intent)
        } else {
            val permissionLauncher = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
                resultCallback
            )
            permissionLauncher.launch(PERMISSIONS_STORAGE)
        }
    }

    const val Pictures = 1
    const val Movies = 2
    const val Music = 3
    const val Documents = 4
    const val Download = 5
    const val DCIM = 6

    @IntDef(Pictures, Movies, Music, Documents, Download, DCIM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class StorageType

    fun getPublicFilePath(@StorageType type: Int): File {
        val fileType = when (type) {
            Pictures -> Environment.DIRECTORY_PICTURES
            Movies -> Environment.DIRECTORY_MOVIES
            Music -> Environment.DIRECTORY_MUSIC
            Documents -> Environment.DIRECTORY_DOCUMENTS
            Download -> Environment.DIRECTORY_DOWNLOADS
            DCIM -> Environment.DIRECTORY_DCIM
            else -> Environment.DIRECTORY_DOWNLOADS
        }
        return Environment.getExternalStoragePublicDirectory(fileType)
    }

}