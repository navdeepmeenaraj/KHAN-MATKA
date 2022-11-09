package khansatta.on.satta.app.web_service.model

data class MarketDataAndDigits(
    val date_today: String,
    val market_data: Data,
    val market_numbers: List<Int>
)

data class Data(
    val id: Int,
    val market_digits_from: Int,
    val market_digits_to: Int,
    val market_id: Int,
    val market_key: String,
    val market_name: String,
    val market_rate: Int
)