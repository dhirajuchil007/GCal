package com.velocityappsdj.gcal.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class GCalApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}