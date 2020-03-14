package com.example.funcamera

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val REQUEST_CODE = 1234

    //widgets

    //vars
    private var mPermission:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    private fun init(){
        if (mPermission){
            if (checkCameraHardware(this)){
                // Open the Camera
                Log.d(TAG, "init: opening the camera fragment.");
            }else{
                showSnackBar("You need a camera to use this application", Snackbar.LENGTH_INDEFINITE);
            }
        }else{
            verifyPermissions()
        }
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context):Boolean{

        if(context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            //Device has camera
            return true
        }
        return false
    }

    public fun verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        val permissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        )

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                permissions[0])== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.applicationContext,
            permissions[1])== PackageManager.PERMISSION_GRANTED){
            mPermission = true
        }else{
            ActivityCompat.requestPermissions(this@MainActivity,permissions,REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE){
            if (mPermission){
                init()
            }else{
                verifyPermissions()
            }
        }
    }

    private fun showSnackBar(title:String,length:Int){
        val view = this.findViewById<View>(android.R.id.content).rootView
        Snackbar.make(view,title,length).show()
    }
}
