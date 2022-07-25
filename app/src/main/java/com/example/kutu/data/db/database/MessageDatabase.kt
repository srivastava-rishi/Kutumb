package com.example.kutu.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kutu.data.db.dao.MessageDao
import com.example.kutu.data.db.entity.MessageEntity


@Database(entities = [MessageEntity::class], version = 1)
abstract class MessageDatabase: RoomDatabase() {

    abstract fun messageDao(): MessageDao

}