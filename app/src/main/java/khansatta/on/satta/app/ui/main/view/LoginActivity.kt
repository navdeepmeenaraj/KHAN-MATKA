package khansatta.on.satta.app.ui.main.view

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.AppConfig
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.Constants
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val secondViewModel: SecondViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        observeServerData()

    }

    fun fetchAppConfig() {
        BasicUtils.cool("Fetching App Config")
        secondViewModel.fetchAppConfig("")
    }

    private fun observeServerData() {
        secondViewModel.config.observe(this, {
            if (it.data != null) {
                BasicUtils.cool("Saving App Config")
                saveAppConfig(it.data)
            }
        })
    }

    private fun saveAppConfig(config: AppConfig) {
        BasicUtils.cool(config.toString())
        Prefs.putString(Constants.PREFS_BANNER_IMAGE, config.banner_image)
        Prefs.putString(Constants.PREFS_PAYMENT_UID, config.payment_address)
        Prefs.putString(Constants.PREFS_PHONE_NUMBER, config.phone_number)
        Prefs.putString(Constants.PREFS_WHATSAPP_NUMBER, config.whatsapp_numebr)
    }

}