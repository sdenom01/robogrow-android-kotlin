package io.robogrow.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class Serializer<T> {
    companion object {
        inline fun <reified T> fromJson(json: String): T {
            return Gson().fromJson(json, object: TypeToken<T>(){}.type)
        }
    }
}