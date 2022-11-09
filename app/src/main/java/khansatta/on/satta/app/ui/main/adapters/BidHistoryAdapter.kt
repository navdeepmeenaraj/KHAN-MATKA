package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.BidHistory
import kotlinx.android.synthetic.main.bid_history_item.view.*
import java.util.*

class BidHistoryAdapter(private var bids: List<BidHistory>) :
    RecyclerView.Adapter<BidHistoryAdapter.BetViewAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = BetViewAdapter(
        LayoutInflater.from(parent.context).inflate(R.layout.bid_history_item, parent, false)
    )

    override fun getItemCount() = bids.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: BetViewAdapter, position: Int) {
        holder.bind(bids[position])
    }

    class BetViewAdapter(view: View) : RecyclerView.ViewHolder(view) {
        private val mBetDate = view.tv_bet_date
        private val mBetDigit = view.tv_bet_digit
        private val mBetAmount = view.tv_bet_amount
        private val mSession = view.tv_market_session
        private val mMarketId = view.tv_market_id

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(bets: BidHistory) {
            mBetDate.text = bets.bet_date
            mBetDigit.text = "Digit : " + bets.bet_digit
            mBetAmount.text = "Points - " + bets.bet_amount.toString()
            mSession.text = "Session : " + bets.market_session.toUpperCase(Locale.ENGLISH)
            mMarketId.text = bets.market.market_name + " (${bets.bet_type.replace("_", " ")})"
        }
    }
}