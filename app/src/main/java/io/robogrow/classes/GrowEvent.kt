package io.robogrow.classes

import java.util.*

data class GrowEvent(
    val _id: String,
    val growId: String,
    val temp: Float,
    val humidity: Float,
    val lux: Float,
    val infrared: Float,
    val createDate: Date
)