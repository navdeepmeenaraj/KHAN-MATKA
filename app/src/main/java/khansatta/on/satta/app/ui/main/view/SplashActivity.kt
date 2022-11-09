package khansatta.on.satta.app.ui.main.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.AppConfig
import khansatta.on.satta.app.ui.main.viewmodel.MainViewModel
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.bearerToken
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Status
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val secondViewModel: SecondViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        getInitialData()

        mainViewModel.checkUserStatus()

        observeInitialData()

        observeUserPoints()

        observeUserTokenCheckData()
        userStatusObserver()

    }


    private fun userStatusObserver() {
        mainViewModel.isVerified.observe(this, {
            cool(it.toString())
            if (it.data != null) {
                Prefs.putInt(Constants.IS_VERIFIED, it.data.is_verified)
            }
        })
    }


    private fun observeUserTokenCheckData() {
        mainViewModel.server.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        getUserPoints()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                }
                Status.ERROR -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                else -> cool("Loading")
            }

        })
    }

    private fun observeUserPoints() {
        secondViewModel.points.observe(this, {
            if (it.data != null) {
                Prefs.putString(Constants.USER_POINTS_PREFS, it.data.balance.toString())
            } else {
                cool("Data Error : ${it.data} ${it.message} ${it.status}")
            }
        })
    }

    private fun observeInitialData() {
        secondViewModel.config.observe(this, {
            if (it.data != null) {
                checkUserValidToken()
                saveAppConfig(it.data)
            }
        })
    }

    private fun getUserPoints() {
        secondViewModel.getUserPoints(
            bearerToken(),
            BasicUtils.userId()
        )
    }

    //#2 Step
    private fun getInitialData() {
        cool("Splash Activity Fetching App Config ")
        secondViewModel.fetchAppConfig(bearerToken())
    }


    //#1 Step
    private fun checkUserValidToken() {
        mainViewModel.checkUserToken(bearerToken())
    }

    private fun saveAppConfig(config: AppConfig) {
        Prefs.putString(Constants.PREFS_BANNER_IMAGE, config.banner_image)
        Prefs.putString(Constants.PREFS_PAYMENT_UID, config.payment_address)
        Prefs.putString(Constants.PREFS_PHONE_NUMBER, config.phone_number)
        Prefs.putString(Constants.PREFS_TELEGRAM, config.telegram)
        Prefs.putString(Constants.PREFS_WHATSAPP_NUMBER, config.whatsapp_numebr)
        Prefs.putInt(Constants.MIN_BET, config.min_bet)
        Prefs.putInt(Constants.MAX_BET, config.max_bet)
    }
}