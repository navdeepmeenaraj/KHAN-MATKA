package khansatta.on.satta.app.web_service.model

data class WithdrawRequest(
    val user_id: Int,
    val amount: String,
    val number: String,

    )
