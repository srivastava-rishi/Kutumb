package com.example.kutu.dh

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.kutu.app.App
import com.example.kutu.data.db.database.AddressDatabase
import com.example.kutu.data.db.database.LastMessageDatabase
import com.example.kutu.data.db.database.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext applicationContext: Context): App {
        return applicationContext as App
    }

    @Provides
    @Singleton
    fun provideMessageDb(app: Application): MessageDatabase =
        Room.databaseBuilder(app, MessageDatabase::class.java,"messageDb")
            .build()

    @Provides
    @Singleton
    fun provideLastMessageDb(app: Application): LastMessageDatabase =
        Room.databaseBuilder(app, LastMessageDatabase::class.java,"lastMessageDb")
            .build()

    @Provides
    @Singleton
    fun provideAddressDb(app: Application): AddressDatabase =
        Room.databaseBuilder(app, AddressDatabase::class.java,"addressDb")
            .build()

}