package khansatta.on.satta.app.web_service.model.gali

data class bidhis(
    val bet_amount: String,
    val bet_date: String,
    val bet_digit: String,
    val bet_rate: Int,
    val gali_id: String,
    val id: Int,
    val is_win: Int,
    val user_id: String
)