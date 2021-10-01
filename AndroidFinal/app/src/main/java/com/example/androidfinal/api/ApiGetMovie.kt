package com.example.androidfinal.api


class ApiGetMovie(
    var isAdult: Boolean,
    var backdrop_path: String,
    var title: String,
    var vote_average: Double,
    var poster_path: String,
    var overview: String,
    var release_date: String
) {
    var id = 0
}
