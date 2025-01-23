package com.aman.aniview

import retrofit2.Call
import retrofit2.http.GET

interface AnimeApiService {
    @GET("top/anime")
    fun getTopAnime(): Call<AnimeResponse>
}
