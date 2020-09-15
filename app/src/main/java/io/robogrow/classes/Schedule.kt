package io.robogrow.classes

import org.json.JSONArray

data class Schedule(
    val id: Int,
    val type: Int,
    val name: String,
    val events: JSONArray // Replace with actual object
)