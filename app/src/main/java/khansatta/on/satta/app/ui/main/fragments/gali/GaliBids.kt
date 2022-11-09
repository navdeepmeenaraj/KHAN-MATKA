package khansatta.on.satta.app.ui.main.fragments.gali

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.web_service.model.gali.bidhis
import khansatta.on.satta.app.databinding.FragmentGaliBidsBinding
import khansatta.on.satta.app.ui.main.adapters.GaliBidHisAdapter
import khansatta.on.satta.app.ui.main.viewmodel.GaliViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GaliBids : Fragment() {
    private val viewmodel: GaliViewModel by viewModels()
    private lateinit var _context: Context
    private lateinit var recyclerView: RecyclerView
    private val bidListStar: ArrayList<bidhis> = ArrayList()
    private var bidHistoryAdapterStar: GaliBidHisAdapter? = null
    private lateinit var binding: FragmentGaliBidsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGaliBidsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.galiBidsHis
        getBids()
        observeGaliBids()

    }

    private fun observeGaliBids() {
        viewmodel.bids.observe(viewLifecycleOwner, {
            recyclerView.visibility = View.VISIBLE
            binding.progresBar.visibility = View.GONE
            if (it.data != null) {
                if (it.data.isEmpty()) {
                    requireActivity().showErrorSnackBar("No Bid History Found")
                } else {
                    initRecyclerViewWin(it.data)
                }
            }

        })
    }

    private fun getBids() {
        recyclerView.visibility = View.GONE
        binding.progresBar.visibility = View.VISIBLE
        viewmodel.fetchGaliBidHistory()
    }

    private fun initRecyclerViewWin(data: List<bidhis>) {
        bidListStar.clear()
        bidListStar.addAll(data)
        bidHistoryAdapterStar?.notifyDataSetChanged()
        recyclerView.layoutManager = LinearLayoutManager(_context)
        bidHistoryAdapterStar = GaliBidHisAdapter(bidListStar)
        recyclerView.adapter = bidHistoryAdapterStar
        recyclerView.setHasFixedSize(false)
    }

}
