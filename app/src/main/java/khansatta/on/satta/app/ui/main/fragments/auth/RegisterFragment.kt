package khansatta.on.satta.app.ui.main.fragments.auth

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.OtpRequestModel
import khansatta.on.satta.app.databinding.FragmentRegisterBinding
import khansatta.on.satta.app.ui.main.view.SplashActivity
import khansatta.on.satta.app.ui.main.viewmodel.MainViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.toast
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Constants.IS_VERIFIED
import khansatta.on.satta.app.basic_utils.Constants.PREFS_PASSCODE
import khansatta.on.satta.app.basic_utils.Constants.PREFS_TOKEN
import khansatta.on.satta.app.basic_utils.Constants.PREFS_USER_ID
import khansatta.on.satta.app.basic_utils.Status
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.broooapps.otpedittext2.OtpEditText
import com.koushikdutta.ion.Ion
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import khansatta.on.satta.app.basic_utils.Constants.TAG
import java.util.regex.Pattern


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mView: View
    private lateinit var mContext: Context
    private lateinit var progressBar: ProgressDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = ProgressDialog(mContext)
        progressBar.setCanceledOnTouchOutside(false)
        progressBar.setMessage(Constants.LOADING_MESSAGE)
        mView = view
        initOnClickButton()
        observePostRegisterData()

    }

    private fun initOnClickButton() {
        binding.btnregister.setOnClickListener {
            validateUserInputData()
        }

        binding.toLoginFragment.setOnClickListener {
            Navigation.findNavController(it)
                .navigateUp()

        }

    }

    private fun validateUserInputData() {
        val name = binding.editTextNameRegister.text.toString()
        val number = binding.editTextUsernameRegister.text.toString()
        val password = binding.editTextPasswordRegister.text.toString()

        if (name.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter Your Name !")
            return
        }

        if (name.length < 2) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Name is too short !")
            return
        }

        if (name.length > 25) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Name is too Long !")
            return
        }

        val regx = "[a-zA-Z]+\\.?"
        val pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)

        if (!pattern.matcher(name).find()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Name should only contain Alphabets !")
            return
        }

        if (number.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter Number !")
            return
        }

        if (number.length != 10) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Number is too Short !")
            return
        }
        if (!PhoneNumberUtils.isGlobalPhoneNumber("+91$number")) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Mobile Number is Invalid !")
            return
        }

        if (password.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter Password !")
            return
        }

        if (password.length < 6) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Password is too Short !")
            return
        }

        if (password.length > 20) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Password is too Long !")
            return
        }

        verifyMobileNumber(number)


    }

    private fun verifyMobileNumber(number: String) {
        progressBar.show()
        Ion.with(context)
            .load(Constants.SERVER_URL_2 + "send_otp.php")
            .asJsonObject()
            .setCallback { e, result ->
                if (e != null) {
                    Log.d(TAG, "Error Occurred")
                } else {
                    sendOtp(result["otp_url"].asString, result["otp_app_key"].asString, number)
                }
            }


    }

    private fun sendOtp(otp_url: String, otp_key: String, number: String) {
        val requestObject = JSONObject()
        requestObject.put("app_key", otp_key)
        requestObject.put("mobile", number)
        AndroidNetworking.post(otp_url)
            .setContentType("application/json")
            .addStringBody(requestObject.toString())
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(
                OtpRequestModel::class.java,
                object : ParsedRequestListener<OtpRequestModel> {
                    override fun onResponse(response: OtpRequestModel?) {
                        progressBar.dismiss()
                        if (response != null) {
                            verifyOtp(response)
                        }
                    }
                    override fun onError(anError: ANError?) {
                        progressBar.dismiss()
                        cool(anError?.message.toString())
                    }
                })
    }

    private fun verifyOtp(response: OtpRequestModel) {

        val dialog = MaterialDialog(mContext, BottomSheet(LayoutMode.WRAP_CONTENT))
            .customView(R.layout.layout_otp, scrollable = true)

        val otpInput = dialog.getCustomView().findViewById<OtpEditText>(R.id.otpEditText)

        val verifyButton = dialog.getCustomView().findViewById<Button>(R.id.verifyOtpButton)

        otpInput.setOnCompleteListener { value ->
            cool(value)
        }

        verifyButton.setOnClickListener {
            Toast.makeText(mContext, otpInput.text.toString(), Toast.LENGTH_SHORT).show()
            if (otpInput.text.toString().toInt() != response.otp) {
                mContext.toast("Incorrect OTP ! Try Again")
            } else {
                dialog.dismiss()
                val name = binding.editTextNameRegister.text.toString()
                val number = binding.editTextUsernameRegister.text.toString()
                val password = binding.editTextPasswordRegister.text.toString()
                startRegisterService(name, number, password, "0000")
            }
        }

        dialog.cancelOnTouchOutside(false)
        dialog.show()
    }


    private fun observePostRegisterData() {
        mainViewModel.resp.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.dismiss()
                    Prefs.putString("mobile_otp", binding.editTextUsernameRegister.text.toString())
                    it.data?.let { response ->
                        Prefs.putInt(IS_VERIFIED, response.is_verified)
                        Prefs.putString("user_name", binding.editTextNameRegister.text.toString())
                        Prefs.putString(PREFS_TOKEN, response.token)
                        Prefs.putInt(PREFS_USER_ID, response.userId)
                        Prefs.putInt(PREFS_PASSCODE, response.passcode)
                        startActivity(Intent(activity, SplashActivity::class.java))
                        activity?.finish()
                    }
                }
                Status.LOADING -> {
                    progressBar.show()
                }
                Status.ERROR -> {
                    progressBar.dismiss()
                    requireActivity().hideSoftKeyboard(mView)
                    requireActivity().showErrorSnackBar(it.message.toString())
                }
            }
        })

    }

    private fun startRegisterService(
        name: String,
        username: String,
        password: String,
        pin: String
    ) {
        mainViewModel.regUser(name, username, password, pin)
    }
}