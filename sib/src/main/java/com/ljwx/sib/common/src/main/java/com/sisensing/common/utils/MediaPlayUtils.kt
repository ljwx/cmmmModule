package com.sisensing.common.utils

import android.content.Context
import android.media.AudioManager

import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.blankj.utilcode.util.Utils
import com.sisensing.common.R


object MediaPlayUtils {

    private val audioManager by lazy {
        Utils.getApp().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    @JvmStatic
    fun playResource(context: Context, @RawRes rawRes: Int, silenceVolume: Boolean = false) {
        val player = MediaPlayer.create(context, rawRes)
        var old = 0
        if (silenceVolume && audioManager.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            old = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 12, 0)
        }
//        player.setAudioStreamType(AudioManager.STREAM_ALARM)
        player.setOnCompletionListener {
            if (silenceVolume && audioManager.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, old, 0)
            }
        }
        player.start()
    }

    fun playFile(filePath: String) {
        val player = MediaPlayer()
        val path = "/sdcard/test.mp3"
        player.setDataSource(filePath)
        player.prepare()
        player.start()
    }

    @JvmStatic
    fun getRawRes(position: Int) :Int{
        val list = arrayOf(
            R.raw.sound1,
            R.raw.sound2,
            R.raw.sound3,
            R.raw.sound4,
            R.raw.sound5,
            R.raw.sound6,
            R.raw.sound7,
            R.raw.sound8,
            R.raw.sound9,
            R.raw.sound10,
        )
        return if (position < list.size) list[position] else R.raw.sound1
    }

}