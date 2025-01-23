//package com.aman.aniview
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//
//class AnimeAdapter(private val animeList: List<AnimeData>, private val onItemClick: (AnimeData) -> Unit) :
//    RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {
//
//    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.item_image)
//        val titleView: TextView = itemView.findViewById(R.id.item_title)
//        val episodesView: TextView = itemView.findViewById(R.id.item_episodes)
//        val ratingView: TextView = itemView.findViewById(R.id.item_rating)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.anime_card_view, parent, false)
//        return AnimeViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
//        val anime = animeList[position]
//
//        val displayTitle = anime.title_english ?: anime.title
//        val episodes = anime.episodes?.toString() ?: "N/A"
//        val rating = anime.score?.toString() ?: "N/A"
//
//        holder.titleView.text = displayTitle
//        holder.episodesView.text = "Episodes: $episodes"
//        holder.ratingView.text = "Rating: $rating"
//
//        Glide.with(holder.imageView.context)
//            .load(anime.images.jpg.large_image_url)
//            .placeholder(R.drawable.load)
//            .error(R.drawable.error)
//            .into(holder.imageView)
//
//        // Set the click listener for the card view
//        holder.itemView.setOnClickListener {
//            onItemClick(anime) // This will trigger the lambda in MainActivity
//        }
//    }
//
//    override fun getItemCount() = animeList.size
//}

package com.aman.aniview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AnimeAdapter(private val animeList: List<AnimeData>, private val onItemClick: (AnimeData) -> Unit) :
    RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val titleView: TextView = itemView.findViewById(R.id.item_title)
        val episodesView: TextView = itemView.findViewById(R.id.item_episodes)
        val ratingView: TextView = itemView.findViewById(R.id.item_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.anime_card_view, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        val displayTitle = anime.title_english ?: anime.title
        val episodes = anime.episodes?.toString() ?: "N/A"
        val rating = anime.score?.toString() ?: "N/A"

        holder.titleView.text = displayTitle
        holder.episodesView.text = "Episodes: $episodes"
        holder.ratingView.text = "Rating: $rating"

        Glide.with(holder.imageView.context)
            .load(anime.images.jpg.large_image_url)
            .placeholder(R.drawable.load)
            .error(R.drawable.error)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(anime)
        }
    }

    override fun getItemCount() = animeList.size
}
