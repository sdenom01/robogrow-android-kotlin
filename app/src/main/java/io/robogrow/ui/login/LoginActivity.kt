package io.robogrow.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.robogrow.MainActivity
import io.robogrow.R
import io.robogrow.ui.register.RegisterActivity
import io.robogrow.utils.AppUtils
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var email: String
    private lateinit var pass: String

    private lateinit var tvError: TextView
    private lateinit var llWrapper: LinearLayout
    private lateinit var pbLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val login = findViewById<Button>(R.id.login)
        val signUp = findViewById<Button>(R.id.bt_sign_up)

        llWrapper = findViewById(R.id.ll_wrapper)
        tvError = findViewById(R.id.tv_login_error)
        pbLoading = findViewById(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }

            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            pbLoading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                loginUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )

            email = username.text.toString()
        }

        password.apply {
            password.afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )

                pass = password.text.toString()
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                pbLoading.visibility = View.VISIBLE
//                loginViewModel.login(username.text.toString(), password.text.toString())
                loginUser(null)
            }

            signUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java).apply {

                }

                startActivity(intent)
            }
        }
    }

    private fun loginUser(model: LoggedInUserView?) {
        val stringRequest: StringRequest = object : StringRequest( Method.POST, "https://api.robogrow.io/authenticate",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)

                    // Create SharedPreferences object.
                    AppUtils.saveUserToSharedPreferences(this, response)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {

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
                params["password"] = pass

                Log.d("PARAMS", params.toString())
                return params
            }
        }

        // Make form and other components invisible
        llWrapper.visibility = View.GONE

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }


    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()

        Log.wtf("WTF", "WHY THE FUCK? PAUSE")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.wtf("WTF", "WHY THE FUCK? DESTROY")
    }

    override fun onStop() {
        super.onStop()

        Log.wtf("WTF", "WHY THE FUCK? STOP")
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
