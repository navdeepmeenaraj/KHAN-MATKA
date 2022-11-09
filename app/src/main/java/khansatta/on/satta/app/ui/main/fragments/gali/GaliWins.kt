package khansatta.on.satta.app.ui.main.fragments.gali

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.gali.bidhis
import khansatta.on.satta.app.ui.main.adapters.GaliWinHisAdapter
import khansatta.on.satta.app.ui.main.viewmodel.GaliViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GaliWins : Fragment(R.layout.fragment_gali_wins) {
    private val viewmodel: GaliViewModel by viewModels()
    private lateinit var _context: Context
    private lateinit var recyclerView: RecyclerView
    private val bidListStar: ArrayList<bidhis> = ArrayList()
    private var bidHistoryAdapterStar: GaliWinHisAdapter? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView(view)
        getBids()

    }

    private fun getBids() {
        viewmodel.fetchGaliWinHistory()
        viewmodel.wins.observe(viewLifecycleOwner, {
            if (it.data != null) {
                if (it.data.isEmpty()) {
                    requireActivity().showErrorSnackBar("No Win History Found")
                } else {
                    initRecyclerViewWin(it.data)
                }
            }
        })
    }

    private fun setView(view: View) {
        recyclerView = view.findViewById(R.id.gali_win_his)
    }

    private fun initRecyclerViewWin(data: List<bidhis>) {
        bidListStar.clear()
        bidListStar.addAll(data)
        bidHistoryAdapterStar?.notifyDataSetChanged()
        recyclerView.layoutManager = LinearLayoutManager(_context)
        bidHistoryAdapterStar = GaliWinHisAdapter(bidListStar)
        recyclerView.adapter = bidHistoryAdapterStar
        recyclerView.setHasFixedSize(false)
    }
}