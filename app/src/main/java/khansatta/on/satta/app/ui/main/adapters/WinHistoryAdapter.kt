package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.WinHistory
import kotlinx.android.synthetic.main.bid_history_item.view.*

class WinHistoryAdapter(private var wins: List<WinHistory>) :
    RecyclerView.Adapter<WinHistoryAdapter.WinHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = WinHistoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.win_history_item, parent, false)
    )

    override fun getItemCount() = wins.size

    override fun onBindViewHolder(holder: WinHistoryViewHolder, position: Int) {
        holder.bind(wins[position])
    }

    class WinHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mBetDate = view.tv_bet_date
        private val mBetDigit = view.tv_bet_digit
        private val mBetAmount = view.tv_bet_amount
        private val mSession = view.tv_market_session
        private val mMarketId = view.tv_market_id

        @SuppressLint("SetTextI18n")
        fun bind(bets: WinHistory) {
            mBetDate.text = bets.bet_date
            mBetDigit.text = "Digit : " + bets.bet_digit
            mBetAmount.text = "Amount : + " + bets.bet_rate * bets.bet_amount
            mSession.text = "Session : " + bets.market_session
            mMarketId.text =
                "Market ID : " + bets.market_id.toString() + " (${bets.bet_type.replace("_", " ")})"


        }
    }
}