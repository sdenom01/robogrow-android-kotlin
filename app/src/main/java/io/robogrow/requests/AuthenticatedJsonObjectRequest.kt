package io.robogrow.requests

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.robogrow.utils.AppUtils
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

open class AuthenticatedJsonObjectRequest<T>(
    private val context: Context,
    method: Int,
    url: String?,
    private val clazz: Class<T>,
    jsonParams: T?,
    private val listener: Response.Listener<T?>?,
    errorListener: Response.ErrorListener?
) :
    JsonRequest<T>(method, url, jsonParams.toString(), listener, errorListener) {
    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        headers["x-api-token"] = AppUtils.loadUserFromSharedPreferences(context).token
        headers["Content-Type"] = "application/json"
        return headers
    }

    override fun setRetryPolicy(retryPolicy: RetryPolicy?): Request<*> {
        return super.setRetryPolicy(
            DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
    }

    override fun deliverResponse(response: T) = listener!!.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                Gson().fromJson(json, clazz),
                HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}