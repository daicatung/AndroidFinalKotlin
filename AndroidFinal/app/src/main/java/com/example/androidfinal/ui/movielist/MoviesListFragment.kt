package com.example.androidfinal.ui.movielist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.DetailMovies
import com.example.androidfinal.R
import com.example.androidfinal.adapter.AdapterMovie
import com.example.androidfinal.adapter.AdapterMovie.onClickItemMovies
import com.example.androidfinal.api.*
import com.example.androidfinal.database.MoviesDatabase
import com.example.androidfinal.model.Cast
import com.example.androidfinal.model.Movies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MoviesListFragment : Fragment(), View.OnClickListener {
    //list Movie & Cast get from api
    private var listMovieFromApi: List<ApiGetMovie>? = null
    private var listCastFromApi: List<ApiGetCast>? = null
    private var mRecyclerViewDisplayAllMovie: RecyclerView? = null
    private var mRecyclerViewDisplayAllMovie2: RecyclerView? = null
    private var mAdapterMovie: AdapterMovie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // onclick option menu is true click
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movies_list, container, false)
        callApiMovie()

        mRecyclerViewDisplayAllMovie = root.findViewById(R.id.fragment_movies_list_rcy)
        mRecyclerViewDisplayAllMovie2 = root.findViewById(R.id.fragment_movies_list_rcy)
        mAdapterMovie = AdapterMovie(listMoviesAllApp, object : onClickItemMovies {
            override fun updateFavoriteMovies(mv: Movies?) {
                updateMovie(mv)
            }

            override fun deleteFavoriteMovies(mv: Movies?) {}
        }, this, context)
        return root
    }

    /*
    Get data movie from API
     */
    private fun callApiMovie() {
        ApiGetAllData.API_GET_ALL_DATA.getMovieApi(API_KEY, 1)!!.enqueue(
            object : Callback<ApiGetListMovie?> {
                override fun onResponse(
                    call: Call<ApiGetListMovie?>?,
                    response: Response<ApiGetListMovie?>?
                ) {
                    val l = response?.body()
                    listMovieFromApi = l?.results

                    //Get 20 movie from api
                    for (i in 0..19) {
                        val tmp = listMovieFromApi!![i].vote_average.toString() + ""
                        var movieType = "No Adult"
                        if (listMovieFromApi!![i].isAdult) {
                            movieType = "Adult"
                        }
                        listMoviesAllApp.add(
                            Movies(
                                listMovieFromApi!![i].title,
                                R.drawable.test_img,
                                listMovieFromApi!![i].release_date,
                                tmp,
                                listMovieFromApi!![i].overview,
                                movieType,
                                false
                            )
                        )
                        listMoviesAllApp[i].imgPoster =
                            BASE_URL + listMovieFromApi!![i].backdrop_path
                        listMoviesAllApp[i].idMovie = listMovieFromApi!![i].id
                        val listCastTmp: MutableList<Cast> = ArrayList()

                        //Call api cast after set data Movie list cast
                        callApiCast(listCastTmp, i)
                        listMoviesAllApp[i].listCast = listCastTmp
                    }
                    mRecyclerViewDisplayAllMovie!!.layoutManager = GridLayoutManager(context, 1)
                    mRecyclerViewDisplayAllMovie!!.adapter = mAdapterMovie
                }

                override fun onFailure(call: Call<ApiGetListMovie?>?, t: Throwable?) {
                    TODO("Not yet implemented")
                }
            }
        )
    }



    private fun callApiCast(listCastTmp: MutableList<Cast>, index: Int) {
        ApiGetAllData.API_GET_ALL_DATA.getCastApi(listMovieFromApi!![index].id)!!
            .enqueue(object : Callback<ApiGetListCast?> {
                override fun onResponse(
                    call: Call<ApiGetListCast?>?,
                    response: Response<ApiGetListCast?>?
                ) {
                    val l = response?.body() as ApiGetListCast
                    listCastFromApi = l.cast
                    for (i in listCastFromApi!!.indices) {
                        listCastTmp.add(
                            Cast(
                                listCastFromApi!![i].name,
                                BASE_URL + listCastFromApi!![i].profile_path
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ApiGetListCast?>?, t: Throwable?) {
                    TODO("Not yet implemented")
                }
            })
    }

    // event when clicking on the menu, the recyclerview will change style
    private var count = 0
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_movie -> if (count == 0) {
                AdapterMovie.checkShow = 1
                val adapterMovie3 = AdapterMovie(listMoviesAllApp)
                mRecyclerViewDisplayAllMovie2!!.layoutManager = GridLayoutManager(context, 3)
                mRecyclerViewDisplayAllMovie2!!.adapter = adapterMovie3
                ++count
                item.icon = resources.getDrawable(R.drawable.menu)
            } else if (count == 1) {
                AdapterMovie.checkShow = 0
                mAdapterMovie = AdapterMovie(listMoviesAllApp, object : onClickItemMovies {
                    override fun updateFavoriteMovies(mv: Movies?) {
                        updateMovie(mv)
                    }

                    override fun deleteFavoriteMovies(mv: Movies?) {}
                }, this, context)
                mRecyclerViewDisplayAllMovie!!.layoutManager = GridLayoutManager(context, 1)
                mRecyclerViewDisplayAllMovie!!.adapter = mAdapterMovie
                --count
                item.icon = resources.getDrawable(R.drawable.menu_grid_view)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun updateMovie(mv: Movies?) {
        if (mv!!.isMovieFavorite) {
            mv.isMovieFavorite = false
            context?.let { MoviesDatabase.getInstance(it)?.moviesDAO()?.deleteMovies(mv) }
            Toast.makeText(context, "Delete Movies complete", Toast.LENGTH_SHORT).show()
        } else {
            if (isMoviesExit(mv)) {
                mv.isMovieFavorite = true
                Toast.makeText(context, "Movies is already list favorite", Toast.LENGTH_SHORT)
                    .show()
                mAdapterMovie!!.setData(listMoviesAllApp)
                return
            }
            mv.isMovieFavorite = true

            context?.let { MoviesDatabase.getInstance(it)?.moviesDAO()?.insertMovies(mv) }
            Toast.makeText(context, "insert completed", Toast.LENGTH_SHORT).show()
        }
        mAdapterMovie!!.setData(listMoviesAllApp)
    }

    private fun isMoviesExit(mv: Movies): Boolean {
        val list: List<Movies?>? = MoviesDatabase.getInstance(requireContext())!!.moviesDAO()!!
            .checkMovies(mv.movieTitle)
        return list != null && !list.isEmpty()
    }

    companion object {
        const val BASE_URL = "https://image.tmdb.org/t/p/w200"
        const val API_KEY = "e7631ffcb8e766993e5ec0c1f4245f93"
        const val KEY_PASS_MOVIE = "KEY_PASS_MOVIES"

        //list display all movie
        var listMoviesAllApp: MutableList<Movies> = ArrayList()
        fun newInstance(): MoviesListFragment {
            return MoviesListFragment()
        }
    }
}


