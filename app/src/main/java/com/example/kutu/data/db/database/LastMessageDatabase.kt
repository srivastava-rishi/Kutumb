package com.example.kutu.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kutu.data.db.dao.LastMessageDao
import com.example.kutu.data.db.entity.LastMessageEntity


@Database(entities = [LastMessageEntity::class], version = 1)
abstract class LastMessageDatabase: RoomDatabase() {

    abstract fun lastMessageDao(): LastMessageDao

}