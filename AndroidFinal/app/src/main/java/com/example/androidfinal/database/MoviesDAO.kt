package com.example.androidfinal.database

import androidx.room.*
import com.example.androidfinal.model.Movies

@Dao
interface MoviesDAO {
    @Insert
    fun insertMovies(mv: Movies?)

    @Query("SELECT * FROM movies1")
    fun getListMovies(): MutableList<Movies>

    @Query("SELECT * FROM movies1 where movieTitle=:moviesName")
    fun checkMovies(moviesName: String?): MutableList<Movies?>?

    @Update
    fun updateMovies(mv: Movies?)

    @Delete
    fun deleteMovies(mv: Movies?)

    @Query("DELETE FROM movies1")
    fun deleteAllMovies()

    @Query("SELECT * FROM movies1 WHERE movieTitle LIKE '%' || :name || '%' ")
    fun searchMovies(name: String?): MutableList<Movies?>?
}