package khansatta.on.satta.app.web_service.model

data class PaymentDetails(
    val business_name: String,
    val id: Int,
    val max_amount: Int,
    val min_amount: Int,
    val payment_desc: String,
    val upi_id: String,
    val min_withdrawal: Int,
    val withdrawal_time_title: String,
    val merchant_code: String
)