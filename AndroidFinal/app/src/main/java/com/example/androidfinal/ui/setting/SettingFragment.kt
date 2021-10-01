package com.example.androidfinal.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.example.androidfinal.R

class SettingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }
    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.settingsFragment, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preference_setting, rootKey)
        }
    }
    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
}