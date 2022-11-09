package khansatta.on.satta.app.ui.main.fragments.bids

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import khansatta.on.satta.app.databinding.FragmentJodiBinding
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.ui.main.view.LoginActivity
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.getMinMaxBetMessage
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.isBetween
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showInfoSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showSuccessSnackBar
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Constants.PREFS_MARKET_ID
import khansatta.on.satta.app.basic_utils.Status
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class JodiFragment : Fragment() {

    private val secondViewModel: SecondViewModel by viewModels()
    private lateinit var binding: FragmentJodiBinding
    private lateinit var mView: View
    private lateinit var _context: Context

    private var digitList: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJodiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        binding.textViewDate.isEnabled = false
        binding.textViewDate.setText(BasicUtils.getCurrentDate())
        initFormTitle()
        initDigitList()
        setAutoCompleteDigits()
        initMarketSettings()
        observeMarketData()
        observePostBidData()
        setOnClick()
    }

    private fun initFormTitle() {
        val marketName = Prefs.getString(Constants.PREFS_MARKET_NAME, "")
        binding.formTitle.text = marketName
    }

    private fun initDigitList() {
        for (i in 0..99) {
            val value = String.format("%02d", i)
            digitList.add(value)
        }
    }

    private fun observePostBidData() {
        secondViewModel.betPlace.observe(viewLifecycleOwner, {
            binding.progress.visibility = View.GONE
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        binding.buttonSubmitBet.isEnabled = true
                        binding.editInputDigits.setText("")
                        binding.editInputPoints.setText("")
                        requireActivity().showSuccessSnackBar(data.error_msg)
                        (activity as HomeActivity?)?.getUserPoints()
                    }
                }
                Status.LOADING -> {
                    binding.buttonSubmitBet.isEnabled = false
                }
                Status.ERROR -> {
                    val error: String =
                        if (it.message?.toInt() == Constants.ERROR_UNAUTHORIZED) {
                            Constants.INCORRECT_CREDS
                        } else {
                            Constants.UNKNOWN_ERROR
                        }
                    requireActivity().showErrorSnackBar(error)
                    if (error == Constants.INCORRECT_CREDS) {
                        requireActivity().startActivity(Intent(_context, LoginActivity::class.java))
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
                    if (data.open_market_status == 0) {
                        binding.buttonSubmitBet.isEnabled = false
                        requireActivity().showInfoSnackBar("Market Closed")
                        return@Observer
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    private fun initMarketSettings() {
        binding.progress.visibility = View.VISIBLE
        val mainMarketId = Prefs.getInt(PREFS_MARKET_ID, 123)
        secondViewModel.fetchOneMarketData(BasicUtils.bearerToken(), mainMarketId)
    }

    private fun setOnClick() {
        binding.buttonSubmitBet.setOnClickListener {
            validateBidData()
        }
    }

    private fun validateBidData() {
        val inputDigit = binding.editInputDigits.text.toString()
        if (inputDigit.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Digit")
            return
        }

        val inputPoint = binding.editInputPoints.text.toString()
        if (inputPoint.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Points")
            return
        }

        val bool = isBetween(inputPoint.toInt())
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

        if (!verifyDigits(inputDigit)) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.INVALID_DIGITS)
            return
        }

        initBidHashMap(inputDigit, inputPoint)
    }

    private fun initBidHashMap(inputDigit: String, inputPoint: String) {
        val map = hashMapOf<String, Any?>()
        map[Constants.HASH_USER_ID] = BasicUtils.userId()
        map[Constants.HASH_BET_DIGIT] = inputDigit
        map[Constants.HASH_MARKET] = Prefs.getInt(PREFS_MARKET_ID, 1)
        map[Constants.HASH_BET_AMT] = inputPoint
        map[Constants.HASH_TYPE] = Constants.DOUBLE_MAIN
        map[Constants.HASH_SESSION] = Constants.SESSION_NULL
        map[Constants.HASH_DATE] = BasicUtils.getCurrentDate()
        requireActivity().hideSoftKeyboard(mView)
        secondViewModel.placeBet(map)
        binding.progress.visibility = View.VISIBLE
    }

    private fun setAutoCompleteDigits() {
        val adapter = ArrayAdapter<String>(
            _context,
            android.R.layout.simple_dropdown_item_1line,
            digitList
        )
        binding.editInputDigits.setAdapter(adapter)
    }

    private fun verifyDigits(inputDigits: String): Boolean {
        return digitList.contains(inputDigits)
    }
}