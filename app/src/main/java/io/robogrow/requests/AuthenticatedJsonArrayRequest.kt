package io.robogrow.requests

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.internal.`$Gson$Types`
import io.robogrow.utils.AppUtils
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.nio.charset.Charset


open class AuthenticatedJsonArrayRequest<T>(
    private val context: Context,
    method: Int,
    url: String?,
    private val clazz: Class<T>?,
    private val headers: MutableMap<String?, String?>?,
    listener: Response.Listener<ArrayList<T?>?>?,
    errorListener: Response.ErrorListener?
) :
    Request<ArrayList<T?>?>(method, url, errorListener) {
    private val gson: Gson? = Gson()
    private val listener: Response.Listener<ArrayList<T?>?>? = listener

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        headers["x-api-token"] = AppUtils.loadUserFromSharedPreferences(context).token
        headers["Content-Type"] = "application/json"
        return headers
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<ArrayList<T?>?>? {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
            )

            val listType: Type? =
                `$Gson$Types`.newParameterizedTypeWithOwner(
                    null,
                    ArrayList::class.java, clazz
                )
            val tList: ArrayList<T?> = gson!!.fromJson(json, listType)
            Response.success(
                tList,
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: ArrayList<T?>?) {
        listener!!.onResponse(response)
    }

}