package khansatta.on.satta.app.web_service.model

data class WinHistory(
    val bet_amount: Int,
    val bet_date: String,
    val bet_digit: String,
    val bet_rate: Int,
    val bet_type: String,
    val created_at: String,
    val id: Int,
    val is_win: Int,
    val market_id: Int,
    val market_session: String,
    val updated_at: String,
    val user_id: Int
)