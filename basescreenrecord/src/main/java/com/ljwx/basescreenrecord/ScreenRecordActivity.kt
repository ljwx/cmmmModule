package com.ljwx.basescreenrecord

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ljwx.basescreenrecord.databinding.ActivityScreenRecordSimpleBinding


class ScreenRecordActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1
    private var mMediaProjectionManager: MediaProjectionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityScreenRecordSimpleBinding>(this, R.layout.activity_screen_record_simple)

        checkPermission(this); //检查权限

        mMediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?;

        binding.start.setOnClickListener {
            startRecorder(null)
        }
        binding.stop.setOnClickListener {
            stopRecorder(null)
        }
    }

    fun startRecorder(view: View?) {
        createScreenCapture()
    }

    fun stopRecorder(view: View?) {
        val service = Intent(this, ScreenRecordService::class.java)
        stopService(service)
    }

    private fun createScreenCapture() {
        val captureIntent = mMediaProjectionManager!!.createScreenCaptureIntent()
        startActivityForResult(captureIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE && resultCode === RESULT_OK) {
            try {
                Toast.makeText(this, "允许录屏", Toast.LENGTH_SHORT).show()
                val service = Intent(this, ScreenRecordService::class.java)
                service.putExtra("resultCode", resultCode)
                service.putExtra("data", data)
                startService(service)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "拒绝录屏", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPermission(activity: AppCompatActivity?) {
        if (Build.VERSION.SDK_INT >= 23) {
            val checkPermission =
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        + ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                        + ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                //动态申请
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)
            }
        }
    }

}