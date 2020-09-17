package io.robogrow.requests.grows

import android.content.Context
import com.android.volley.Response
import io.robogrow.classes.Grow
import io.robogrow.requests.AuthenticatedJsonArrayRequest

class GetGrowAllGrowsForUserId(
    context: Context,
    listener: Response.Listener<ArrayList<Grow?>?>
) :
    AuthenticatedJsonArrayRequest<Grow>(
        context,
        Method.GET,
        "https://api.robogrow.io/grows",
        Grow::class.java,
        mutableMapOf(),
        listener
    )