package khansatta.on.satta.app

import android.app.Application
import android.content.ContextWrapper
import com.androidnetworking.AndroidNetworking
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initSharedPrefs()
        AndroidNetworking.initialize(applicationContext)

    }


    private fun initSharedPrefs() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}
