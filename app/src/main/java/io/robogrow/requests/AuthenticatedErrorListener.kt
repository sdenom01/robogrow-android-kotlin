package io.robogrow.requests

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.google.gson.Gson
import io.robogrow.classes.APIError
import io.robogrow.ui.login.LoginActivity
import io.robogrow.utils.AppUtils

class AuthenticatedErrorListener (val context: Context) : Response.ErrorListener {
    override fun onErrorResponse(e: VolleyError?) {
        if (e !is TimeoutError) {
            val errorCode = e?.networkResponse?.statusCode ?: 500

            try {
                val errString = String(e?.networkResponse?.data!!)
                val error: APIError = Gson().fromJson(errString, APIError::class.java)

                Toast.makeText(context, error.error, Toast.LENGTH_LONG).show()

                when (errorCode) {
                    401 -> forceLogout()
                }
            } catch (e: KotlinNullPointerException) {
                Log.e("NullPointer", e.cause.toString())
            }
        } else {
            // HANDLE TIMEOUT?
            Toast.makeText(context, "Request timed out!", Toast.LENGTH_LONG).show()
        }
    }

    private fun forceLogout() {
        // We might need to do more here
        AppUtils.removeUserFromSharedPreferences(context)

        val intent = Intent(context, LoginActivity::class.java).apply {}

        context.startActivity(intent)
    }
}