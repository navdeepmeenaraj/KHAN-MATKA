package khansatta.on.satta.app.web_service.model

data class WalletTransaction(
    val amount: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val type: Int,
    val updated_at: String,
    val user_id: Int
)