package io.robogrow.networking

import android.content.Context
import android.content.Intent
import com.android.volley.Response
import com.android.volley.VolleyError
import io.robogrow.MainActivity
import io.robogrow.ui.login.LoginActivity

class AuthenticatedErrorListener (val context: Context) : Response.ErrorListener {
    override fun onErrorResponse(error: VolleyError?) {
        val errorCode = error?.networkResponse?.statusCode ?: 500

        when (errorCode) {
            401 -> forceReauthActivity()
        }
    }

    private fun forceReauthActivity() {
        val intent = Intent(context, MainActivity::class.java).apply {}

        context.startActivity(intent)
    }
}