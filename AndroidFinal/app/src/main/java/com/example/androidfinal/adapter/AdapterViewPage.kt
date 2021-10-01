package com.example.androidfinal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidfinal.ui.about.AboutFragment
import com.example.androidfinal.ui.favorite.FavoriteFragment
import com.example.androidfinal.ui.movielist.MoviesListFragment
import com.example.androidfinal.ui.setting.SettingFragment


class AdapterViewPage(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        when (position) {
            TAB_INDEX_FIRST -> return MoviesListFragment.newInstance()
            TAB_INDEX_SECOND -> return FavoriteFragment.newInstance()
            TAB_INDEX_THIRD -> return SettingFragment.newInstance()
            TAB_INDEX_FOUR -> return AboutFragment.newInstance()
        }
        return MoviesListFragment.newInstance()
    }

    override fun getItemCount(): Int {
        return 4
    }

    companion object {
        const val TAB_INDEX_FIRST = 0
        const val TAB_INDEX_SECOND = 1
        const val TAB_INDEX_THIRD = 2
        const val TAB_INDEX_FOUR = 3
    }
}
