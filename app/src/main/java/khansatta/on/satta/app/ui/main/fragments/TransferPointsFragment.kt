package khansatta.on.satta.app.ui.main.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import khansatta.on.satta.app.databinding.FragmentTransferPointsBinding
import khansatta.on.satta.app.ui.main.viewmodel.PViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.BasicUtils.toast
import khansatta.on.satta.app.basic_utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferPointsFragment : Fragment() {

    private lateinit var binding: FragmentTransferPointsBinding
    private lateinit var mContext: Context
    private val pViewModel: PViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnClick()
        observerUserVerification()
        observerTransferResponse()
    }

    private fun observerTransferResponse() {
        pViewModel.transferPoints.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        if (!data.error) {
                            mContext.toast("Transfer Successful")
                        }
                    }
                }
                Status.LOADING -> {
                    cool("Verifying User")
                }
                Status.ERROR -> {
                    mContext.toast("Something went wrong !")
                }
            }
        }

    }

    private fun observerUserVerification() {
        pViewModel.verifyUser.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        if (data.success) {
                            binding.verifiedUserName.text = "Name : " + data.name
                            binding.verifiedUserName.visibility = View.VISIBLE
                            binding.verifyButton.visibility = View.GONE
                            binding.transferButton.visibility = View.VISIBLE
                        }
                    }
                }
                Status.LOADING -> {
                    cool("Verifying User")
                }
                Status.ERROR -> {
                    mContext.toast("Something went wrong !")
                }
            }
        }

    }

    private fun initOnClick() {
        binding.verifyButton.setOnClickListener {
            validateInputFields()
        }

        binding.transferButton.setOnClickListener {
            val transferNumber = binding.transferNumber.text.toString()
            val transferAmount = binding.transferAmount.text.toString()

            if (transferNumber.isEmpty()) {
                mContext.toast("Please Enter Mobile Number")
                return@setOnClickListener
            }
            if (transferNumber.length < 10) {
                mContext.toast("Mobile Number length Invalid ")
                return@setOnClickListener

            }

            if (transferAmount.isEmpty()) {
                mContext.toast("Please Enter Points")
                return@setOnClickListener

            }

            pViewModel.transferPoints(transferNumber, transferAmount)
        }
    }

    private fun validateInputFields() {
        val transferNumber = binding.transferNumber.text.toString()
        val transferAmount = binding.transferAmount.text.toString()

        if (transferNumber.isEmpty()) {
            mContext.toast("Please Enter Mobile Number")
            return
        }
        if (transferNumber.length < 10) {
            mContext.toast("Mobile Number length Invalid ")
            return
        }

        if (transferAmount.isEmpty()) {
            mContext.toast("Please Enter Points")
            return
        }

        verifyMobileNumber(transferNumber)
    }

    private fun verifyMobileNumber(transferNumber: String) {
        pViewModel.verifyUser(transferNumber)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferPointsBinding.inflate(inflater, container, false)
        return binding.root
    }

}