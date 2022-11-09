package khansatta.on.satta.app.ui.main.fragments.wallut

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import khansatta.on.satta.app.databinding.FragmentPayTmBinding
import khansatta.on.satta.app.ui.main.viewmodel.PViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.toast
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Status
import dagger.hilt.android.AndroidEntryPoint
import com.google.gson.JsonObject

import com.koushikdutta.ion.Ion
import khansatta.on.satta.app.basic_utils.BasicUtils
import java.lang.Exception


@AndroidEntryPoint
class PFragment : Fragment() {

    private val viewmodel: PViewModel by viewModels()
    private lateinit var mView: View
    private lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: FragmentPayTmBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        progressDialog = ProgressDialog(mContext)
        progressDialog.setMessage(Constants.LOADING_MESSAGE)
        observePaymentDetailUpdateResponse()
        initButtons()
        getPaytmDetails()
    }


    private fun getPaytmDetails() {
        val json = JsonObject()
        json.addProperty("user_id", BasicUtils.userId())

        Ion.with(context)
            .load(Constants.SERVER_URL + "paytm_number")
            .setJsonObjectBody(json)
            .asJsonObject()
            .setCallback { e, result ->
                if (e != null) {
                    binding.editTextPaytm.setText("")
                    mContext.toast(
                        "Paytm Number Not Added"
                    )
                } else {
                    val isSuccess = result["success"].asBoolean
                    if (isSuccess) {
                        try {
                            val paytmNumber = result["paytm"].toString().removeRange(0, 1)
                            val paytmNumber2 = paytmNumber.removeRange(10, 11)
                            binding.editTextPaytm.setText(paytmNumber2)
                        } catch (e: Exception) {
                            Log.d("kesari", e.toString())
                        }

                    } else {
                        binding.editTextPaytm.setText("")
                    }

                }

            }
    }


    private fun initButtons() {
        binding.btnSubmitPaytm.setOnClickListener {
            val number = binding.editTextPaytm.text.toString()
            if (number.length < 10) {
                requireActivity().showErrorSnackBar("Enter Valid Number")
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                requireActivity().showErrorSnackBar("Enter Number")
                return@setOnClickListener
            }

            updatePaymentDetails(1, number)
        }
    }

    private fun observePaymentDetailUpdateResponse() {
        viewmodel.paymentDetails.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data?.let { data ->
                        mContext.toast(data.message)
                    }
                }
                Status.LOADING -> {
                    progressDialog.show()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    requireActivity().hideSoftKeyboard(mView)
                    mContext.toast(it.message.toString())

                }
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayTmBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun updatePaymentDetails(paymentType: Int, paymentNumber: String) {
        viewmodel.updatePaymentDetails(paymentType, paymentNumber)
    }

}