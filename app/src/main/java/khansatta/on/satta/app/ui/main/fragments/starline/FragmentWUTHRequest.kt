package khansatta.on.satta.app.ui.main.fragments.starline

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import khansatta.on.satta.app.web_service.model.WithdrawList
import khansatta.on.satta.app.databinding.FragmentWitHdrAwRequestBinding
import khansatta.on.satta.app.ui.main.adapters.WHAdapter
import khansatta.on.satta.app.ui.main.viewmodel.PViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.hideSoftKeyboard
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.basic_utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class FragmentWUTHRequest : Fragment() {

    private val viewModel: PViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var mView: View
    private lateinit var binding: FragmentWitHdrAwRequestBinding

    private val withdrawList: ArrayList<WithdrawList> = ArrayList()
    private var withdrawListAdapter: WHAdapter? = null

    private lateinit var alertDialog: ProgressDialog


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        alertDialog = ProgressDialog(mContext)
        alertDialog.setMessage(Constants.LOADING_MESSAGE)
        alertDialog.setCanceledOnTouchOutside(false)
        observeWithdrawRequestList()
        fetchWithdrawList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWitHdrAwRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observeWithdrawRequestList() {
        viewModel.withdrawList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    alertDialog.dismiss()
                    it.data?.let { list ->
                        if (it.data.isEmpty()) {
                            requireActivity().showErrorSnackBar("No History Found")
                        } else
                            initRecyclerView(list)
                    }
                }
                Status.LOADING -> {
                    alertDialog.show()
                }
                Status.ERROR -> {
                    alertDialog.dismiss()
                    requireActivity().hideSoftKeyboard(mView)
                    requireActivity().showErrorSnackBar("No History Found !")

                }
            }

        }
    }

    private fun fetchWithdrawList() {
        viewModel.getWithdrawRequestList()
    }

    private fun initRecyclerView(data: List<WithdrawList>) {
        val recyclerView = binding.recyclerView
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        withdrawList.clear()
        withdrawList.addAll(data)
        withdrawListAdapter?.notifyDataSetChanged()
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        withdrawListAdapter = WHAdapter(withdrawList)
        recyclerView.adapter = withdrawListAdapter
        recyclerView.setHasFixedSize(false)
    }
}