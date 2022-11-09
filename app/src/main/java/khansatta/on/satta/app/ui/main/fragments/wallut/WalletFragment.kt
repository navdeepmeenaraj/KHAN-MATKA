package khansatta.on.satta.app.ui.main.fragments.wallut

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import khansatta.on.satta.app.basic_utils.Constants.PREFS_TELEGRAM
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import khansatta.on.satta.app.R
import khansatta.on.satta.app.databinding.FragmentWalletNewBinding
import khansatta.on.satta.app.ui.main.view.PActivity

@AndroidEntryPoint
class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletNewBinding
    private lateinit var mContext: Context
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setOnClick()
    }

    private fun setOnClick() {
        binding.buttoAddPoints.setOnClickListener {
            startActivity(Intent(mContext, PActivity::class.java))
        }

        binding.buttonTelegram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Prefs.getString(PREFS_TELEGRAM, ""))
            startActivity(intent)
        }

        binding.textPoints.text  = Prefs.getInt("Balance",0).toString() + " Pts"

        binding.buttonWithdrPoints.setOnClickListener {
            navController.navigate(R.id.withdrawFragment)
        }
        binding.buttonPhonepe.setOnClickListener {
            navController.navigate(R.id.phonepayFragment)
        }

        binding.buttonGpay.setOnClickListener {
            navController.navigate(R.id.gpayFragment)
        }

        binding.buttonPaytm.setOnClickListener {
            navController.navigate(R.id.paytmFragment)
        }
    }


}