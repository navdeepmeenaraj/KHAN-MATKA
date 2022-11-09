package khansatta.on.satta.app.ui.main.fragments.bids

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import khansatta.on.satta.app.databinding.FragmentFullSangamBinding
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.getMinMaxBetMessage
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showInfoSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showSuccessSnackBar
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Constants.FS_MAIN
import khansatta.on.satta.app.basic_utils.Constants.SESSION_NULL
import khansatta.on.satta.app.basic_utils.Status
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FullSangamFragment : Fragment() {

    private lateinit var binding: FragmentFullSangamBinding
    private val secondViewModel: SecondViewModel by viewModels()
    private lateinit var mView: View
    private lateinit var _context: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullSangamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        binding.fullSangamDate.text = BasicUtils.getCurrentDate()
        initFormTitle()
        initMarketSettings()
        observeMarketData()
        observePostBidData()
        setOnClick()
    }

    private fun observePostBidData() {
        secondViewModel.betPlace.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                    binding.progress.visibility = View.GONE
                    binding.fullSangamButtonSubmitBet.isEnabled = true
                    binding.fullSangamEditInputPoints.setText("")
                    binding.fullSangamOpenPanel.setText("")
                    binding.fullSangamClosePanel.setText("")

                    it.data?.let { data ->
                        requireActivity().hideSoftKeyboard(mView)
                        requireActivity().showSuccessSnackBar(it.data.error_msg)
                    }
                    (activity as HomeActivity?)?.getUserPoints()
                }
                Status.LOADING -> {
                    BasicUtils.cool("Bid Place in Progress")
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    requireActivity().showErrorSnackBar("Unable to Place Bet")
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
                    if (openStatus == 0) {
                        binding.fullSangamButtonSubmitBet.isEnabled = false
                        requireActivity().showInfoSnackBar("Cannot Place Bid Result Already Declared")
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

    @SuppressLint("SetTextI18n")
    private fun initFormTitle() {
        val marketName = Prefs.getString(Constants.PREFS_MARKET_NAME, "")
        binding.formTitle.text = marketName
    }

    private fun setOnClick() {
        binding.fullSangamButtonSubmitBet.setOnClickListener {
            validateBidData()
        }
    }

    private fun validateBidData() {
        val openPanel = binding.fullSangamOpenPanel.text.toString()
        val closePanel = binding.fullSangamClosePanel.text.toString()
        val bidAmount = binding.fullSangamEditInputPoints.text.toString()

        if (openPanel.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Open Panel Digits")
            return
        }

        if (closePanel.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Close Panel Digits")
            return
        }

        if (openPanel.length < 3) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Panel should be Three Digits Long")
            return
        }

        if (closePanel.length < 3) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Panel should be Three Digits Long")
            return
        }

        if (bidAmount.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Please Enter Bid Amount")
            return
        }

        val bool = BasicUtils.isBetween(bidAmount.toInt())
        if (!bool) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(getMinMaxBetMessage())
            return
        }

        val points = Prefs.getInt("Balance", 0)
        if (points < bidAmount.toInt()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar(Constants.POINTS_INSUFFICIENT)
            return
        }

        initBidHashMap(openPanel, closePanel, bidAmount)

    }

    private fun initBidHashMap(openPanel: String, closePanel: String, bidAmount: String) {
        val map = hashMapOf<String, Any?>()
        map[Constants.HASH_USER_ID] = BasicUtils.userId()
        map[Constants.HASH_BET_DIGIT] = openPanel + closePanel
        map[Constants.HASH_MARKET] = Prefs.getInt(Constants.PREFS_MARKET_ID, 123)
        map[Constants.HASH_BET_AMT] = bidAmount
        map[Constants.HASH_TYPE] = FS_MAIN
        map[Constants.HASH_SESSION] = SESSION_NULL
        map[Constants.HASH_DATE] = BasicUtils.getCurrentDate()
        betPlace(map)
    }


    private fun betPlace(map: HashMap<String, Any?>) {
        binding.fullSangamButtonSubmitBet.isEnabled = false
        binding.progress.visibility = View.VISIBLE
        secondViewModel.placeBet(map)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context

    }
}