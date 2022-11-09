package khansatta.on.satta.app.ui.main.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.WithdrawList
import khansatta.on.satta.app.basic_utils.Constants
import kotlinx.android.synthetic.main.wi_td_raw_request_item.view.*

class WHAdapter(private var itemList: List<WithdrawList>) :
    RecyclerView.Adapter<WHAdapter.WithdrawListAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = WithdrawListAdapter(
        LayoutInflater.from(parent.context).inflate(R.layout.wi_td_raw_request_item, parent, false)
    )

    override fun getItemCount() = itemList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WithdrawListAdapter, position: Int) {
        holder.bind(itemList[position])
    }

    class WithdrawListAdapter(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.request_id
        private val requestAmount: TextView = view.request_amount
        private val createdAt: TextView = view.created_at
        private val requestMessage: TextView = view.request_message
        private val paymentNumber: TextView = view.payment_number
        private val requestStatus: TextView = view.request_status

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(withdrawList: WithdrawList) {
            id.text = withdrawList.id
            requestAmount.text = "- ${withdrawList.request_amount}"
            createdAt.text = withdrawList.created_at
            requestMessage.text = withdrawList.request_message
            paymentNumber.text = withdrawList.payment_number
            requestStatus.text = withdrawList.request_status

            when (withdrawList.request_status) {
                Constants.APPROVED -> {
                    requestStatus.setTextColor(Color.parseColor("#2ecc71"))
                }
                Constants.REJECTED -> {
                    requestStatus.setTextColor(Color.parseColor("#c0392b"))

                }
                Constants.PENDING -> {
                    requestStatus.setTextColor(Color.parseColor("#f39c12"))
                }
            }
        }
    }
}