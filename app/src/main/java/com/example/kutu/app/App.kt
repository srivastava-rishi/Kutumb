package com.example.kutu.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initCalligraphyViewPump()
    }

    private fun initCalligraphyViewPump() {

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf").build()
                    )
                )
                .build()
        )

    }

}