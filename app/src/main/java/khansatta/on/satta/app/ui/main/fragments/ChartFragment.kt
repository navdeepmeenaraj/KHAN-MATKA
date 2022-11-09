package khansatta.on.satta.app.ui.main.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import khansatta.on.satta.app.R
import khansatta.on.satta.app.ui.main.viewmodel.GaliViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.Constants
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {
    private val galiViewModel: GaliViewModel by viewModels()
    private lateinit var _context: Context
    private lateinit var chartView: WebView
    private lateinit var title: TextView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chartView = view.findViewById(R.id.webview_chart)
        title = view.findViewById(R.id.form_title)
        chartView.settings.javaScriptEnabled = true
        try {
            title.text = Prefs.getString(
                Constants.PREFS_CHART_TITLE,
                "Market Chart"
            ) + " Result Chart"
        } catch (e: Exception) {
            BasicUtils.cool("Error In Settings Fragment ${e.localizedMessage}")
        }
        try {
            galiViewModel.fetchMarketChart(Prefs.getInt(Constants.PREFS_CHART_ID, 100)) {
                chartView.loadUrl(it)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}