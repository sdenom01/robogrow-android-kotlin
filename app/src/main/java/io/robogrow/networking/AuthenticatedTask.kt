package io.robogrow.networking

import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.robogrow.VolleySingleton

abstract class AuthenticatedTask<T>(t: T) {
    abstract var jsonObjectRequest : JsonObjectRequest;
    fun sendRequest(){

        jsonObjectRequest.headers
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun receiveRequest(payload:String, type:Class<out T>) : T{
        return Gson().fromJson(payload, object: TypeToken<T>(){}.type)
    }
}