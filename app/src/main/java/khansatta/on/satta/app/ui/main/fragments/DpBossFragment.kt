package khansatta.on.satta.app.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import khansatta.on.satta.app.databinding.FragmentDpBossBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DpBossFragment : Fragment() {

    private lateinit var binding: FragmentDpBossBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDpBossBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webviewDpboss.settings.javaScriptEnabled = true
        binding.webviewDpboss.loadUrl("https://dpboss.net/")
    }
}