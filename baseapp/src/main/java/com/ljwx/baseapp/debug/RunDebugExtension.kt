package com.ljwx.baseapp.debug


inline fun debugRun(debug: Boolean = DebugUtils.isDebug(), run: () -> Unit) {
    if (debug) {
        run.invoke()
    }
}

inline fun <reified T : RunDebugProxy> debugRun(type: Int, debug: Boolean = DebugUtils.isDebug()) {
    if (debug) {
        val className = T::class.java.name
        val classType = Class.forName(className).kotlin
        val constructor = classType.constructors.first()
        val instance = constructor.call(type)
        if (instance is RunDebugProxy) {
            instance.run()
        }
    }
}