package khansatta.on.satta.app.ui.main.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import khansatta.on.satta.app.R
import khansatta.on.satta.app.databinding.FragmentBidTypeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BidTypeFragment : Fragment() {

    private lateinit var binding: FragmentBidTypeBinding
    private lateinit var _context: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setOnClick() {

        binding.apply {
            starSp.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_betTypeFragment_to_singlePanelFragment)
            }

            starDp.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_betTypeFragment_to_doublePanelFragment)
            }

            starTp.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_betTypeFragment_to_triplePanelFragment)
            }

            starSingle.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_betTypeFragment_to_singleFragment)
            }

            betDouble.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_betTypeFragment_to_jodiFragment)
            }

            betHalfSangam.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_betTypeFragment_to_halfSangamFragment)
            }
            betFullSangam.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_betTypeFragment_to_fullSangamFragment)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }
}