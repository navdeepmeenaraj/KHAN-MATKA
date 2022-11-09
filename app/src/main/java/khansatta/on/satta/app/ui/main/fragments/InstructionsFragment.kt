package khansatta.on.satta.app.ui.main.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import khansatta.on.satta.app.databinding.FragmentInstructionsBinding
import khansatta.on.satta.app.ui.main.viewmodel.MainViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.checkIfUserIsVerified
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructionsFragment : Fragment() {
    private lateinit var progressDialog: ProgressDialog
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var binding: FragmentInstructionsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(mContext)
        progressDialog.setMessage(Constants.LOADING_MESSAGE)
        getYoutubeVideoLink()
        observeVideoLink()
        if (!checkIfUserIsVerified()) run {
            binding.howToRootLayout.visibility = View.GONE
        }
    }

    private fun getYoutubeVideoLink() {
        viewModel.videoLink()
    }

    private fun observeVideoLink() {
        viewModel.videoLik.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data.let { data ->
                        binding.videoButton.setOnClickListener {
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(data?.link)
                            startActivity(i)
                        }
                    }

                }
                Status.LOADING -> {
                    progressDialog.show()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                }
            }

        }
    }
}