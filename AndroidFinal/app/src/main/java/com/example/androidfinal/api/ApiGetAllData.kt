package com.example.androidfinal.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiGetAllData {
    @GET("3/movie/popular")
    fun getMovieApi(
        @Query("api_key") api_key: String?,
        @Query("page") page: Int
    ): Call<ApiGetListMovie?>?

    @GET("/3/movie/{id}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    fun getCastApi(@Path("id") id: Int): Call<ApiGetListCast?>?

    companion object {
        private val gSon: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        // https://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page={1}
        val API_GET_ALL_DATA: ApiGetAllData = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .build()
            .create(ApiGetAllData::class.java)
    }
}
