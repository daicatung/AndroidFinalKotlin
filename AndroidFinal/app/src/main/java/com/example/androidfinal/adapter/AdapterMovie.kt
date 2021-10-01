package com.example.androidfinal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfinal.R
import com.example.androidfinal.model.Movies


class AdapterMovie : RecyclerView.Adapter<AdapterMovie.AdapterMovieViewHolder> {

    private var mListMovies: MutableList<Movies>
    private var mContext: Context? = null
    private var mOnClickItemMovies: onClickItemMovies? = null
    private var monClickListener: View.OnClickListener? = null

    constructor(list: MutableList<Movies>, onClickItemMovies: onClickItemMovies?) {
        mListMovies = list
        mOnClickItemMovies = onClickItemMovies
    }

    constructor(
        list: MutableList<Movies>,
        onClickItemMovies: onClickItemMovies?,
        onClickListener: View.OnClickListener?,
        context: Context?
    ) {
        mListMovies = list
        mOnClickItemMovies = onClickItemMovies
        monClickListener = onClickListener
        mContext = context
    }

    constructor(list: MutableList<Movies>) {
        mListMovies = list
    }

    interface onClickItemMovies {
        fun updateFavoriteMovies(mv: Movies?)
        fun deleteFavoriteMovies(mv: Movies?)
    }

    fun setData(list: MutableList<Movies>) {
        mListMovies = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMovieViewHolder {
        val view: View = if (checkShow == 0) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_movies2, parent, false)
        }
        return AdapterMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterMovieViewHolder, position: Int) {
        val movies = mListMovies.get(position)
        holder.setData(movies)
        holder.mContainerLayout!!.setOnClickListener(monClickListener)
        holder.mContainerLayout!!.tag = movies
        if (checkShow == 0) {
            holder.mImgFavoriteMovie?.setOnClickListener {
                mOnClickItemMovies!!.updateFavoriteMovies(movies)
                mOnClickItemMovies!!.deleteFavoriteMovies(movies)
            }
        }
    }

    override fun getItemCount(): Int {
        return mListMovies.size
    }

    class AdapterMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mTvNameMovie: TextView? = null
        private var mTvReleaseDateMovie: TextView? = null
        private var mTvRatingMovie: TextView? = null
        private var mTvDetailMovie: TextView? = null
        private var mTvTypeMovie: TextView? = null
        private var mImgMovie: ImageView? = null
        var mImgFavoriteMovie: ImageView? = null
        var mContainerLayout: ConstraintLayout? = null

        fun setData(movies: Movies) {
            if (AdapterMovie.checkShow == 0) {
                val rating = "Rating: " + movies.movieRating + "/10"
                val releaseDate = "Date: " + movies.movieReleaseDate
                mTvNameMovie?.text = movies.movieTitle
                mTvDetailMovie?.text = movies.movieOverView
                mTvTypeMovie?.text = movies.movieType
                mTvRatingMovie?.text = rating
                mTvReleaseDateMovie?.text = releaseDate
                mImgMovie?.let { Glide.with(it).load(movies.imgPoster).into(mImgMovie!!) }
                if (movies.isMovieFavorite) {
                    mImgFavoriteMovie?.setImageResource(R.drawable.star)
                } else {
                    mImgFavoriteMovie?.setImageResource(R.drawable.star2)
                }
            } else {
                mTvNameMovie?.text = movies.movieTitle
                mImgMovie?.let { Glide.with(it).load(movies.imgPoster).into(mImgMovie!!) }
            }
        }

        init {
            if (checkShow == 0) {
                mContainerLayout = itemView.findViewById(R.id.item_movies_container)
                mTvNameMovie = itemView.findViewById(R.id.item_movies_tv_name_movie)
                mTvReleaseDateMovie = itemView.findViewById(R.id.item_movies_tv_release_date_movie)
                mTvRatingMovie = itemView.findViewById(R.id.item_movies_tv_rating_movie)
                mTvDetailMovie = itemView.findViewById(R.id.item_movies_tv_detail_movie)
                mTvTypeMovie = itemView.findViewById(R.id.item_movies_tv_type_movie)
                mImgMovie = itemView.findViewById(R.id.item_movies_img_movie)
                mImgFavoriteMovie = itemView.findViewById(R.id.item_movies_img_favorite_movie)
            } else {
                mContainerLayout = itemView.findViewById(R.id.item_movies2_container)
                mTvNameMovie = itemView.findViewById(R.id.item_movies2_tv_name_movie)
                mImgMovie = itemView.findViewById(R.id.item_movies2_img_movie)
            }
        }
    }
    companion object {
        var checkShow: Int = 0
    }
}
