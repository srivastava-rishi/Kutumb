package com.example.kutu.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.kutu.R
import com.example.kutu.base.BaseActivity
import com.example.kutu.data.db.database.AddressDatabase
import com.example.kutu.data.db.entity.AddressEntity
import com.example.kutu.databinding.ActivitySplashBinding
import com.example.kutu.main.MainActivity
import com.example.kutu.util.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var addressDatabase: AddressDatabase

    var checkFlag: Boolean =  false

     var TAG = "@SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        initTheme()
        requestPermission()
    }


    private fun readSms() =
        ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED

    private fun receiveSms() =
        ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED


    private fun requestPermission(){

        var permissionRequest = mutableListOf<String>()

        if (!readSms()){
            permissionRequest.add(Manifest.permission.READ_SMS)
        }
        if (!receiveSms()) {
            permissionRequest.add(Manifest.permission.RECEIVE_SMS)
           }

        if (permissionRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionRequest.toTypedArray(),0)
        }else{
            getAddresses()
            gotoMainActivity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if(requestCode == 0 && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                checkFlag = false
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                   checkFlag = true
                }
            }
            if (checkFlag){
                getAddresses()
                gotoMainActivity()
            }

        }

    }

    private fun initTheme(){
        window.statusBarColor = resources.getColor(R.color.lightBlack)
        window.navigationBarColor = resources.getColor(R.color.lightBlack)
    }

    private fun gotoMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000
        )
    }

    @SuppressLint("Range")
    fun getAddresses() {


        val uriSmsUri = Uri.parse("content://sms/inbox")
        val cur: Cursor? = contentResolver.query(uriSmsUri, null, null, null, null)

        while (cur!!.moveToNext()) {
            val add: String =  cur.getString(cur.getColumnIndexOrThrow("address")).toString();
            val threadId: String? =  cur.getString(cur.getColumnIndexOrThrow("thread_id")).toString();

            val addressEntity = AddressEntity(
                addressId = add
            )

            val addressDao = addressDatabase.addressDao()

            Log.d(TAG, "getNumbers: $addressEntity")
            Log.d(TAG, "getNumbers: $threadId")

            CoroutineScope(Dispatchers.IO).launch {
                addressDao.insertMessageAddress(addressEntity)
            }

        }


    }

}