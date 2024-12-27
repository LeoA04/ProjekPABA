package com.example.projekpaba.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DaftarHistory::class], version = 1)
abstract class DaftarHistoryDB : RoomDatabase() {
    abstract fun funDaftarHistoryDAO(): DaftarHistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: DaftarHistoryDB? = null

        @JvmStatic
        fun getDatabase(context: Context): DaftarHistoryDB {
            if (INSTANCE == null) {
                synchronized(DaftarHistoryDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DaftarHistoryDB::class.java, "daftarHistory_db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as DaftarHistoryDB
        }
    }
}