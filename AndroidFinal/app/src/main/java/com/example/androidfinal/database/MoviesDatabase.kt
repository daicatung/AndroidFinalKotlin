package com.example.androidfinal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidfinal.model.Movies

@Database(entities = [Movies::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDAO?

    companion object {
        private const val DATABASE_NAME = "movies2.db"
        private var instance: MoviesDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MoviesDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java, DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}