package khansatta.on.satta.app.ui.main.fragments.starline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import khansatta.on.satta.app.web_service.model.starline.StarWins
import khansatta.on.satta.app.databinding.FragmentStarLineWinHistoryBinding
import khansatta.on.satta.app.ui.main.adapters.StarWinHistoryAdapter
import khansatta.on.satta.app.ui.main.viewmodel.StarlineViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StarLineWinHistory : Fragment() {
    private lateinit var binding: FragmentStarLineWinHistoryBinding

    private val vm: StarlineViewModel by viewModels()

    private val winListStar: ArrayList<StarWins> = ArrayList()
    private var winHistoryAdapterStar: StarWinHistoryAdapter? = null
    private lateinit var _context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStarLineWinHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.visibility = View.VISIBLE
        vm.fetchStarWinHistory()
        observeWinHistory()
    }

    private fun observeWinHistory() {
        vm.wins.observe(viewLifecycleOwner, {
            binding.progress.visibility = View.GONE
            if (it.data != null) {
                if (it.data.isEmpty()) {
                    requireActivity().showErrorSnackBar("No Win History Found !")
                } else initRecyclerViewWin(it.data)
            }
        })
    }

    private fun initRecyclerViewWin(data: List<StarWins>) {
        winListStar.clear()
        winListStar.addAll(data)
        winHistoryAdapterStar?.notifyDataSetChanged()
        binding.starWinsHis.layoutManager = LinearLayoutManager(_context)
        winHistoryAdapterStar = StarWinHistoryAdapter(winListStar)
        binding.starWinsHis.adapter = winHistoryAdapterStar
        binding.starWinsHis.setHasFixedSize(false)
    }
}