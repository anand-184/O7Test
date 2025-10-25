package com.anand.o7test

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Booking::class], version = 1)
abstract class BookingDatabase: RoomDatabase() {
    abstract fun bookingDao(): BookingDAO

    companion object {
        private var bookingDatabase: BookingDatabase? = null
        fun getInstance(context: Context): BookingDatabase {
            if (BookingDatabase == null) {
                BookingDatabase = Room.databaseBuilder(context, BookingDatabase::class.java, "BookingDatabase")
                        .allowMainThreadQueries().build()
            }
            return bookingDatabase!!
        }
    }


}