package com.example.flixsterapp

import android.app.ActivityOptions.makeSceneTransitionAnimation
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.math.log

const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"

class MovieAdapter(private val context: Context, private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    //expensive operation : creating a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    //cheap operation : simply bind data to an existing view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder position $position")
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val imageViewPoster = itemView.findViewById<ImageView>(R.id.imageViewPoster)
        private val textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
        private val textViewOverview = itemView.findViewById<TextView>(R.id.textViewOverview)

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(movie: Movie) {

            textViewTitle.text = movie.title
            textViewOverview.text = movie.overview

            var imageUrl: String =""
            if(context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.backdropImageUrl
            }else if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                imageUrl = movie.posterImageUrl
            }

            //Glide.with(context).load(imageUrl).into(imageViewPoster)
             Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_launcher_background).into(imageViewPoster)

        }

        override fun onClick(p0: View?) {
            //1. Get notified of the particular movie which was clicked
            val movie = movies[adapterPosition]
         // Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()

            //2. Use the intent system to navigate to the new activity
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)

            context.startActivity(intent)


        }


    }

}
