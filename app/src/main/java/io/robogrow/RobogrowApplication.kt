package io.robogrow

import android.app.Application
import io.robogrow.networking.VolleySingleton

class RobogrowApplication : Application() {

    companion object {
        lateinit var queue: VolleySingleton
    }

    override fun onCreate() {
        super.onCreate()
        queue = VolleySingleton.getInstance(this)
    }

}