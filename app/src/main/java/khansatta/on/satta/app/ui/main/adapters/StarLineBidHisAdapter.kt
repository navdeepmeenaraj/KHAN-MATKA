package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.starline.StarlineBids

class StarLineBidHisAdapter(private var wins: List<StarlineBids>) :
    RecyclerView.Adapter<StarLineBidHisAdapter.StarBidHisView>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = StarBidHisView(
        LayoutInflater.from(parent.context).inflate(R.layout.star_bid_item, parent, false)
    )

    override fun getItemCount() = wins.size

    override fun onBindViewHolder(holder: StarBidHisView, position: Int) {
        holder.bind(wins[position])
    }

    class StarBidHisView(view: View) : RecyclerView.ViewHolder(view) {
        private val bidDate = view.findViewById<TextView>(R.id._bid_date)
        private val bidDigit = view.findViewById<TextView>(R.id._bid_digit)
        private val bidAmount = view.findViewById<TextView>(R.id._bid_amount)
        private val bidType = view.findViewById<TextView>(R.id._bid_type)
        private val bidMarketId = view.findViewById<TextView>(R.id._bid_market_id)

        @SuppressLint("SetTextI18n")
        fun bind(bids: StarlineBids) {
            bidDate.text = "Date : " + bids.bet_date
            bidDigit.text = "Digit : " + bids.bet_digit
            bidAmount.text = "Amount : - " + bids.bet_amount.toString()
            bidMarketId.text = "Market ID : " + bids.market_id.toString()
            bidType.text = "Type : " + getMarketType(bids.bet_type)
        }

        private fun getMarketType(betType: Int): String {
            return when (betType) {
                1 -> {
                    "Single"
                }
                2 -> {
                    "Single Panel"
                }
                3 -> {
                    "Double Panel"
                }
                else -> {
                    "Triple Panel"
                }
            }
        }
    }

}