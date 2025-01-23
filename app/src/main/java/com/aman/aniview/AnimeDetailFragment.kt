package com.aman.aniview

import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.aman.aniview.DataClass.AnimeDetailResponse
import com.aman.aniview.Interface.JikanApiService
import com.bumptech.glide.Glide
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.aman.aniview.DataClass.AnimeData
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class AnimeDetailFragment : Fragment() {

    private var malId: Int? = null
    private lateinit var titleView: TextView
    private lateinit var imageView: ImageView
    private lateinit var episodesView: TextView
    private lateinit var ratingView: TextView
    private lateinit var synopsisView: TextView
    private lateinit var genresView: TextView
    private lateinit var cardView: CardView
    private lateinit var loadingDialog: Dialog  // Declare a Dialog for loading spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anime_detail, container, false)

        // Initialize views
        titleView = view.findViewById(R.id.detail_title)
        imageView = view.findViewById(R.id.detail_image)
        episodesView = view.findViewById(R.id.detail_episodes)
        ratingView = view.findViewById(R.id.detail_rating)
        synopsisView = view.findViewById(R.id.detail_synopsis)
        genresView = view.findViewById(R.id.detail_genre)
        cardView = view.findViewById(R.id.cardview_anime_details)

        // Initialize the loading dialog with ProgressBar and TextView
        loadingDialog = Dialog(requireContext())
        val dialogLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }

        // Create ProgressBar
        val progressBar = ProgressBar(requireContext()).apply {
            isIndeterminate = true
            layoutParams = LinearLayout.LayoutParams(150, 150).apply {
                gravity = Gravity.CENTER
            }

            // Set the ProgressBar color to colorPrimary
            indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
            )
        }

        // Create TextView with "Loading Data..." message
        val loadingText = TextView(requireContext()).apply {
            text = "Loading Data..."
            textSize = 16f
            setPadding(0, 16, 0, 0)
            gravity = Gravity.CENTER
            // Ensure the text is fully visible by making the width wrap content
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16
            }
        }

        // Add ProgressBar and TextView to dialogLayout
        dialogLayout.addView(progressBar)
        dialogLayout.addView(loadingText)

        // Set the dialog content view
        loadingDialog.setContentView(dialogLayout)
        loadingDialog.setCancelable(false)

        malId = arguments?.getInt("mal_id")
        malId?.let { fetchAnimeDetails(it) }

        return view
    }

    private fun fetchAnimeDetails(id: Int) {
        // Show the loading dialog
        loadingDialog.show()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.getBaseUrl()) // Use secure URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(JikanApiService::class.java)
        apiService.getAnimeDetails(id).enqueue(object : Callback<AnimeDetailResponse> {
            override fun onResponse(
                call: Call<AnimeDetailResponse>,
                response: Response<AnimeDetailResponse>
            ) {
                // Hide the loading dialog once data is fetched
                loadingDialog.dismiss()

                if (response.isSuccessful) {
                    response.body()?.data?.let { populateUI(it) }
                }
            }

            override fun onFailure(call: Call<AnimeDetailResponse>, t: Throwable) {
                // Hide the loading dialog on failure
                loadingDialog.dismiss()
                // Handle error here (show a toast or a retry option)
            }
        })
    }

    private fun populateUI(data: AnimeData) {
        titleView.text = data.title_english ?: data.title
        Glide.with(this).load(data.images.jpg.large_image_url).into(imageView)
        episodesView.text = "Episodes: ${data.episodes ?: "N/A"}"
        ratingView.text = "Rating: ${data.score ?: "N/A"}/10"
        synopsisView.text = data.synopsis ?: "No synopsis available."
        genresView.text = data.genres.joinToString(", ") { it.name }

        // Show CardView once data is loaded
        cardView.visibility = View.VISIBLE  // Set CardView to visible

        val webView: WebView = requireView().findViewById(R.id.video_player)
        data.trailer?.embed_url?.let { embedUrl ->
            webView.visibility = View.VISIBLE
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.loadUrl(embedUrl)
        }
    }

    companion object {
        fun newInstance(malId: Int): AnimeDetailFragment {
            val fragment = AnimeDetailFragment()
            val bundle = Bundle()
            bundle.putInt("mal_id", malId)
            fragment.arguments = bundle
            return fragment
        }
    }
}
