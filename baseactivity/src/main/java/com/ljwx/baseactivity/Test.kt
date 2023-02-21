package com.ljwx.baseactivity

class Test{
    var aa:String? = null
}

var Test.name: String?
    get() {
        return "3"
    }
    set(value) {
        aa = value
    }

fun main() {
    val o = Test()
    o.name = null
    print(o.name)
}