package io.robogrow.classes

data class Grow(
    val _id: String,
    val userId: String,
    val name: String,
    val numPlants: Int,
    val growStage: String
)