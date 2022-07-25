package com.example.kutu.data.db.entity

import androidx.annotation.Keep
import androidx.room.Entity

@Keep
@Entity(tableName = "message" , primaryKeys = ["messageId","addressId"])
data class MessageEntity(
    val messageId: String,
    val addressId: String,
    val message: String,
    val timestamp: Long,
    val type:Int
)
