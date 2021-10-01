package com.example.androidfinal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfinal.adapter.AdapterCast
import com.example.androidfinal.database.MoviesReminderDatabase
import com.example.androidfinal.model.Movies
import java.util.*

class DetailMovies : AppCompatActivity() {
    private var mTvReleaseDate: TextView? = null
    private var mTvRating: TextView? = null
    private var mTvAdult: TextView? = null
    private var mTvOverview: TextView? = null
    private var mTvTimeReminder: TextView? = null
    private var mBtnReminder: Button? = null
    private var mImgFavorite: ImageView? = null
    private var mImgMovie: ImageView? = null
    private var mCalendar: Calendar? = null
    private var mRecyclerViewCast: RecyclerView? = null
    private var mCastAdapter: AdapterCast? = null
    private var mMoviesGetIntent: Movies? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movies)
        init()
        val intent = intent
        if (intent != null) {
            mMoviesGetIntent = intent.getSerializableExtra(KEY_PASS_MOVIES) as Movies?
        }
        supportActionBar!!.setTitle(mMoviesGetIntent!!.movieTitle) // for set actionbar title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // for add back arrow in action bar
        mCastAdapter = AdapterCast(mMoviesGetIntent!!.listCast!!)
        val horizontalLayoutManager = LinearLayoutManager(
            this@DetailMovies, LinearLayoutManager.HORIZONTAL, false
        )
        mRecyclerViewCast!!.layoutManager = horizontalLayoutManager
        mRecyclerViewCast!!.adapter = mCastAdapter
        mTvReleaseDate!!.text = "Release date: " + mMoviesGetIntent!!.movieReleaseDate
        mTvRating!!.text = "Rating: " + mMoviesGetIntent!!.movieRating + "/10"
        mTvOverview!!.text = """
            Description: 
            ${mMoviesGetIntent!!.movieOverView}
            """.trimIndent()
        setImgFavoriteDetail()
        mTvAdult!!.text = mMoviesGetIntent!!.movieType
        Glide.with(mImgMovie!!).load(mMoviesGetIntent!!.imgPoster).into(mImgMovie!!)
        mBtnReminder!!.setOnClickListener {
            listMovieReminder.add(mMoviesGetIntent)
            Toast.makeText(this@DetailMovies, "Reminder set!", Toast.LENGTH_SHORT).show()
            if (isMoviesExit(mMoviesGetIntent)) {
                Toast.makeText(this@DetailMovies, "the movie has been repeated", Toast.LENGTH_SHORT)
                    .show()
            } else {
                MoviesReminderDatabase.getInstance(this@DetailMovies)?.moviesDAO()
                    ?.insertMovies(mMoviesGetIntent)
            }
        }
        mTvTimeReminder!!.setOnClickListener { chooseTimeReminder() }
    }

    private fun init() {
        mTvReleaseDate = findViewById(R.id.activity_detail_movies_tv_date)
        mTvRating = findViewById(R.id.activity_detail_movies_tv_rating)
        mTvAdult = findViewById(R.id.activity_detail_movies_tv_adult)
        mTvOverview = findViewById(R.id.activity_detail_movies_tv_overview)
        mTvTimeReminder = findViewById(R.id.activity_detail_movies_tv_time_reminder)
        mBtnReminder = findViewById(R.id.activity_detail_movies_btn_reminder)
        mImgMovie = findViewById(R.id.activity_detail_movies_img_main)
        mImgFavorite = findViewById(R.id.activity_detail_movies_img_favorite)
        mRecyclerViewCast = findViewById(R.id.activity_detail_movies_rcy)
    }

    private fun setImgFavoriteDetail() {
        if (mMoviesGetIntent!!.isMovieFavorite) {
            mImgFavorite!!.setImageResource(R.drawable.star)
        } else {
            mImgFavorite!!.setImageResource(R.drawable.star2)
        }
        mTvAdult!!.text = mMoviesGetIntent!!.movieType
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun chooseTimeReminder() {
        val value = arrayOf(Date())
        mCalendar = Calendar.getInstance()
        mCalendar?.time = value[0]
        DatePickerDialog(
            this,
            { _, y, m, d ->
                mCalendar?.set(Calendar.YEAR, y)
                mCalendar?.set(Calendar.MONTH, m)
                mCalendar?.set(Calendar.DAY_OF_MONTH, d)

                // now show the time picker
                TimePickerDialog(
                    this@DetailMovies,
                    { _, h, min ->
                        mCalendar?.set(Calendar.HOUR_OF_DAY, h)
                        mCalendar?.set(Calendar.MINUTE, min)
                        value[0] = mCalendar?.time!!
                        val formatDate = SimpleDateFormat("yyyy/MM/dd-HH:mm", Locale.getDefault())
                        mTvTimeReminder!!.text = formatDate.format(mCalendar?.time)
                        mMoviesGetIntent!!.timeReminder = mCalendar?.timeInMillis!!
                        Log.d(
                            "TAG",
                            "onTimeSet: " + mCalendar?.timeInMillis + ":" + System.currentTimeMillis()
                        )
                        mMoviesGetIntent!!.timeReminderDisplay =
                            formatDate.format(mCalendar?.time)
                    }, mCalendar?.get(Calendar.HOUR_OF_DAY)!!,
                    mCalendar?.get(Calendar.MINUTE)!!, true
                ).show()
            }, mCalendar?.get(Calendar.YEAR)!!, mCalendar?.get(Calendar.MONTH)!!,
            mCalendar?.get(Calendar.DAY_OF_MONTH)!!
        ).show()
    }

    private fun isMoviesExit(mv: Movies?): Boolean {
        val list: List<Movies> = MoviesReminderDatabase.getInstance(this)?.moviesDAO()?.checkMovies(
            mv!!.movieTitle
        ) as List<Movies>
        return list != null && list.isNotEmpty()
    }

    companion object {
        var listMovieReminder: MutableList<Movies?> = ArrayList()
        const val KEY_PASS_MOVIES = "KEY_PASS_MOVIES"
        const val MY_LIST_MOVIE_REMINDER = "list reminder"
        const val KEY_GET_LIST_OBJECT_REMINDER = "key get list reminder"
    }
}