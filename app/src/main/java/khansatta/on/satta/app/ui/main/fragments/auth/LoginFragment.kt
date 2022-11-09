package khansatta.on.satta.app.ui.main.fragments.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.login.AuthResponse
import khansatta.on.satta.app.databinding.FragmentLoginBinding
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.ui.main.view.LoginActivity
import khansatta.on.satta.app.ui.main.viewmodel.MainViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.BasicUtils.showSuccessSnackBar
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Constants.IS_VERIFIED
import khansatta.on.satta.app.basic_utils.Constants.PREFS_TOKEN
import khansatta.on.satta.app.basic_utils.Constants.PREFS_USER_ID
import khansatta.on.satta.app.basic_utils.Constants.USER_MOBILE
import khansatta.on.satta.app.basic_utils.Constants.USER_NAME
import khansatta.on.satta.app.basic_utils.Status
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    private lateinit var mView: View

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view
        binding.btngotoregister.setOnClickListener {
            navigateToRegister(view)
        }
        initOnClick()
        observePostLoginData()

    }

    private fun initOnClick() {

        binding.buttonForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_frogotPassword2)
        }

        binding.buttonHelp.text =
            "Contact Admin  " + Prefs.getString(Constants.PREFS_WHATSAPP_NUMBER, "null")

        binding.buttonHelp.setOnClickListener {
            val number = Prefs.getString(Constants.PREFS_WHATSAPP_NUMBER, "null")
            if (number != "null") {
                val url = "https://api.whatsapp.com/send?phone=$number"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

        binding.btnlogin.setOnClickListener {

            val username = binding.etusername.text.toString()
            val password = binding.etpassword.text.toString()
            if (validateInputInfo(
                    username, password
                )
            ) {
                setupObserver(username, password)
            }
        }
    }

    private fun observePostLoginData() {
        mainViewModel.users.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { users ->
                        cool(users.user.toString())
                        saveLoginUserData(users)
                        requireActivity().showSuccessSnackBar(users.message)
                    }

                    requireActivity().hideSoftKeyboard(mView)

                    (activity as LoginActivity?)?.fetchAppConfig()

                    startActivity(Intent(activity, HomeActivity::class.java))
                    activity?.finish()
                }
                Status.LOADING -> {
                    binding.loginProgressBar.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.loginProgressBar.visibility = View.GONE

                    requireActivity().hideSoftKeyboard(mView)
                    requireActivity().showErrorSnackBar(it.message.toString())

                }
            }
        })
    }

    private fun saveLoginUserData(users: AuthResponse) {
        Prefs.putInt(IS_VERIFIED, users.user.is_verified)
        Prefs.putString(USER_NAME, users.user.name)
        Prefs.putString(USER_MOBILE, users.user.username)
        Prefs.putString(PREFS_TOKEN, users.token)
        Prefs.putInt(PREFS_USER_ID, users.user.id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun validateInputInfo(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter Mobile Number")
            return false
        }
        if (password.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter Password")
            return false
        }
        if (username.isEmpty() && password.isEmpty()) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter Mobile Number and Password")
            return false
        }
        if (username.length < 10) {
            requireActivity().hideSoftKeyboard(mView)
            requireActivity().showErrorSnackBar("Enter Valid Mobile Number")
            return false
        }
        return true
    }

    private fun navigateToRegister(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun setupObserver(username: String, password: String) {
        mainViewModel.fetchUsers(username, password)
    }
}