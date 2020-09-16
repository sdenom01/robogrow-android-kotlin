package io.robogrow.requests.grows

import android.content.Context
import com.android.volley.Response
import io.robogrow.classes.Grow
import io.robogrow.requests.AuthenticatedJsonArrayRequest
import io.robogrow.requests.AuthenticatedJsonObjectRequest

class GetGrowById(
    _id: String,
    context: Context,
    listener: Response.Listener<Grow?>,
    errorListener: Response.ErrorListener?
) :
    AuthenticatedJsonObjectRequest<Grow>(
        context,
        Method.GET,
        "https://api.robogrow.io/grows/$_id",
        Grow::class.java,
        null,
        listener,
        errorListener
    )