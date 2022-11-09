package khansatta.on.satta.app.ui.main.fragments.starline

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import khansatta.on.satta.app.R
import com.pixplicity.easyprefs.library.Prefs

class StarLineMarkets : Fragment(R.layout.fragment_star_line_markets) {

    private lateinit var _context: Context
    private lateinit var starBetSingle: LinearLayout
    private lateinit var starBetSingleP: LinearLayout
    private lateinit var starBetDoubleP: LinearLayout
    private lateinit var starBetTripleP: LinearLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setType(view)
        setOnClick()

    }

    private fun setOnClick() {
        starBetSingle.setOnClickListener {
            prefsHelper("star_bet_type", 1)
            Navigation.findNavController(view as View)
                .navigate(R.id.action_starLineMarkets_to_starLineForm)
        }

        starBetSingleP.setOnClickListener {
            prefsHelper("star_bet_type", 2)
            Navigation.findNavController(view as View)
                .navigate(R.id.action_starLineMarkets_to_starLineForm)
        }
        starBetDoubleP.setOnClickListener {
            prefsHelper("star_bet_type", 3)
            Navigation.findNavController(view as View)
                .navigate(R.id.action_starLineMarkets_to_starLineForm)
        }

        starBetTripleP.setOnClickListener {
            prefsHelper("star_bet_type", 4)
            Navigation.findNavController(view as View)
                .navigate(R.id.action_starLineMarkets_to_starLineForm)
        }
    }

    private fun setType(view: View) {
        view.apply {
            starBetSingle = findViewById(R.id.star_single)
            starBetSingleP = findViewById(R.id.star_sp)
            starBetDoubleP = findViewById(R.id.star_dp)
            starBetTripleP = findViewById(R.id.star_tp)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    private fun prefsHelper(key: String, value: Any) {
        if (value == Int) {
            Prefs.putInt(key, value.toString().toInt())
        } else {
            Prefs.putString(key, value.toString())
        }
    }
}