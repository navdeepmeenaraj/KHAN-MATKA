package khansatta.on.satta.app.web_service.model

data class MarketData(
    val close_market_status: Int,
    val created_at: String,
    val id: Int,
    val market_close_time: String,
    val market_id: Int,
    val market_name: String,
    val market_open_time: String,
    val open_pana: String,
    val close_pana: String,
    val market_status: Int,
    val open_market_status: Int,
    val updated_at: String
)