package khansatta.on.satta.app.ui.main.fragments.bids

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import khansatta.on.satta.app.databinding.FragmentHalfSangamBinding
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.getMinMaxBetMessage
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
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
class HalfSangamFragment : Fragment() {

    private val secondViewModel: SecondViewModel by viewModels()
    private lateinit var binding: FragmentHalfSangamBinding
    private lateinit var mView: View
    private lateinit var _context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHalfSangamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        binding.date.text = BasicUtils.getCurrentDate()

        initFormTitle()
        initMarketSettings()
        setAutoCompleteAdapter()
        setAutoCompleteAdapterTwo()
        observeMarketData()
        observePostBidData()
        setOnClick()

    }

    private fun observePostBidData() {
        secondViewModel.betPlace.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    binding.buttonSubmitBid.isEnabled = true
                    binding.hsgCloseDigit.setText("")
                    binding.hsgOpenDigit.setText("")
                    binding.hsgOpenPanel.setText("")
                    binding.hsgClosePanel.setText("")
                    binding.hsgEditInputPoints.setText("")
                    it.data?.let { data ->
                        requireActivity().hideSoftKeyboard(mView)
                        requireActivity().showSuccessSnackBar(it.data.error_msg)
                        (activity as HomeActivity?)?.getUserPoints()
                    }
                }
                Status.LOADING -> {
                    BasicUtils.cool("Bet Place in Progress")
                }
                Status.ERROR -> {
                    binding.buttonSubmitBid.isEnabled = true
                    binding.progress.visibility = View.GONE
                    requireActivity().hideSoftKeyboard(mView)
                    requireActivity().showErrorSnackBar("Unable to Place Bid")
                }
            }
        })
    }

    private fun initMarketSettings() {
        binding.progress.visibility = View.VISIBLE
        val mainMarketId = Prefs.getInt(PREFS_MARKET_ID, 123)
        secondViewModel.fetchOneMarketData(BasicUtils.bearerToken(), mainMarketId)
    }

    @SuppressLint("SetTextI18n")
    private fun initFormTitle() {
        val marketName = Prefs.getString(Constants.PREFS_MARKET_NAME, "")
        binding.formTitle.text = marketName
    }

    private fun observeMarketData() {
        secondViewModel.singleMarket.observe(viewLifecycleOwner, Observer {
            if (it.data != null) {
                it.data.let { data ->
                    binding.progress.visibility = View.GONE
                    val openStatus = data.open_market_status
                    if (openStatus == 0) {
                        binding.buttonSubmitBid.isEnabled = false
                        requireActivity().showInfoSnackBar("Cannot Place Bid Result Already Declared")
                    }
                }
            }
        })
    }

    private fun setOnClick() {
        binding.hsgOpenCircle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.hsgOneCond.visibility = View.VISIBLE
                binding.hsgTwoCond.visibility = View.GONE
            }
        }

        binding.hsgCloseCircle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.hsgOneCond.visibility = View.GONE
                binding.hsgTwoCond.visibility = View.VISIBLE
            }
        }

        binding.buttonSubmitBid.setOnClickListener {

            requireActivity().hideSoftKeyboard(mView)

            if (binding.hsgOpenCircle.isChecked) {
                validateFormOne()
            } else if (binding.hsgCloseCircle.isChecked) {
                validateFormTwo()
            }

        }
    }

    private fun validateFormTwo() {

        val closePana = binding.hsgClosePanel.text.toString()
        if (closePana.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Close Panel")
            return
        }

        if (closePana.length < 3) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Close Panel Should be Three Digits")
            return
        }

        val openDigit = binding.hsgOpenDigit.text.toString()
        if (openDigit.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Open Digit")
            return
        }

        val bidAmount = binding.hsgEditInputPoints.text.toString()
        if (bidAmount.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Amount")
            return
        }

        val points = Prefs.getInt("Balance", 0)
        if (points < bidAmount.toInt()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.POINTS_INSUFFICIENT)
            return
        }

        val bool = BasicUtils.isBetween(
            binding.hsgEditInputPoints.text.toString().toInt()
        )
        if (!bool) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(getMinMaxBetMessage())
            return
        }

        initBidHashMapTwo(closePana, openDigit, bidAmount)
    }

    private fun initBidHashMapTwo(closePana: String, openDigit: String, bidAmount: String) {
        val map = hashMapOf<String, Any?>()
        map[Constants.HASH_USER_ID] = BasicUtils.userId()
        map[Constants.HASH_BET_DIGIT] = closePana + openDigit
        map[Constants.HASH_MARKET] = Prefs.getInt(PREFS_MARKET_ID, 123)
        map[Constants.HASH_BET_AMT] = bidAmount
        map[Constants.HASH_TYPE] = Constants.HS_MAIN
        map[Constants.HASH_SESSION] = Constants.SESSION_CLOSE
        map[Constants.HASH_DATE] = BasicUtils.getCurrentDate()
        bidPlace(map)
    }

    private fun validateFormOne() {

        val openPana = binding.hsgOpenPanel.text.toString()
        if (openPana.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Open Panel")
            return
        }

        if (openPana.length < 3) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Close Panel Should be Three Digits")
            return
        }

        val closeDigit = binding.hsgCloseDigit.text.toString()
        if (closeDigit.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Close Digit")
            return
        }

        val bidAmount = binding.hsgEditInputPoints.text.toString()
        if (bidAmount.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Amount")
            return
        }

        val bool = BasicUtils.isBetween(
            binding.hsgEditInputPoints.text.toString().toInt()
        )

        if (!bool) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.MIN_BET_MSG)
            return
        }

        initBidHashMapOne(openPana, closeDigit, bidAmount)
    }

    private fun initBidHashMapOne(openPana: String, closeDigit: String, bidAmount: String) {
        val map = hashMapOf<String, Any?>()
        map[Constants.HASH_USER_ID] = BasicUtils.userId()
        map[Constants.HASH_BET_DIGIT] = openPana + closeDigit
        map[Constants.HASH_MARKET] = Prefs.getInt(PREFS_MARKET_ID, 123)
        map[Constants.HASH_BET_AMT] = bidAmount
        map[Constants.HASH_TYPE] = Constants.HS_MAIN
        map[Constants.HASH_SESSION] = Constants.SESSION_OPEN
        map[Constants.HASH_DATE] = BasicUtils.getCurrentDate()
        bidPlace(map)
    }

    private fun bidPlace(map: HashMap<String, Any?>) {
        binding.buttonSubmitBid.isEnabled = false
        binding.progress.visibility = View.VISIBLE
        secondViewModel.placeBet(map)
    }

    private fun setAutoCompleteAdapter() {
        val adapter = ArrayAdapter(
            _context,
            android.R.layout.simple_dropdown_item_1line,
            BasicUtils.halfSangamDigits()
        )
        binding.hsgOpenPanel.setAdapter(adapter)
        binding.hsgClosePanel.setAdapter(adapter)
    }

    private fun setAutoCompleteAdapterTwo() {
        val adapter = ArrayAdapter(
            _context,
            android.R.layout.simple_dropdown_item_1line,
            BasicUtils.singleDigits()
        )
        binding.hsgOpenDigit.setAdapter(adapter)
        binding.hsgCloseDigit.setAdapter(adapter)
    }

}