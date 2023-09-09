package com.ljwx.basepermission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions3.RxPermissions

class AppPermissions {

    private var permission: RxPermissions? = null

    constructor(activity: FragmentActivity) {
        permission = RxPermissions(activity)
    }

    constructor(fragment: Fragment) {
        permission = RxPermissions(fragment)
    }


    fun request(vararg permissions: String) {
        //需要导入rxjava
        permission!!.request(*permissions).subscribe {
            if (it) {

            }
        }
    }

}