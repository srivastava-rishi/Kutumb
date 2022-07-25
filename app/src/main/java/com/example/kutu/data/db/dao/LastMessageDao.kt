package com.example.kutu.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kutu.data.db.entity.LastMessageEntity


@Dao
interface LastMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastMessageAddress(lastMessageEntity: LastMessageEntity)

    @Query("SELECT * FROM lastMessage ORDER BY timeStamp DESC")

    fun getAllLastMessages():List<LastMessageEntity>

}