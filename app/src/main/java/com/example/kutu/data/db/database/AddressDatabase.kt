package com.example.kutu.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kutu.data.db.dao.AddressDao
import com.example.kutu.data.db.entity.AddressEntity


@Database(entities = [AddressEntity::class], version = 1)
abstract class AddressDatabase: RoomDatabase() {

    abstract fun addressDao(): AddressDao

}