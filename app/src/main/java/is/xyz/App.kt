package `is`.xyz

import android.app.Application
import `is`.xyz.mpv.config.PrefManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PrefManager.init(applicationContext)
    }
}
