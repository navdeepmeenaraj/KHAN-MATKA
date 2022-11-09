package khansatta.on.satta.app.web_service.model.login

data class AuthResponse(
    val error: Any,
    val isError: Boolean,
    val message: String,
    val token: String,
    val user: User
)

data class User(
    val created_at: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val is_verified: Int,
    val updated_at: String,
    val username: String,
    val passcode: Int
)