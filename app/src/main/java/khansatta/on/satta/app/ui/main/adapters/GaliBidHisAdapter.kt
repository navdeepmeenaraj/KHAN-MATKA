package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.gali.bidhis

class GaliBidHisAdapter(private var wins: List<bidhis>) :
    RecyclerView.Adapter<GaliBidHisAdapter.GaliBidHisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = GaliBidHisViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.gali_bid_item, parent, false)
    )

    override fun getItemCount() = wins.size

    override fun onBindViewHolder(holder: GaliBidHisViewHolder, position: Int) {
        holder.bind(wins[position])
    }

    class GaliBidHisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bidDate = view.findViewById<TextView>(R.id.gali_bid_date)
        private val bidDigit = view.findViewById<TextView>(R.id.gali_bid_digit)
        private val bidAmount = view.findViewById<TextView>(R.id.gali_bid_amount)
        private val bidMarketId = view.findViewById<TextView>(R.id.gali_bid_market_id)

        @SuppressLint("SetTextI18n")
        fun bind(bids: bidhis) {
            bidDate.text = "Date : " + bids.bet_date
            bidDigit.text = "Digit : " + bids.bet_digit
            bidAmount.text = "Amount : - " + bids.bet_amount
            bidMarketId.text = "Market Name : " + getMarketType(bids.gali_id.toInt())
        }

        private fun getMarketType(betType: Int): String {
            return when (betType) {
                1 -> {
                    "Dishawar"
                }
                2 -> {
                    "Faridabad"
                }
                3 -> {
                    "Gaziabad"
                }
                else -> {
                    "Gali"
                }
            }
        }
    }

}