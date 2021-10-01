package com.example.androidfinal

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.adapter.AdapterMovieReminder
import com.example.androidfinal.database.MoviesReminderDatabase
import com.example.androidfinal.model.Movies

class ShowAllReminder : AppCompatActivity(), View.OnClickListener {
    private var mRecyclerViewReminder: RecyclerView? = null
    private var mAdapterMovieReminder: AdapterMovieReminder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_all_reminder)
        supportActionBar!!.title = "ALL REMINDERS" // for set actionbar title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // for add back arrow in action bar
        mRecyclerViewReminder = findViewById(R.id.fragment_setting_rcy)
    }

    override fun onResume() {
        super.onResume()
        val list: List<Movies> =
            MoviesReminderDatabase.getInstance(this)?.moviesDAO()?.getListMovies() as List<Movies>
        mAdapterMovieReminder = AdapterMovieReminder(
            list,
            (this@ShowAllReminder as View.OnClickListener)
        )
        mRecyclerViewReminder!!.layoutManager = GridLayoutManager(this, 1)
        mRecyclerViewReminder!!.adapter = mAdapterMovieReminder
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.item_reminder_container -> {
                val mv = v.tag as Movies
                val intent = Intent(this, DetailMovies::class.java)
                intent.putExtra(KEY_PASS_MOVIES, mv)
                startActivity(intent)
            }
            else -> {
            }
        }
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

    companion object {
        private const val KEY_PASS_MOVIES = "KEY_PASS_MOVIES"
    }
}