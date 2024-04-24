package com.ljwx.basepermission

import com.blankj.utilcode.util.PermissionUtils

object PermissionPoolUtils {

    private val permissionsPool = LinkedHashMap<String, Array<String>>()

    fun addPermissions(tag: String, vararg permission: String) {
        permissionsPool.put(tag, permission.toList().toTypedArray())
    }

    fun removePermission(tag: String) {
        permissionsPool.remove(tag)
    }

    fun startRequestPermissions(callback: PermissionUtils.SingleCallback) {
        if (permissionsPool.isEmpty()) {
            return
        }
        val tag = permissionsPool.keys.first()
        val permissions = permissionsPool[tag]!!
        PermissionUtils.permission(*permissions)
            .callback { isAllGranted, granted, deniedForever, denied ->
                callback.callback(isAllGranted, granted, deniedForever, denied)
                removePermission(tag)
            }
    }

    private fun request() {
        if (permissionsPool.isEmpty()) {
            return
        }
        val tag = permissionsPool.keys.first()
        val permissions = permissionsPool[tag]!!
        PermissionUtils.permission(*permissions)
            .callback { isAllGranted, granted, deniedForever, denied ->
//                callback.callback(isAllGranted, granted, deniedForever, denied)
                removePermission(tag)
                request()
            }
    }

    interface PermissionCallback {
        fun onResult(
            isAllGranted: Boolean, granted: List<String>,
            deniedForever: List<String>, denied: List<String>,
            permissionTag: String,
        )
    }

}