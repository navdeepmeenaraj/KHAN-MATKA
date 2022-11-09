package khansatta.on.satta.app.ui.main.fragments.gali

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint
import khansatta.on.satta.app.basic_utils.Constants
import khansatta.on.satta.app.databinding.FragmentGaliChartBinding

@AndroidEntryPoint
class GaliChartFragment : Fragment() {

    private lateinit var binding: FragmentGaliChartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val galiId = Prefs.getInt("gali_id", 0)

        try {
            binding.webviewChart.loadUrl(Constants.SERVER_URL + "gali_chart/$galiId")
        } catch (e: Exception) {
            throw e
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGaliChartBinding.inflate(inflater, container, false);
        return binding.root
    }

}