package com.ljwx.basemodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljwx.network.OkHttpConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        OkHttpConfig



    }
}


open class Person {
    open fun abc() {

    }
}

open class P2 : Person() {
    override fun abc() {
        super.abc()
    }

    fun abc(a: Int) {

    }
}

