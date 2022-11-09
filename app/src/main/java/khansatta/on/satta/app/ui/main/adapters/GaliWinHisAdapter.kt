package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.gali.bidhis

class GaliWinHisAdapter(private var wins: List<bidhis>) :
    RecyclerView.Adapter<GaliWinHisAdapter.GaliWinHisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = GaliWinHisViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.gali_win_item, parent, false)
    )

    override fun getItemCount() = wins.size

    override fun onBindViewHolder(holder: GaliWinHisViewHolder, position: Int) {
        holder.bind(wins[position])
    }

    class GaliWinHisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bidDate = view.findViewById<TextView>(R.id.gali_bid_date)
        private val bidDigit = view.findViewById<TextView>(R.id.gali_bid_digit)
        private val bidAmount = view.findViewById<TextView>(R.id.gali_bid_amount)
        private val bidMarketId = view.findViewById<TextView>(R.id.gali_bid_market_id)
        private val wonAmount = view.findViewById<TextView>(R.id.gali_won_amount)

        @SuppressLint("SetTextI18n")
        fun bind(bids: bidhis) {
            bidDate.text = "Date : " + bids.bet_date
            bidDigit.text = "Digit : " + bids.bet_digit
            bidAmount.text = "Amount : - " + bids.bet_amount
            wonAmount.text = "Won Amount : + " + bids.bet_amount.toInt() * bids.bet_rate
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