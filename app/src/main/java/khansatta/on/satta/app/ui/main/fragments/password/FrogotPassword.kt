package khansatta.on.satta.app.ui.main.fragments.password

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.OtpRequestModel
import khansatta.on.satta.app.databinding.FragmentPasswordBinding
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.Constants.OTP_NUMBER
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.koushikdutta.ion.Ion
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import khansatta.on.satta.app.basic_utils.Constants

@AndroidEntryPoint
class FrogotPassword : Fragment() {
    private lateinit var binding: FragmentPasswordBinding
    private lateinit var ctx: Context
    private lateinit var mView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        binding.buttonSendOtp.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() {
        val number = binding.editText.text.toString()
        if (number.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Number is Empty !")
            return
        }

        if (number.length < 10) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter 10 Digit Mobile Number !")
            return
        }
        verifyMobileNumber(number)
    }

    private fun verifyMobileNumber(number: String) {
        Ion.with(context)
            .load(Constants.SERVER_URL_2 + "send_otp.php")
            .asJsonObject()
            .setCallback { e, result ->
                if (e != null) {
                    Log.d(Constants.TAG, "Error Occurred")
                } else {
                    sendOtp(result["otp_url"].asString, result["otp_app_key"].asString, number)
                }
            }


    }


    private fun sendOtp(otp_url: String, otp_key: String, number: String) {

        val requestObject = JSONObject()
        requestObject.put("app_key", otp_key)
        requestObject.put("mobile", "+91$number")

        binding.progressBar.visibility = View.VISIBLE

        AndroidNetworking.post(otp_url)
            .setContentType("application/json")
            .addStringBody(requestObject.toString())
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(
                OtpRequestModel::class.java,
                object : ParsedRequestListener<OtpRequestModel> {
                    override fun onResponse(response: OtpRequestModel?) {
                        if (response != null) {
                            binding.progressBar.visibility = View.GONE
                            verifyOtp(response, number)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        binding.progressBar.visibility = View.GONE
                        BasicUtils.cool(anError?.message.toString())
                    }

                })
    }

    private fun verifyOtp(response: OtpRequestModel, number: String) {
        MaterialDialog(ctx, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(null, "Verify Mobile Number")
            noAutoDismiss()
            cancelOnTouchOutside(false)
            input(
                allowEmpty = false,
                maxLength = 4,
                hint = "Enter OTP",
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { materialDialog, charSequence ->
                val input = materialDialog.getInputField().text.toString().toInt()
                val inputField = materialDialog.getInputField()
                if (input != response.otp) {
                    inputField.error = "OTP Incorrect Try Again !"
                } else {
                    Prefs.putString(OTP_NUMBER, number)
                    materialDialog.dismiss()
                    binding.editText.setText("")
                    findNavController().navigate(R.id.action_frogotPassword_to_passwordNewFragment)

                }
            }
            negativeButton(null, "Cancel") {
                it.dismiss()
            }

        }

    }
}