package com.ljwx.baseutils

import android.content.Context
import android.media.AudioManager

object AudioManagerUtils {

    fun isMicBusy(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val isBusy = audioManager.isWiredHeadsetOn || audioManager.isBluetoothScoOn
        val isRecording =
            audioManager.mode == AudioManager.MODE_IN_CALL || audioManager.mode == AudioManager.MODE_IN_COMMUNICATION
        return isBusy || isRecording
    }

}