package khansatta.on.satta.app.web_service.model

data class PasswordReset(
    val mobile: String,
    val password: String,
    val confirmPassword: String
)
