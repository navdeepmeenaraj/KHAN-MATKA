package khansatta.on.satta.app.web_service.model

data class RegisterResponse(
    val error: String,
    val isError: Boolean,
    val message: String,
    val passcode: Int,
    val token: String,
    val is_verified: Int,
    val userId: Int
)