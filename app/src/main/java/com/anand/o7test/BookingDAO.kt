package com.anand.o7test

import androidx.room.Insert
import androidx.room.Query

class BookingDAO {
    @Insert
    fun insertBooking(booking: Booking)
    @Query("SELECT * FROM bookings")
    fun getAllBookings(): List<Booking>


}