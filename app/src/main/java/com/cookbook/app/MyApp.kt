package com.cookbook.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp:Application() {

    var fromFilter: Boolean = false
}