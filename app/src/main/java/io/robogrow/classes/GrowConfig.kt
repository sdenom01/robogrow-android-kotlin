package io.robogrow.classes

import org.json.JSONArray

data class GrowConfig (
    val _id : String,
    val userId: String,
    val name: String,
    val lightsOn: String,
    val lightsOff: String,
    val tempLow: Float,
    val tempHigh: Float,
    val humidityLow: Float,
    val humidityHigh: Float
)