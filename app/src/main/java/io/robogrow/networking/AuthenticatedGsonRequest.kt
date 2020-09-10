package io.robogrow.networking

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.robogrow.utils.AppUtils
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.nio.charset.Charset

open class AuthenticatedGsonRequest<T>(
    url: String,
    method: Int,
    private val type: Class<T>,
    private val headers: MutableMap<String, String>?,
    private val listener: Response.Listener<T>,
    errorListener: Response.ErrorListener,
    private val context: Context
) : Request<T>(method, url, errorListener) {
    private val gson = Gson()

    override fun getHeaders(): MutableMap<String, String> {
        var retHeaders: HashMap<String, String> = (headers?: hashMapOf()) as HashMap<String, String>
        retHeaders["x-api-token"] = fetchApiKey()

        return retHeaders
    }

    override fun deliverResponse(response: T) = listener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
            )

            Response.success(
                Gson().fromJson(json, type),
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        } catch(e: Exception) {
            Response.error(ParseError(e))
        }
    }

    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun fetchApiKey(): String = AppUtils.loadUserFromSharedPreferences(context).token
}