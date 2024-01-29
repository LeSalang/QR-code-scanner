package com.lesa.qrcodescanner.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Product::class],
    version = 1
)
abstract class MainDB: RoomDatabase() {
    abstract val dao: Dao
}