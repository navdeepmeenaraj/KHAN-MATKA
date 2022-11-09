package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.MarketRates
import kotlinx.android.synthetic.main.item_game_rate.view.*

class GameRatesAdapter(private var itemList: List<MarketRates>) :
    RecyclerView.Adapter<GameRatesAdapter.MarketRatesAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MarketRatesAdapter(
        LayoutInflater.from(parent.context).inflate(R.layout.item_game_rate, parent, false)
    )

    override fun getItemCount() = itemList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MarketRatesAdapter, position: Int) {
        holder.bind(itemList[position])
    }

    class MarketRatesAdapter(view: View) : RecyclerView.ViewHolder(view) {

        private val gameName = view.game_name
        private val gameRate = view.game_rate

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(marketRates: MarketRates) {
            gameName.text = marketRates.market_name
            gameRate.text = "10 - ${marketRates.market_rate * 10}"
        }
    }
}