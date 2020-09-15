package io.robogrow.classes

import org.json.JSONArray

data class Grow(
    val _id: String,
    val userId: String,
    val name: String,
    val numPlants: Int,
    val growStage: String,
    val config: GrowConfig,
    val events: ArrayList<GrowEvent>,
    val current: GrowEvent
)