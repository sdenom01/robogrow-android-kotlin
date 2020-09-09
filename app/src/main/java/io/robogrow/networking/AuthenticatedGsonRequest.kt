package io.robogrow.networking

import com.android.volley.Request
import com.android.volley.Response
import android.content.Context

class AuthenticatedGsonRequest<T>(
    url:String,
    method:Method,
    private val type:Class<T>,
    private val headers:MutableMap<String, String>?,
    private val listener:Response.Listener<T>,
    errorListener: Response.ErrorListener,
    private val context: Context
) : Request<T>(method, url, errorListener){

    override fun getHeaders(): MutableMap<String, String> {
        var retHeaders : MutableMap<String, String>
        val sharedPref : SharedPreferences = getSharedPreferences( , 1)

        if (headers != null) {
            retHeaders = headers
            retHeaders.put("x-api-token", fetchApiKey())
        }
        else retHeaders = super.getHeaders()

        return retHeaders;
    }

    override fun deliverResponse(response: T) = listener.onResponse(response)

    fun fetchApiKey() : String = context.getSharedPreferences()
        .getString("x-api-token", null) ?: ""
}