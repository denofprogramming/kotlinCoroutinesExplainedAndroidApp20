package com.denofprogramming.mycoroutineapplication

import android.app.Application
import timber.log.Timber

class MyCoroutineApp : Application() {


    init {
        Timber.plant(Timber.DebugTree())
        Timber.i("My Coroutine App Application instantiated...")
    }

}