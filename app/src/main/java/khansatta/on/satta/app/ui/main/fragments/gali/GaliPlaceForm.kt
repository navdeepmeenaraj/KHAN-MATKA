package khansatta.on.satta.app.ui.main.fragments.gali

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import khansatta.on.satta.app.databinding.FragmentGaliPlaceFormBinding
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.ui.main.viewmodel.GaliViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.getMinMaxBetMessage
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.isBetween
import khansatta.on.satta.app.basic_utils.BasicUtils.showSuccessSnackBar
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Status
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GaliPlaceForm : Fragment() {

    private val viewmodel: GaliViewModel by viewModels()
    private lateinit var mView: View
    private lateinit var _context: Context
    private lateinit var binding: FragmentGaliPlaceFormBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.fetchDate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGaliPlaceFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        binding.lodaDate.text = BasicUtils.getCurrentDate()
        setOnClick()
        observePostBidData()
    }

    private fun observePostBidData() {
        viewmodel.place_bid.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->

                        data.message

                        binding.buttonSubmitBid.isEnabled = true
                        binding.betDigit.setText("")
                        binding.galiBidPoints.setText("")
                        requireActivity().hideSoftKeyboard(mView)
                        requireActivity().showSuccessSnackBar("Bid Placed Successfully")
                        (activity as HomeActivity?)?.getUserPoints()
                    }
                }
                Status.LOADING -> {
                    binding.buttonSubmitBid.isEnabled = false
                }
                Status.ERROR -> {
                    binding.buttonSubmitBid.isEnabled = true
                    requireActivity().hideSoftKeyboard(mView)
                    requireActivity().showSuccessSnackBar("Unable to Place Bid")
                }
            }

        })

    }

    private fun setOnClick() {
        binding.buttonSubmitBid.setOnClickListener {
            try {
                val points = binding.galiBidPoints.text.toString().toInt()
                val digits = binding.betDigit.text.toString()

                if (points == null) {
                    binding.galiBidPoints.error = "Enter Points"
                } else if (digits == null) {
                    binding.betDigit.error = "Enter Digits"
                } else if (!isBetween(points)) {
                    binding.galiBidPoints.error = getMinMaxBetMessage()
                } else if (digits.length < 2) {
                    binding.betDigit.error = "Enter Digits Between 01 and 99"
                } else if (Prefs.getInt("Balance", 0) < points) {
                    binding.galiBidPoints.error = "Insufficient Points in your wallet"
                } else {
                    placeUserBids(points, digits)
                }
            } catch (e: Exception) {
                Log.d("Shiv", e.message.toString())
            }
        }
    }

    private fun placeUserBids(points: Int, digits: String) {
        val hashMap = HashMap<String, Any?>()
        hashMap["gali_id"] = Prefs.getInt("gali_id", 0)
        hashMap["bid_digit"] = digits
        hashMap["bid_amount"] = points
        hashMap["user_id"] = BasicUtils.userId()
        hashMap["bid_date"] = BasicUtils.getCurrentDate()
        try {
            viewmodel.galiBidPlaced(hashMap)
        } catch (e: Exception) {
            Log.d(Constants.TAG, e.message.toString())
        }
    }
}