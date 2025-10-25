package com.anand.o7test

import androidx.room.Entity

@Entity(tableName = "booking")
data class Booking(
    val passengerName: String,
    val age: Int,
    val noOfTickets: Int,
    val distance: Int,
    val travelClass: String,
    val travelTime: String,
    val totalFare: Double
)
