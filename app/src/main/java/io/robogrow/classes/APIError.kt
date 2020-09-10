package io.robogrow.classes

import org.json.JSONArray

data class APIError (
    val status: Int,
    val error: String,
    // This is sort of hacky, I need to make the API only return a single error value
    val errors: JSONArray
)