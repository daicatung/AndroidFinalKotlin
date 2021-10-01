package com.example.androidfinal.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.DetailMovies
import com.example.androidfinal.R
import com.example.androidfinal.adapter.AdapterMovie
import com.example.androidfinal.adapter.AdapterMovie.onClickItemMovies
import com.example.androidfinal.database.MoviesDatabase
import com.example.androidfinal.model.Movies

class FavoriteFragment : Fragment(), View.OnClickListener {
    private var mRecyclerViewMovieFavorite: RecyclerView? = null
    private var mAdapterMovieFavorite: AdapterMovie? = null
    private lateinit var mListMoviesFavorite: MutableList<Movies>
    private var mEdtSearchFavorite: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        mListMoviesFavorite!!.clear()
        /*
            Get movie list data from room
         */
        mListMoviesFavorite = ArrayList()
        /*
            Get movie list data from room
         */
        mListMoviesFavorite = MoviesDatabase.getInstance(requireContext())?.moviesDAO()?.getListMovies()!!
        mAdapterMovieFavorite = mListMoviesFavorite?.let {
            AdapterMovie(it, object : onClickItemMovies {
                override fun updateFavoriteMovies(mv: Movies?) {
                    updateMovie(mv)
                }

                override fun deleteFavoriteMovies(mv: Movies?) {
                    alertDialogDeleteMovies(mv)
                }
            }, this, context)
        }
        mRecyclerViewMovieFavorite!!.layoutManager = GridLayoutManager(context, 1)
        mRecyclerViewMovieFavorite!!.adapter = mAdapterMovieFavorite
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        mListMoviesFavorite = ArrayList()
        mRecyclerViewMovieFavorite = root.findViewById(R.id.fragment_favorite_rcy)
        mEdtSearchFavorite = root.findViewById(R.id.fragment_favorite_edt_search)
        mEdtSearchFavorite?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val listSearchMovie: MutableList<Movies> = ArrayList()
                val str = s.toString()
                for (mv in mListMoviesFavorite as ArrayList<Movies?>) {
                    if (mv!!.movieTitle == str) {
                        listSearchMovie.add(mv)
                    }
                }
                mAdapterMovieFavorite = AdapterMovie(listSearchMovie, object : onClickItemMovies {
                    override fun updateFavoriteMovies(mv: Movies?) {
                        updateMovie(mv)
                    }

                    override fun deleteFavoriteMovies(mv: Movies?) {
                        alertDialogDeleteMovies(mv)
                    }
                }, context as View.OnClickListener?, context)
                mAdapterMovieFavorite!!.setData(listSearchMovie)
                mRecyclerViewMovieFavorite?.layoutManager = GridLayoutManager(context, 1)
                mRecyclerViewMovieFavorite?.adapter = mAdapterMovieFavorite
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    mAdapterMovieFavorite =
                        AdapterMovie(mListMoviesFavorite, object : onClickItemMovies {
                            override fun updateFavoriteMovies(mv: Movies?) {
                                updateMovie(mv)
                            }

                            override fun deleteFavoriteMovies(mv: Movies?) {
                                alertDialogDeleteMovies(mv)
                            }
                        })
                    mRecyclerViewMovieFavorite?.layoutManager = GridLayoutManager(context, 1)
                    mRecyclerViewMovieFavorite?.adapter = mAdapterMovieFavorite
                }
            }
        })
        return root
    }

    private fun updateMovie(mv: Movies?) {
        mv?.isMovieFavorite = !mv!!.isMovieFavorite
        mListMoviesFavorite?.let { mAdapterMovieFavorite!!.setData(it) }
    }

    private fun alertDialogDeleteMovies(mv: Movies?) {
        AlertDialog.Builder(requireContext())
            .setTitle("ALERT VIEW TITLE")
            .setMessage("Are you sure you want to remove this item from favourite ?")
            .setPositiveButton("yes") { dialog, which ->
                mv!!.isMovieFavorite = false
                Toast.makeText(context, "Movies delete completed", Toast.LENGTH_SHORT).show()
                mListMoviesFavorite!!.remove(mv)
                context?.let { MoviesDatabase.getInstance(it)?.moviesDAO()?.deleteMovies(mv) }
                mAdapterMovieFavorite!!.setData(mListMoviesFavorite!!)
            }
            .setNegativeButton("no", null).show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.item_movies_container -> {
                val mv = v.tag as Movies
                val intent = Intent(context, DetailMovies::class.java)
                intent.putExtra(KEY_PASS_MOVIE, mv)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    companion object {
        /*
       keyword to shot movies to screen detail movies
     */
        const val KEY_PASS_MOVIE = "KEY_PASS_MOVIES"

        /*
        fragment initialization
     */
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }
}