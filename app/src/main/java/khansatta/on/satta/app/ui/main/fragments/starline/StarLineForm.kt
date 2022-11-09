package khansatta.on.satta.app.ui.main.fragments.starline

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import khansatta.on.satta.app.R
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.ui.main.viewmodel.StarlineViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.BasicUtils.dpDigits
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showSuccessSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.singleDigits
import khansatta.on.satta.app.basic_utils.BasicUtils.spDigits
import khansatta.on.satta.app.basic_utils.BasicUtils.tpDigits
import khansatta.on.satta.app.basic_utils.BasicUtils.userId
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Constants.DP
import khansatta.on.satta.app.basic_utils.Constants.PREF_STAR_MAR_TYPE
import khansatta.on.satta.app.basic_utils.Constants.SINGLE
import khansatta.on.satta.app.basic_utils.Constants.SP
import khansatta.on.satta.app.basic_utils.Constants.TP
import khansatta.on.satta.app.basic_utils.Status
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.set


@AndroidEntryPoint
class StarLineForm : Fragment(R.layout.fragment_star_line_form) {
    private val vm: StarlineViewModel by viewModels()
    private val vm2: SecondViewModel by viewModels()
    private lateinit var mView: View
    private lateinit var _context: Context
    private lateinit var inputPoints: AutoCompleteTextView
    private lateinit var inputDigit: AutoCompleteTextView
    private lateinit var currentBidDate: AutoCompleteTextView
    private lateinit var alertWindow: LinearLayout
    private lateinit var walletPoints: TextView
    private lateinit var alertStatus: TextView
    private lateinit var bidButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        setType(view)
        currentBidDate.setText(BasicUtils.getCurrentDate())
        getUserPoints()
        setOnClick()
        setProps()
        getCurrentDate()

