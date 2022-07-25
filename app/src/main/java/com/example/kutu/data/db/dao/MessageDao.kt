package com.example.kutu.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kutu.data.db.entity.AddressEntity
import com.example.kutu.data.db.entity.LastMessageEntity
import com.example.kutu.data.db.entity.MessageEntity

@Dao
interface MessageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageEntity: MessageEntity)

    @Query("SELECT * FROM message WHERE addressId = :addressId ORDER BY timestamp ASC")
     fun getAllMessages(addressId:String):List<MessageEntity>

}