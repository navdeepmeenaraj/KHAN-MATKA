package khansatta.on.satta.app.ui.main.fragments.bids

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import khansatta.on.satta.app.databinding.FragmentTpBinding
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.ui.main.view.LoginActivity
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.getMinMaxBetMessage
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showInfoSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showSuccessSnackBar
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Status
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TriplePanelFragment : Fragment() {

    private val secondViewModel: SecondViewModel by viewModels()

    private lateinit var binding: FragmentTpBinding
    private lateinit var mView: View
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        binding.textViewDate.text = BasicUtils.getCurrentDate()
        initFormTitle()
        setAutoCompleteDigits()
        initMarketSettings()
        observeMarketData()
        observePostBidData()
        initOnClick()
    }

    private fun initOnClick() {
        binding.buttonSubmitBet.setOnClickListener {
            validateBidData()
        }
    }

    private fun validateBidData() {
        val session = getCheckedRadioButton()

        if (session == "null") {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.NO_SESSION)
            return
        }

        val inputDigit = binding.editInputDigits.text.toString()
        if (inputDigit.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Digit")
            return
        }

        val inputPoint = binding.editInputAmount.text.toString()
        if (inputPoint.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Points")
            return
        }

        val bool = BasicUtils.isBetween(inputPoint.toInt())
        if (!bool) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(getMinMaxBetMessage())
            return
        }

        val points = Prefs.getInt("Balance", 0)
        if (points < inputPoint.toInt()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.POINTS_INSUFFICIENT)
            return
        }

        if (!verifyBidDigits(inputDigit)) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.INVALID_DIGITS)
            return
        }

        initBidHashMap(inputDigit, inputPoint, session)
    }

    private fun initBidHashMap(inputDigit: String, inputPoint: String, session: String) {
        val map = hashMapOf<String, Any?>()
        map[Constants.HASH_USER_ID] = BasicUtils.userId()
        map[Constants.HASH_BET_DIGIT] = inputDigit
        map[Constants.HASH_MARKET] = Prefs.getInt(Constants.PREFS_MARKET_ID, 1)
        map[Constants.HASH_BET_AMT] = inputPoint
        map[Constants.HASH_TYPE] = Constants.TP_MAIN
        map[Constants.HASH_SESSION] = session
        map[Constants.HASH_DATE] = BasicUtils.getCurrentDate()

        BasicUtils.cool("Single Bet Hash Map : $map ")

        initPlaceUserBet(map)
    }

    private fun initPlaceUserBet(map: HashMap<String, Any?>) {
        binding.buttonSubmitBet.isEnabled = false
        binding.progress.visibility = View.VISIBLE
        secondViewModel.placeBet(map)
    }

    private fun getCheckedRadioButton(): String {

        val sessionOpenRadioButtonId = binding.openCircle.id
        val sessionCloseRadioButtonId = binding.closeCircle.id
        return when (binding.sessionRadioGroup.checkedRadioButtonId) {
            sessionOpenRadioButtonId -> {
                Constants.SESSION_OPEN
            }
            sessionCloseRadioButtonId -> {
                Constants.SESSION_CLOSE
            }
            else -> {
                Constants.SESSION_NULL
            }
        }
    }

    private fun observePostBidData() {
        secondViewModel.betPlace.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    binding.buttonSubmitBet.isEnabled = true
                    binding.editInputDigits.setText("")
                    binding.editInputAmount.setText("")
                    it.data?.let { data ->
                        requireActivity().hideSoftKeyboard(mView)
                        requireActivity().showSuccessSnackBar(data.error_msg)
                    }
                    (activity as HomeActivity?)?.getUserPoints()
                }
                Status.LOADING -> {
                    BasicUtils.cool("Bet Place in Progress")
                }
                Status.ERROR -> {
                    binding.buttonSubmitBet.isEnabled = true
                    binding.progress.visibility = View.GONE

                    val error: String =
                        if (it.message?.toInt() == Constants.ERROR_UNAUTHORIZED) {
                            Constants.INCORRECT_CREDS
                        } else {
                            Constants.UNKNOWN_ERROR
                        }
                    requireActivity().showErrorSnackBar(error)
                    if (error == Constants.INCORRECT_CREDS) {
                        requireActivity().startActivity(Intent(mContext, LoginActivity::class.java))
                        requireActivity().finish()
                    }

                }
            }
        })

    }

    private fun observeMarketData() {
        secondViewModel.singleMarket.observe(viewLifecycleOwner, Observer {
            if (it.data != null) {
                it.data.let { data ->
                    binding.progress.visibility = View.GONE
                    val openStatus = data.open_market_status
                    if (data.market_status == 0) {
                        binding.buttonSubmitBet.isEnabled = false
                        requireActivity().showInfoSnackBar("Market Closed")
                        return@Observer
                    }
                    if (openStatus == 0) {
                        binding.openCircle.isEnabled = false
                        binding.closeCircle.isChecked = true
                        return@Observer
                    }
                }
            }
        })
    }

    private fun initMarketSettings() {
        binding.progress.visibility = View.VISIBLE
        val mainMarketId = Prefs.getInt(Constants.PREFS_MARKET_ID, 123)
        secondViewModel.fetchOneMarketData(BasicUtils.bearerToken(), mainMarketId)
    }

    private fun setAutoCompleteDigits() {
        val adapter = ArrayAdapter<String>(
            mContext,
            R.layout.simple_dropdown_item_1line,
            BasicUtils.tpDigits()
        )
        binding.editInputDigits.setAdapter(adapter)
    }

    private fun verifyBidDigits(inputDigit: String): Boolean {
        val digits = BasicUtils.tpDigits()
        return digits.contains(inputDigit)
    }

    @SuppressLint("SetTextI18n")
    private fun initFormTitle() {
        val marketName = Prefs.getString(Constants.PREFS_MARKET_NAME, "")
        binding.formTitle.text = marketName
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


}