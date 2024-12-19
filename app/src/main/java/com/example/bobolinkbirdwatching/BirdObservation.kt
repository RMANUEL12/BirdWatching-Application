package com.example.bobolinkbirdwatching

data class BirdObservation (
    val id: String?="",
    val user: String?="",
    val breed: String?="",
    var sightLocation: String?="",
    var dateSpotted: String?=""
)