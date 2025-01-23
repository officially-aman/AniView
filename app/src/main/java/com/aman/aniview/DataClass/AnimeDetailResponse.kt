package com.aman.aniview.DataClass

data class AnimeDetailResponse(val data: AnimeData)

data class AnimeData(
    val mal_id: Int,
    val title: String,
    val title_english: String?,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String?,
    val genres: List<Genre>,
    val images: Images,
    val trailer: Trailer?
)

data class Genre(val name: String)
data class Images(val jpg: Jpg)
data class Jpg(val large_image_url: String)
data class Trailer(val embed_url: String?)
