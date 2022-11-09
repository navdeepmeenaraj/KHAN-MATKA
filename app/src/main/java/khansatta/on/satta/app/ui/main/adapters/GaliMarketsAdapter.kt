package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.gali.GaliMarkets
import khansatta.on.satta.app.basic_utils.BasicUtils
import com.pixplicity.easyprefs.library.Prefs

class GaliMarketsAdapter(private var gali: List<GaliMarkets>) :
    RecyclerView.Adapter<GaliMarketsAdapter.GaliMarketsViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GaliMarketsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.gali_market_item, parent, false)

        return GaliMarketsViewHolder(view)
    }

    override fun getItemCount() = gali.size

    override fun onBindViewHolder(holder: GaliMarketsViewHolder, position: Int) {
        holder.bind(gali[position])
    }

    class GaliMarketsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val galiName = view.findViewById<TextView>(R.id.textview_gali_name)
        private val galiTime = view.findViewById<TextView>(R.id.textview_gali_time)
        private val galiResult = view.findViewById<TextView>(R.id.textview_gali_result)
        private val galiStatus = view.findViewById<TextView>(R.id.textview_status_subtext)
        private val galiPlayButton = view.findViewById<ImageView>(R.id.buttonGaliPlay)
        private val galiChartButton = view.findViewById<ImageView>(R.id.galiChartIcon)

        @SuppressLint("SetTextI18n")
        fun bind(gali: GaliMarkets) {
            gali.apply {
                galiName.text = gali_name
                galiTime.text = "Result Time- " + BasicUtils.toDate(gali.open_time)
                galiStatus.text = BasicUtils.convertToStatus(gali.is_closed)
                galiResult.text = gali.result
            }
            galiChartButton.setOnClickListener {
                Prefs.putInt("gali_id", gali.id)
                Navigation.findNavController(it).navigate(R.id.galiChartFragment)
            }
            if (gali.is_closed == 0) {
                galiStatus.setTextColor(Color.parseColor("#ff4757"))
                galiPlayButton.setOnClickListener {
                    val vibe =
                        itemView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibe.vibrate(100)
                }
            } else {
                galiStatus.setTextColor(Color.parseColor("#2ecc71"))
                galiPlayButton.setOnClickListener { view ->
                    handleButtonOnClick(gali.id, gali.open_time, view)
                }
            }
        }

        private fun handleButtonOnClick(galiId: Int, galiTime: String, view: View) {
            Prefs.putInt("gali_id", galiId)
            Prefs.putString("gali_time", galiTime)
            Navigation.findNavController(view)
                .navigate(R.id.action_galiMarketsFragment_to_galiPlaceForm)
        }
    }

}