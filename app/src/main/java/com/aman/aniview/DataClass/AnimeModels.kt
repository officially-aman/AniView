package com.aman.aniview

import java.io.Serializable

data class AnimeResponse(
    val data: List<AnimeData>
)

data class AnimeData(
    val mal_id: Int, // Anime ID
    val title_english: String?, // Allow null for title_english
    val title: String, // Title field from the API
    val score: Double?, // Anime score
    val episodes: Int?, // Number of episodes
    val images: AnimeImages // Contains image details
) : Serializable // Serializable for passing between fragments/activities

data class AnimeImages(
    val jpg: AnimeImageUrls,
    val webp: AnimeImageUrls
) : Serializable // Serializable for nested data class

data class AnimeImageUrls(
    val large_image_url: String // URL of the large image
) : Serializable // Serializable for nested data class
