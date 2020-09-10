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
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.robogrow.ui.MainActivity

import io.robogrow.R
import io.robogrow.ui.login.LoggedInUserView
import io.robogrow.ui.login.LoginActivity
import io.robogrow.utils.AppUtils
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var tvError: TextView
    private lateinit var llWrapper: LinearLayout
    private lateinit var pbLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val email = findViewById<EditText>(R.id.et_email)
        val username = findViewById<EditText>(R.id.et_username)
        val password = findViewById<EditText>(R.id.et_password)
        val password2 = findViewById<EditText>(R.id.et_password_2)
        val register = findViewById<Button>(R.id.register)
        val signIn = findViewById<Button>(R.id.bt_sign_in)

        llWrapper = findViewById(R.id.ll_wrapper)
        tvError = findViewById(R.id.tv_login_error)
        pbLoading = findViewById(R.id.loading)

        registerViewModel = ViewModelProviders.of(
            this,
            RegisterViewModelFactory()
        )
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable register button unless both username / password is valid
            register.isEnabled = registerState.isDataValid

            if (registerState.emailError != null) {
                email.error = getString(registerState.emailError)
            }

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

            pbLoading.visibility = View.GONE
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

        email.afterTextChanged {
            registerViewModel.registerDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                password2.text.toString()
            )
        }

        username.afterTextChanged {
            registerViewModel.registerDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                password2.text.toString()
            )
        }

        password.afterTextChanged {
            registerViewModel.registerDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                password2.text.toString()
            )
        }

        password2.afterTextChanged {
            registerViewModel.registerDataChanged(
                email.text.toString(),
                username.text.toString(),
                password.text.toString(),
                password2.text.toString()
            )
        }

        password2.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString(),
                    password2.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            email.text.toString(),
                            username.text.toString(),
                            password.text.toString(),
                            password2.text.toString(),
                            this@RegisterActivity
                        )
                }
                false
            }

            signIn.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java).apply {}
                startActivity(intent)
            }

            register.setOnClickListener {
                pbLoading.visibility = View.VISIBLE

                registerUser(
                    email.text.toString(),
                    username.text.toString(),
                    password.text.toString(),
                    password2.text.toString()
                )
            }
        }
    }

    private fun registerUser(email: String, username: String, pass: String, pass2: String) {
        val stringRequest: StringRequest = object : StringRequest( Method.POST, "https://api.robogrow.io/register",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)

                    // Create SharedPreferences object.
                    AppUtils.saveUserToSharedPreferences(this, response)

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java).apply {

                    }

                    startActivity(intent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                val errString = String(error.networkResponse.data)
                val errors = JSONObject(errString).getJSONArray("errors")


                tvError.visibility = View.VISIBLE
                tvError.text = errors.getJSONObject(0).getString("msg")
                Log.d(errString, errors.toString())

                // Make the form and other components visible
                llWrapper.visibility = View.VISIBLE
                pbLoading.visibility = View.GONE
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["email"] = email
                params["username"] = username
                params["password"] = pass
                params["confirmPassword"] = pass2
                params["type"] = "0"
                
                return params
            }
        }

        // Make form and other components invisible
        llWrapper.visibility = View.GONE

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
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
