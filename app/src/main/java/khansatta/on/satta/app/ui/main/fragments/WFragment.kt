package khansatta.on.satta.app.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import khansatta.on.satta.app.R
import khansatta.on.satta.app.databinding.FragmentWallEtBinding
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.Constants
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WFragment : Fragment(R.layout.fragment_wall_et) {
    private lateinit var binding: FragmentWallEtBinding
    private lateinit var mView: View
    private lateinit var mButtonAddPoints: Button
    private lateinit var mButtonWithdrawPoints: Button
    private lateinit var walletContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        walletContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWallEtBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity?)?.getUserPoints()
        mView = view
        setView(view)
        setOnClick()
    }

    private fun setOnClick() {
        mButtonWithdrawPoints.setOnClickListener {
            val number = Prefs.getString(Constants.PREFS_WHATSAPP_NUMBER, "null")
            if (number != "null") {
                val url = "https://api.whatsapp.com/send?phone=$number"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
        mButtonAddPoints.setOnClickListener {
            requireActivity().showErrorSnackBar("Coming Soon")
        }

        binding.depositWhatsapp.setOnClickListener {
            val number = Prefs.getString(Constants.PREFS_WHATSAPP_NUMBER, "null")
            if (number != "null") {
                val url = "https://api.whatsapp.com/send?phone=$number"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }

    private fun setView(view: View) {
        mButtonAddPoints = view.findViewById(R.id.button_add_points)
        mButtonWithdrawPoints = view.findViewById(R.id.button_withdraw_points)
    }

}