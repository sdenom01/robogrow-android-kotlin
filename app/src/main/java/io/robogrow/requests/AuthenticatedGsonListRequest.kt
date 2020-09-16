package io.robogrow.requests

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.robogrow.utils.AppUtils
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

open class AuthenticatedGsonListRequest<T>(
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

            val collectionType = object :
                TypeToken<Collection<T?>?>() {}.type

            val x : T = fromJson(json)

            Response.success(
                x,
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

    private inline fun fromJson(json: String): T {
        return Gson().fromJson<T>(json, object: TypeToken<T>() {}.type)
    }

//    inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object: TypeToken<T>() {}.type)

    private fun fetchApiKey(): String = AppUtils.loadUserFromSharedPreferences(context).token
}