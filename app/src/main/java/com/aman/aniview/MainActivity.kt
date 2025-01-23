package com.aman.aniview

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingImageView: ImageView
    private lateinit var noInternetImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        loadingImageView = findViewById(R.id.loadingImageView)
        noInternetImageView = findViewById(R.id.noInternetImageView)

        if (isConnectedToInternet()) {
            fetchAnimeData()
        } else {
            showNoInternetMessage()
        }

        window.statusBarColor = resources.getColor(R.color.statusBarColor)
    }

    private fun fetchAnimeData() {

        loadingImageView.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(AnimeApiService::class.java)
        apiService.getTopAnime().enqueue(object : Callback<AnimeResponse> {
            override fun onResponse(call: Call<AnimeResponse>, response: Response<AnimeResponse>) {
                // Hide the loading spinner once data is loaded
                loadingImageView.visibility = View.GONE

                if (response.isSuccessful) {
                    val animeList = response.body()?.data ?: emptyList()
                    recyclerView.adapter = AnimeAdapter(animeList) { anime ->
                        onAnimeClicked(anime)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
                // Hide the loading spinner on failure
                loadingImageView.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onAnimeClicked(anime: AnimeData) {
        Log.d("MainActivity", "Anime clicked: ${anime.title}, ID: ${anime.mal_id}")
        val fragment = AnimeDetailFragment.newInstance(anime.mal_id)

        // Hide the toolbar when navigating to the fragment
        findViewById<Toolbar>(R.id.toolbar).visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

        findViewById<RecyclerView>(R.id.recyclerView).visibility = View.GONE
        findViewById<FrameLayout>(R.id.fragment_container).visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
            findViewById<FrameLayout>(R.id.fragment_container).visibility = View.GONE

            // Show the toolbar again when navigating back
            findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

    // Function to check if there is internet connectivity
    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun showNoInternetMessage() {
        // Hide other views and show the no internet GIF
        recyclerView.visibility = View.GONE
        loadingImageView.visibility = View.GONE
        noInternetImageView.visibility = View.VISIBLE // Ensure this is showing
    }

}

