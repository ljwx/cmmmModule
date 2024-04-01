package com.ljwx.baseapp.util

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object FileReadUtils {

    fun readFromLine(context: Context, path: String): String {
        var content = ""
        try {
            val fis = context.openFileInput(path)
            if (fis != null) {
                val isr = InputStreamReader(fis)
                val bufferedReader = BufferedReader(isr)
                var line = ""
                val stringBuilder = StringBuilder()
                fun read(): String? {
                    line = bufferedReader.readLine()
                    return line
                }
                while (read() != null) {
                    stringBuilder.append(line)
                }
                fis.close();
                content = stringBuilder.toString();
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return content
    }

}