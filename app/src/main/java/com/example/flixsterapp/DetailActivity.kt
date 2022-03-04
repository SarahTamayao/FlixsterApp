package com.example.flixsterapp

import android.media.AsyncPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.*
import okhttp3.Headers
import org.w3c.dom.Text

private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=95ff049eb56d5b8fd518034bf0173b83"
private const val YOUTUBE_API_KEY = "AIzaSyDQfTE4EoUyzvCF0tFcUSRt2QLeGwxj6t0"
private  const val TAG = "DetailActivity"
class DetailActivity : YouTubeBaseActivity() {

    private lateinit var textViewTitle: TextView
    private lateinit var textViewOverview: TextView
    private lateinit var ratingBarVoteAverage: RatingBar
    private lateinit var player: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewOverview = findViewById(R.id.textViewOverview)
        ratingBarVoteAverage = findViewById(R.id.ratingBarVoteAverage)
        player = findViewById(R.id.player)



        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        Log.i(TAG, "Movie is $movie")
        textViewTitle.text = movie.title
        textViewOverview.text = movie.overview
        // get rating of movie
        ratingBarVoteAverage.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG,"onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results = json.jsonObject.getJSONArray("results")
                if(results.length() == 0){
                    Log.w(TAG,"No Movie Trailers Found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubekey = movieTrailerJson.getString("key")
                //play youtube video with this trailer
                initializeYoutube(youtubekey)

            }

        })



    }

    private fun initializeYoutube(youtubekey: String) {
        player.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                player?.cueVideo(youtubekey);
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationFailure")
            }

        } )

    }
}