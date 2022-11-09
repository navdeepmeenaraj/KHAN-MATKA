package khansatta.on.satta.app.web_service.model

data class MarketRates(
    val created_at: Any,
    val id: Int,
    val market_digits_from: Int,
    val market_digits_to: Int,
    val market_id: Int,
    val market_key: String,
    val market_name: String,
    val market_rate: Int
)