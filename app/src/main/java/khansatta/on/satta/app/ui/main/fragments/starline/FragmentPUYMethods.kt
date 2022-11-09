package khansatta.on.satta.app.ui.main.fragments.starline

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import khansatta.on.satta.app.databinding.FragmentPaYmEntMethodsBinding
import khansatta.on.satta.app.ui.main.viewmodel.PViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.toast
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Status
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import dagger.hilt.android.AndroidEntryPoint
import khansatta.on.satta.app.R

@AndroidEntryPoint
class FragmentPUYMethods : Fragment() {

    private val viewmodel: PViewModel by viewModels()
    private lateinit var binding: FragmentPaYmEntMethodsBinding
    private lateinit var mView: View
    private lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog
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
    }

    private fun initButtons() {
        binding.paytmButton.setOnClickListener {
            showPaytmDialog()
        }

        binding.phonepeButton.setOnClickListener {
            showPhonepeDialog()
        }

        binding.gpayButton.setOnClickListener {
            showGpayDialog()
        }

    }

    private fun showGpayDialog() {
        val dialog = MaterialDialog(
            mContext,
            BottomSheet(LayoutMode.WRAP_CONTENT)
        ).customView(R.layout.gp_ay_number)
        val customView = dialog.getCustomView()
        val value = customView.findViewById<AppCompatEditText>(R.id.gpay_number_edit)
        val submit = customView.findViewById<AppCompatButton>(R.id.submit_gpay_button)
        submit.setOnClickListener {
            val number = value.text.toString()
            if (number.length < 10) {
                requireActivity().showErrorSnackBar("Enter Valid Number")
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                requireActivity().showErrorSnackBar("Enter Number")
                return@setOnClickListener
            }

            updatePaymentDetails(3, number)
        }

        dialog.show()
    }

    private fun showPhonepeDialog() {
        val dialog = MaterialDialog(
            mContext,
            BottomSheet(LayoutMode.WRAP_CONTENT)
        ).customView(R.layout.pho_ne_pe_number)
        val customView = dialog.getCustomView()
        val value = customView.findViewById<AppCompatEditText>(R.id.phonepe_number_edit)
        val submit = customView.findViewById<AppCompatButton>(R.id.submit_phonepe_button)
        submit.setOnClickListener {
            val number = value.text.toString()
            if (number.length < 10) {
                requireActivity().showErrorSnackBar("Enter Valid Number")
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                requireActivity().showErrorSnackBar("Enter Number")
                return@setOnClickListener
            }

            updatePaymentDetails(2, number)

        }
        dialog.show()
    }

    private fun showPaytmDialog() {
        val dialog = MaterialDialog(
            mContext,
            BottomSheet(LayoutMode.WRAP_CONTENT)
        ).customView(R.layout.pa_yt_m_number)
        val customView = dialog.getCustomView()
        val value = customView.findViewById<AppCompatEditText>(R.id.paytm_number_edit)
        val submit = customView.findViewById<AppCompatButton>(R.id.submit_paytm_button)
        submit.setOnClickListener {
            val number = value.text.toString()
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
        dialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaYmEntMethodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun updatePaymentDetails(paymentType: Int, paymentNumber: String) {
        viewmodel.updatePaymentDetails(paymentType, paymentNumber)
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
}