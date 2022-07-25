package com.example.kutu.data.db.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "address")
data class AddressEntity(
    @PrimaryKey(autoGenerate = false)
    val addressId: String,
)
