package khansatta.on.satta.app.web_service.model

data class AppConfig(
    val banner_image: String,
    val id: Int,
    val payment_address: String,
    val phone_number: String,
    val telegram: String,
    val whatsapp_numebr: String,
    val min_bet: Int,
    val max_bet: Int,
)