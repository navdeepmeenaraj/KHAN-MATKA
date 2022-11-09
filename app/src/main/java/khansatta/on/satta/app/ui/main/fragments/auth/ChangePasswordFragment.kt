package khansatta.on.satta.app.ui.main.fragments.auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import dagger.hilt.android.AndroidEntryPoint
import khansatta.on.satta.app.databinding.FragmentChangePasswordBinding
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.toast
import khansatta.on.satta.app.basic_utils.Constants

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var mContext: Context
    private lateinit var mProgress: ProgressDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgress = ProgressDialog(mContext)
        mProgress.setMessage(Constants.LOADING_MESSAGE)
        initOnClick()

    }


    private fun initOnClickFunction() {

        binding.buttonSubmitPassword.setOnClickListener {

        }

    }

    private fun initOnClick() {
        binding.buttonSubmitPassword.setOnClickListener {
            if (binding.oldPassword.text.toString().isEmpty()) {
                mContext.toast("Please enter old password")
                return@setOnClickListener
            }

            if (binding.oldPassword.text.toString().length < 6) {
                mContext.toast("Password should be 6 characters long")
                return@setOnClickListener
            }

            if (binding.newPassword.text.toString().isEmpty()) {
                mContext.toast("Please enter new password")
                return@setOnClickListener
            }


            if (binding.newPassword.text.toString().length < 6) {
                mContext.toast("Password should be 6 characters long")

                return@setOnClickListener
            }

            if (binding.confirmPassword.text.toString().isEmpty()) {
                mContext.toast("Please enter confirm password")
                return@setOnClickListener
            }

            if (binding.confirmPassword.text.toString().length < 6) {
                mContext.toast("Password should be 6 characters long")

                return@setOnClickListener
            }

            if (binding.confirmPassword.text.toString() != binding.newPassword.text.toString()) {
                mContext.toast("New and Confirm Password should be same !")
            }

            changePassword(
                binding.oldPassword.text.toString(),
                binding.newPassword.text.toString(),
                binding.confirmPassword.text.toString()
            )

        }
    }

    private fun changePassword(old: String, new: String, confirm: String) {
        mProgress.show()
        val json = JsonObject()
        json.addProperty("user_id", BasicUtils.userId())
        json.addProperty("old_password", old)
        json.addProperty("new_password", new)
        json.addProperty("confirm_new_password", confirm)

        Ion.with(context)
            .load(Constants.SERVER_URL + "change_password")
            .setJsonObjectBody(json)
            .asJsonObject()
            .setCallback { e, result ->
                mProgress.dismiss()
                if (e != null) {
                    mContext.toast(
                        "Something went wrong"
                    )
                    return@setCallback
                }

                mContext.toast(result["message"].toString())
            }
    }
}