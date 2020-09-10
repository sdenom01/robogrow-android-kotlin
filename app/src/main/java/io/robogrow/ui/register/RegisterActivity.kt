package io.robogrow.ui.register

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import io.robogrow.ui.MainActivity

import io.robogrow.R
import io.robogrow.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val username = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val password2 = findViewById<EditText>(R.id.et_password_2)
        val register = findViewById<Button>(R.id.register)
        val signIn = findViewById<Button>(R.id.bt_sign_in)
        val loading = findViewById<ProgressBar>(R.id.loading)

        registerViewModel = ViewModelProviders.of(
            this,
            RegisterViewModelFactory()
        )
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable register button unless both username / password is valid
            register.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                username.error = getString(registerState.usernameError)
            }

            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }

            if (registerState.passwordConfirmationError != null) {
                password2.error = getString(registerState.passwordConfirmationError)
            }
        })

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val registerResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (registerResult.error != null) {
                showRegisterFailed(registerResult.error)
            }

            if (registerResult.success != null) {
                updateUiWithUser(registerResult.success)
            }

            setResult(Activity.RESULT_OK)

            //Complete and destroy register activity once successful
            finish()
        })

        username.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString()
            )
        }

        password.afterTextChanged {
            registerViewModel.registerDataChanged(
                username.text.toString(),
                password.text.toString(),
                password2.text.toString()
            )
        }

        password2.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    password2.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            username.text.toString(),
                            password.text.toString(),
                            password2.text.toString(),
                            this@RegisterActivity
                        )
                }
                false
            }

            signIn.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java).apply {

                }

                startActivity(intent)
            }

            register.setOnClickListener {
                loading.visibility = View.VISIBLE
                registerViewModel.register(
                    username.text.toString(),
                    password.text.toString(),
                    password2.text.toString(),
                    this@RegisterActivity
                )
            }
        }
    }

    private fun updateUiWithUser(model: RegisteredUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName

        val intent = Intent(this@RegisterActivity, MainActivity::class.java).apply {

        }

        startActivity(intent)

        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showRegisterFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
