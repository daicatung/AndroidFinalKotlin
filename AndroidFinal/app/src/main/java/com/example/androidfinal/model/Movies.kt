package com.example.androidfinal.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "movies1")
class Movies : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var movieTitle: String? = null
    var movieImg = 0
    var movieReleaseDate: String? = null
    var movieRating: String? = null
    var movieOverView: String? = null
    var movieType: String? = null
    var isMovieFavorite = false

    @TypeConverters(MoviesConverter::class)
    var listCast: List<Cast>? = null
    var imgPoster: String? = null
    var timeReminder: Long = 0
    var timeReminderDisplay: String? = null
    var idMovie = 0

    constructor(
        movieTitle: String?,
        movieImg: Int,
        movieReleaseDate: String?,
        movieRating: String?,
        movieOverView: String?,
        movieType: String?,
        movieFavorite: Boolean
    ) {
        this.movieTitle = movieTitle
        this.movieImg = movieImg
        this.movieReleaseDate = movieReleaseDate
        this.movieRating = movieRating
        this.movieOverView = movieOverView
        this.movieType = movieType
        isMovieFavorite = movieFavorite
    }

    constructor()
}