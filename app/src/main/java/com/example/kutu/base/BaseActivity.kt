package com.example.kutu.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.kutu.R
import com.example.kutu.databinding.ActivityBaseBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {


     companion object {
         lateinit var binding: ActivityBaseBinding
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base)
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(context))
    }
}