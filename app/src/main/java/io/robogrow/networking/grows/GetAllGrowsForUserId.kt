package io.robogrow.networking.grows

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import io.robogrow.classes.Grow
import io.robogrow.networking.AuthenticatedErrorListener
import io.robogrow.networking.AuthenticatedGsonListRequest
import org.json.JSONArray

/**
 * Make a GET request and return a parsed object from JSON.
 *
 * @param url URL of the request to make
 * @param clazz Relevant class object, for Gson's reflection
 * @param headers Map of request headers
 */
class GetAllGrowsForUserId<T : JSONArray>(
    context: Context,
    listener: Response.Listener<T>,
    private val clazz: Class<T>
) : AuthenticatedGsonListRequest<T>(
    "https://api.robogrow.io/grows",
    Method.GET,
    clazz,
    null,
    listener,
    AuthenticatedErrorListener(context),
    context
) {
    override fun setRetryPolicy(retryPolicy: RetryPolicy?): Request<*> {
        return super.setRetryPolicy(DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ))
    }
}
