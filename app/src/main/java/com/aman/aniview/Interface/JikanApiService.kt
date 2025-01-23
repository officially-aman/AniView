package com.aman.aniview.Interface

import com.aman.aniview.DataClass.AnimeDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApiService {
    @GET("anime/{id}")
    fun getAnimeDetails(@Path("id") id: Int): Call<AnimeDetailResponse>
}
