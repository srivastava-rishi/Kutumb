package com.example.kutu.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kutu.data.db.entity.AddressEntity


@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageAddress(addressEntity: AddressEntity)

    @Query("SELECT * FROM address ")
    fun getAllAddress(): List<AddressEntity>

}