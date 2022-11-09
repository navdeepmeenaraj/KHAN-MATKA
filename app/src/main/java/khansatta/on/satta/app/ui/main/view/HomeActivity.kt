package khansatta.on.satta.app.ui.main.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.PaPaDetails
import khansatta.on.satta.app.databinding.*
import khansatta.on.satta.app.ui.main.viewmodel.MainViewModel
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Constants.APP_LINK
import khansatta.on.satta.app.basic_utils.Constants.USER_MOBILE
import khansatta.on.satta.app.basic_utils.Constants.USER_NAME
import com.google.android.material.navigation.NavigationView
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarBinding: AppBarMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHeaderMainBinding: NavHeaderMainBinding
    lateinit var pointsTextView: TextView

    private val secondViewModel: SecondViewModel by viewModels()
    private val viewmodel: MainViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        appBarBinding = binding.appBar
        navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        val view = binding.root
        setContentView(view)
        userPointsObserver()

        pointsTextView = appBarBinding.walletActionBar
        getUserPoints()
        viewmodel.paymentDetails()
        observePaymentDetails()

        navHeaderMainBinding.userName.text = Prefs.getString(USER_NAME, "Unknown")
        navHeaderMainBinding.userMobile.text = Prefs.getString(USER_MOBILE, "Unknown")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.profileFragment, R.id.historyFragment
            ), drawerLayout
        )

        appBarBinding.walletActionBar.setOnClickListener {
            navController.navigate(R.id.walletFragment)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.itemIconTintList = null
        hideNavigationDrawerItems(navView)




        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.shareAppButton -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        getString(R.string.share_app) + APP_LINK + packageName
                    )
                    startActivity(Intent.createChooser(intent, "Share to "))
                }

                R.id.addPointsDrawer -> {
                    startActivity(Intent(this, PActivity::class.java))
                }

                R.id.rateUsButton -> {
                    val uri: Uri = Uri.parse("market://details?id=$packageName")
                    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                    goToMarket.addFlags(
                        Intent.FLAG_ACTIVITY_NO_HISTORY or
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                    )
                    try {
                        startActivity(goToMarket)
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(APP_LINK + packageName)
                            )
                        )
                    }
                }

                R.id.logoutButton -> {
                    Prefs.putString(Constants.PREFS_TOKEN, "")
                    Prefs.putInt(Constants.PREFS_USER_ID, 0)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }


            NavigationUI.onNavDestinationSelected(menuItem, navController)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }


    private fun hideNavigationDrawerItems(navView: NavigationView) {
        val isVerified = BasicUtils.checkIfUserIsVerified()
        val menu = navView.menu
        if (!isVerified) {
            menu.findItem(R.id.walletFragment).isVisible = false
            menu.findItem(R.id.bidHistory).isVisible = false
            menu.findItem(R.id.winHistory).isVisible = false
            menu.findItem(R.id.gameRatesFragment).isVisible = false
            menu.findItem(R.id.instructionsFragment).isVisible = false
            menu.findItem(R.id.transferPointsFragment).isVisible = false
            menu.findItem(R.id.withdrawFragment).isVisible = false
            menu.findItem(R.id.addPointsDrawer).isVisible = false
            pointsTextView.visibility = View.INVISIBLE
        }

    }

    private fun userPointsObserver() {
        secondViewModel.points.observe(this) {
            if (it.data != null) {
                Prefs.putInt("Balance", it.data.balance)
                appBarBinding.walletActionBar.text = it.data.balance.toString()

            } else {
                appBarBinding.walletActionBar.text = Prefs.getInt("Balance", 0).toString()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun observePaymentDetails() {
        viewmodel.payment.observe(this@HomeActivity, {
            if (it.data != null) {
                val data = it.data
                viewmodel.insertPaymentDetails(
                    this,
                    PaPaDetails(
                        data.id,
                        data.upi_id,
                        data.business_name,
                        data.payment_desc,
                        data.max_amount,
                        data.min_amount,
                        data.min_withdrawal,
                        data.withdrawal_time_title,
                        data.merchant_code
                    )
                )
            }
        })
    }

    fun getUserPoints() {
        secondViewModel.getUserPoints(
            BasicUtils.bearerToken(),
            BasicUtils.userId()
        )
    }
}