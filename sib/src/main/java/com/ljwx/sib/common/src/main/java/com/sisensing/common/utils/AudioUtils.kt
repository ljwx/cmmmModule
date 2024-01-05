package com.sisensing.common.utils

import android.content.Context
import android.media.AudioManager

object AudioUtils {

    fun isSilenceModel(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val model = audioManager.ringerMode
        return model != AudioManager.RINGER_MODE_NORMAL
    }

    fun isHasVolume(context: Context) {

    }

    fun getMusicVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val music = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val alarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
        Log.d("ljwx2", "媒体音量:$music,提醒音量:$alarm")
        return music
    }

    fun setMusicVolume(context: Context, volume: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

}