package com.example.kutu.data.db.entity

import android.graphics.Bitmap
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "lastMessage")
data class LastMessageEntity(
    @PrimaryKey(autoGenerate = false)
    val addressId: String,
    val lastMessage: String,
    val imageColor:Int,
    val type:Int,
    val timeStamp: Long,
)