        observeWalletPoints()
        observePostBidData()
        setAutoCompleteAdapter()
    }

    private fun observeWalletPoints() {
        vm2.points.observe(viewLifecycleOwner, Observer {
            if (it.data != null) {
                walletPoints.text = "Available Points : ${it.data.balance}"
                Prefs.putInt("user_points", it.data.balance)
            } else {
                walletPoints.text = "Available Points : 0"
            }
        })
    }

    private fun observePostBidData() {
        vm.bet.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    bidButton.isEnabled = true
                    inputDigit.setText("")
                    inputPoints.setText("")
                    it.data?.let {
                        requireActivity().hideSoftKeyboard(mView)
                        requireActivity().showSuccessSnackBar("Bid Placed Successfully")
                        getUserPoints()
                    }
                }
                Status.LOADING -> {
                    bidButton.isEnabled = false
                    cool("Bid Place in Progress")
                }
                Status.ERROR -> {
                    bidButton.isEnabled = true
                    requireActivity().hideSoftKeyboard(mView)
                    requireActivity().showErrorSnackBar("Unable to Place Bid")
                }
            }

        })

    }

    private fun setAutoCompleteAdapter() {
        val value = Prefs.getString(PREF_STAR_MAR_TYPE, "").toString().toInt()
        when (value) {
            SINGLE -> {
                inputDigit.setAdapter(
                    ArrayAdapter<String>(
                        _context,
                        android.R.layout.simple_dropdown_item_1line,
                        singleDigits()
                    )
                )

            }
            SP -> {
                inputDigit.setAdapter(
                    ArrayAdapter<Int>(
                        _context,
                        android.R.layout.simple_dropdown_item_1line,
                        spDigits()
                    )
                )
            }
            DP -> {
                inputDigit.setAdapter(
                    ArrayAdapter<Int>(
                        _context,
                        android.R.layout.simple_dropdown_item_1line,
                        dpDigits()
                    )
                )
            }
            TP -> {
                inputDigit.setAdapter(
                    ArrayAdapter<String>(
                        _context,
                        android.R.layout.simple_dropdown_item_1line,
                        tpDigits()
                    )
                )
            }
        }
    }

    private fun getCurrentDate() {
        if (vm.date.value == null) {
            vm.currentDate()
            vm.date.observe(viewLifecycleOwner, Observer {
                if (it.data != null) {
                    Prefs.putString("date", it.data)
                }
            })
        } else {
            vm.date.observe(viewLifecycleOwner, Observer {
                if (it.data != null) {
                    currentBidDate.setText(it.data)
                }
            })
        }
    }

    private fun setOnClick() {
        bidButton.setOnClickListener {
            if (validateUserInputs()) {
                prepareUserBid()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserPoints() {
        vm2.getUserPoints(
            BasicUtils.bearerToken(),
            userId()
        )
    }


    private fun prepareUserBid() {
        val starBidMap = HashMap<String, Any?>()
        starBidMap["user_id"] = userId()
        starBidMap["market_id"] = providePrefs(1)
        starBidMap["bet_digit"] = inputDigit.text.toString()
        starBidMap["bet_amount"] = inputPoints.text.toString()
        starBidMap["bet_type"] = providePrefs(3)
        starBidMap["bet_date"] = BasicUtils.getCurrentDate()
        if (starBidMap["bet_amount"].toString().toInt() > Prefs.getInt("user_points", 0)) {
            showAlertWindow("Insufficient Points")
        } else if (!verifyDigits(
                starBidMap["bet_digit"].toString().toInt(),
                starBidMap["bet_type"].toString().toInt()
            )
        ) {
            showAlertWindow("Invalid digits for this Game")
        } else {
            placeStarBid(starBidMap)
        }
    }

    private fun placeStarBid(map: java.util.HashMap<String, Any?>) {
        vm.placeStarBet(map)
    }

    private fun verifyDigits(inputDigits: Int, betType: Int): Boolean {
        return when (betType) {
            SINGLE -> {
                inputDigits.toString() in singleDigits()
            }
            SP -> {
                inputDigits in spDigits()
            }
            DP -> {
                inputDigits in dpDigits()
            }
            TP -> {
                true
            }
            else -> {
                false
            }
        }
    }

    private fun validateUserInputs(): Boolean {

        val digits = inputDigit.text.toString()
        val points = inputPoints.text.toString()

        if (digits.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Digits")
            return false
        }

        if (points.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Amount")
            return false
        }

        val bool = BasicUtils.isBetween(points.toInt())
        if (!bool) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.MIN_BET_MSG)
            return false
        }

        val pointsP = Prefs.getInt("Balance", 0)
        if (pointsP < points.toInt()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.POINTS_INSUFFICIENT)
            return false
        }

        return true

    }

    private fun showAlertWindow(message: String) {
        alertWindow.visibility = View.VISIBLE
        CoroutineScope(
            Dispatchers.Main
        ).launch {
            alertStatus.text = message
            delay(2000)
            alertWindow.visibility = View.GONE
        }
    }

    private fun providePrefs(id: Int): String {
        return when (id) {
            1 -> {
                Prefs.getInt("star_market_id", 1).toString()
            }
            2 -> {
                Prefs.getString(
                    "star_market_time",
                    "null"
                )
            }
            else -> {
                Prefs.getString(PREF_STAR_MAR_TYPE, "")
            }
        }
    }

    private fun setProps() {
        val marketType = Prefs.getString(PREF_STAR_MAR_TYPE, "").toString().toInt()
        if (marketType != 90) {
            if (marketType == 1) {
                inputDigit.filters = arrayOf<InputFilter>(LengthFilter(1))
            } else {
                inputDigit.filters = arrayOf<InputFilter>(LengthFilter(3))
            }
        }
    }

    private fun setType(view: View) {
        view.apply {
            inputDigit = findViewById(R.id.star_bid_digits)
            inputPoints = findViewById(R.id.star_bid_points)
            currentBidDate = findViewById(R.id.star_bid_date)
            bidButton = findViewById(R.id.star_place_bid)
            alertWindow = findViewById(R.id.star_alert_window)
            alertStatus = findViewById(R.id.star_text_status)
            walletPoints = findViewById(R.id.star_user_points)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }
}