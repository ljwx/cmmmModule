package com.sisensing.common.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import com.blankj.utilcode.util.LogUtils
import java.util.*

/**
 *@author y.xie
 *@date 2022/11/7 16:39
 *@desc
 *
 */
object SystemTTS {
    private const val TAG = "SystemTTS"
    private var instance: SystemTTS? = null
    private var textToSpeech: TextToSpeech? = null
    private var isSupport = true

    fun init(context: Context) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = textToSpeech?.setLanguage(Locale.CHINA)!!
                textToSpeech?.setPitch(1.0f) // 设置音调
                textToSpeech?.setSpeechRate(1.0f) // 设置语速
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    isSupport = false
                    LogUtils.d(TAG, "系统不支持中文语音播报")
                }
            }
        }
    }


    fun speak(text: String?) {
        if (!isSupport) {
            return
        }
        if (textToSpeech != null) {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null,UUID.randomUUID().toString())
        }
    }

    fun destroy() {
        if (textToSpeech != null) {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
        instance = null
    }
}