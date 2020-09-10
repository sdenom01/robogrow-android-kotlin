package io.robogrow.ui.register

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val emailError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val passwordConfirmationError: Int? = null,
    val isDataValid: Boolean = false
)
