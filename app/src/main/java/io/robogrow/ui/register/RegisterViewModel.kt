package io.robogrow.ui.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.robogrow.data.RegisterRepository
import io.robogrow.data.Result

import io.robogrow.R

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(username: String, password: String, password2: String, context: Context) {
        // can be launched in a separate asynchronous job
        val result = registerRepository.register(username, password, password2)

        if (result is Result.Success) {
            _registerResult.value =
                RegisterResult(success = RegisteredUserView(displayName = result.data.displayName))
        } else {
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    fun registerDataChanged(username: String, password: String, password2: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if (!doPasswordsMatch(password, password2)) {
            _registerForm.value =
                RegisterFormState(passwordConfirmationError = R.string.invalid_password_confirmation)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    // A placeholder password validation check
    private fun doPasswordsMatch(password: String, password2: String): Boolean {
        return password == password2
    }
}
