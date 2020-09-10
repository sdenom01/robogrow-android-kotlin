package io.robogrow.networking.grows

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import io.robogrow.classes.User
import io.robogrow.networking.AuthenticatedErrorListener
import io.robogrow.networking.AuthenticatedGsonRequest

/**
 * Make a GET request and return a parsed object from JSON.
 *
 * @param url URL of the request to make
 * @param clazz Relevant class object, for Gson's reflection
 * @param headers Map of request headers
 */
class GetAllGrowsForUserId<T : User>(
    context: Context,
    listener: Response.Listener<T>,
    private val clazz: Class<T>
) : AuthenticatedGsonRequest<T>(
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
