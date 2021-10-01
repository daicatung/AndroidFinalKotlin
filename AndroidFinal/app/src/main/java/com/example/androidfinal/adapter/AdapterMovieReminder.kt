package com.example.androidfinal.adapter


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfinal.R
import com.example.androidfinal.model.Movies


class AdapterMovieReminder(
    private val mListMovieReminder: List<Movies>,
    private val mOnClickListener: View.OnClickListener
) : RecyclerView.Adapter<AdapterMovieReminder.AdapterMovieReminderViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterMovieReminderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return AdapterMovieReminderViewHolder(view)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: AdapterMovieReminderViewHolder, position: Int) {
        val movies = mListMovieReminder[position]
        holder.setData(movies)
        holder.mContainerLayout.tag = movies
        holder.mContainerLayout.setOnClickListener(mOnClickListener)
    }

    override fun getItemCount(): Int {
        return mListMovieReminder.size
    }

    class AdapterMovieReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mImgMovie: ImageView = itemView.findViewById(R.id.item_reminder_img)
        private val mTvNameTitle: TextView = itemView.findViewById(R.id.item_reminder_tv_name)
        private val mTvTimeReal: TextView = itemView.findViewById(R.id.item_reminder_tv_time_real)
        val mContainerLayout: ConstraintLayout = itemView.findViewById(R.id.item_reminder_container)

        @RequiresApi(api = Build.VERSION_CODES.N)
        fun setData(movies: Movies) {
            val tmp: String =
                movies.movieTitle + "-" + movies.movieReleaseDate + "-" + movies.movieRating + "/10"

            Glide.with(mImgMovie).load(movies.imgPoster).into(mImgMovie)
            mTvNameTitle.text = tmp
            mTvTimeReal.text = movies.timeReminderDisplay
        }
    }
}
