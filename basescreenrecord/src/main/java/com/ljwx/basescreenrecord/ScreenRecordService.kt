package com.ljwx.basescreenrecord

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Environment
import android.os.IBinder
import android.util.Log
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ScreenRecordService : Service() {

    private val TAG = "ScreenRecordService"

    /**
     * 是否为标清视频
     */
    private val isVideoSd = false

    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mScreenDensity = 0

    private var mResultCode = 0
    private var mResultData: Intent? = null

    private var mMediaProjection: MediaProjection? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var mVirtualDisplay: VirtualDisplay? = null

    fun ScreenRecordService() {}

    override fun onBind(intent: Intent?): IBinder? {
        // TODO: Return the communication channel to the service.
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mResultCode = intent.getIntExtra("resultCode", 1)
        mResultData = intent.getParcelableExtra("data")
        getScreenBaseInfo()
        mMediaProjection = createMediaProjection()
        mMediaRecorder = createMediaRecorder()
        mVirtualDisplay =
            createVirtualDisplay() // 必须在mediaRecorder.prepare() 之后调用，否则报错"fail to get surface"
        mMediaRecorder!!.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        if (mVirtualDisplay != null) {
            mVirtualDisplay!!.release()
            mVirtualDisplay = null
        }
        if (mMediaRecorder != null) {
            mMediaRecorder!!.setOnErrorListener(null)
            mMediaProjection!!.stop()
            mMediaRecorder!!.reset()
        }
        if (mMediaProjection != null) {
            mMediaProjection!!.stop()
            mMediaProjection = null
        }
    }


    /**
     * 获取屏幕相关数据
     */
    private fun getScreenBaseInfo() {
        mScreenWidth = 1080
        mScreenHeight = 2400
        mScreenDensity = 500
    }

    private fun createMediaProjection(): MediaProjection? {
        Log.i(TAG, "Create MediaProjection")
        return (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).getMediaProjection(
            mResultCode,
            mResultData!!)
    }


    private fun createMediaRecorder(): MediaRecorder? {
        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        val curDate = Date(System.currentTimeMillis())
        val curTime: String = formatter.format(curDate).replace(" ", "")
        var videoQuality = "HD"
        if (isVideoSd) videoQuality = "SD"
        Log.i(TAG, "Create MediaRecorder")
        val mediaRecorder = MediaRecorder()
        //        if(isAudio) mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        val fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            .toString() + "/" + videoQuality + curTime + ".mp4"
        Log.d(TAG, fileName)
        mediaRecorder.setOutputFile(fileName)
        mediaRecorder.setVideoSize(mScreenWidth,
            mScreenHeight) //after setVideoSource(), setOutFormat()
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264) //after setOutputFormat()
        //        if(isAudio) mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);  //after setOutputFormat()
        val bitRate: Int
        bitRate = if (isVideoSd) {
            mediaRecorder.setVideoEncodingBitRate(mScreenWidth * mScreenHeight)
            mediaRecorder.setVideoFrameRate(30)
            mScreenWidth * mScreenHeight / 1000
        } else {
            mediaRecorder.setVideoEncodingBitRate(5 * mScreenWidth * mScreenHeight)
            mediaRecorder.setVideoFrameRate(60) //after setVideoSource(), setOutFormat()
            5 * mScreenWidth * mScreenHeight / 1000
        }
        try {
            mediaRecorder.prepare()
        } catch (e: IllegalStateException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return mediaRecorder
    }

    private fun createVirtualDisplay(): VirtualDisplay? {
        Log.i(TAG, "Create VirtualDisplay")
        return mMediaProjection!!.createVirtualDisplay(TAG,
            mScreenWidth,
            mScreenHeight,
            mScreenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mMediaRecorder!!.surface,
            null,
            null)
    }

}