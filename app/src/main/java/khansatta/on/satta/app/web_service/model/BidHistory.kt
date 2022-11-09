package khansatta.on.satta.app.web_service.model

data class BidHistory(
    val bet_amount: Int,
    val bet_date: String,
    val bet_digit: String,
    val bet_rate: Int,
    val bet_type: String,
    val created_at: String,
    val id: Int,
    val is_win: Int,
    val market: Market,
    val market_id: Int,
    val market_session: String,
    val updated_at: String,
    val user_id: Int
)

data class Market(
    val close_market_status: Int,
    val close_pana: String,
    val id: Int,
    val market_close_time: String,
    val market_id: Int,
    val market_name: String,
    val market_open_time: String,
    val market_status: Int,
    val open_market_status: Int,
    val open_pana: String
)