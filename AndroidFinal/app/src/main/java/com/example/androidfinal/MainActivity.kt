package com.example.androidfinal

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.example.androidfinal.adapter.AdapterViewPage
import com.example.androidfinal.database.MoviesReminderDatabase
import com.example.androidfinal.model.Movies
import com.example.androidfinal.receiver.ReminderBroadcast
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var mViewPager: ViewPager2? = null
    private var mTabLayout: TabLayout? = null
    private var mViewPagerAdapter: AdapterViewPage? = null

    //component navigation drawer
    private var mImgAvatar: ImageView? = null
    private var mBtnShowAllReminder: Button? = null
    private var mBtnEditProfile: Button? = null
    private var mTvNameUser: TextView? = null
    private var mTvDateUser: TextView? = null
    private var mTvEmailUser: TextView? = null
    private var mTvSexUser: TextView? = null
    private var mTvReminderList1: TextView? = null
    private var mTvReminderList2: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.app_bar_main_tool_bar)
        setSupportActionBar(toolbar)
        mViewPager = findViewById(R.id.content_main__viewpager2_menu)
        mTabLayout = findViewById(R.id.content_main_tab_layout_menu)
        mViewPagerAdapter = AdapterViewPage(supportFragmentManager, lifecycle)
        mViewPager?.adapter = mViewPagerAdapter
        TabLayoutMediator(mTabLayout!!, mViewPager!!,
            TabConfigurationStrategy { tab, position ->
                when (position) {
                    AdapterViewPage.TAB_INDEX_FIRST -> tab.text = "Movies List"
                    AdapterViewPage.TAB_INDEX_SECOND -> tab.text = "Favorite"
                    AdapterViewPage.TAB_INDEX_THIRD -> tab.text = "Setting"
                    AdapterViewPage.TAB_INDEX_FOUR -> tab.text = "About"
                }
            }).attach()
        mTabLayout?.addOnTabSelectedListener(TabChangeListener(this))
        val drawer = findViewById<DrawerLayout>(R.id.activity_main_drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.activity_main_nav_view)
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
        )
            .setDrawerLayout(drawer)
            .build()
        val navController =
            Navigation.findNavController(this, R.id.content_main_nav_host_fragment_container)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
        val header = navigationView.getHeaderView(0)
        mBtnShowAllReminder = header.findViewById<View>(R.id.nav_header_main_btn_show_all) as Button
        mBtnEditProfile = header.findViewById<View>(R.id.nav_header_main_btn_edit) as Button
        mImgAvatar = header.findViewById<View>(R.id.nav_header_main_img_avatar) as ImageView
        mTvNameUser = header.findViewById<View>(R.id.nav_header_main_tv_name) as TextView
        mTvDateUser = header.findViewById<View>(R.id.activity_edit_user_tv_date_user) as TextView
        mTvEmailUser = header.findViewById<View>(R.id.activity_edit_user_tv_email_user) as TextView
        mTvSexUser = header.findViewById<View>(R.id.activity_edit_user_tv_sex_user) as TextView
        mTvReminderList1 = header.findViewById<View>(R.id.nav_header_main_tv_mv1) as TextView
        mTvReminderList2 = header.findViewById<View>(R.id.nav_header_main_tv_mv2) as TextView
        mBtnShowAllReminder!!.setOnClickListener(this)
        mBtnEditProfile!!.setOnClickListener(this)
        val prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE)
        if (prefs != null) {
            val name = prefs.getString("name", "No name defined")
            val date = prefs.getString("date_user", "No date defined")
            val email = prefs.getString("email_user", "No email defined")
            val gt = prefs.getString("sex_user", "No defined")
            mTvNameUser!!.text = name
            mTvDateUser!!.text = date
            mTvEmailUser!!.text = email
            mTvSexUser!!.text = gt
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE)
        if (prefs != null) {
            val name = prefs.getString("name_user", "No name defined")
            val date = prefs.getString("date_user", "No date defined")
            val email = prefs.getString("email_user", "No email defined")
            val gt = prefs.getString("sex_user", "No defined")
            mTvNameUser!!.text = name
            mTvDateUser!!.text = date
            mTvEmailUser!!.text = email
            mTvSexUser!!.text = gt
        }
        val list: MutableList<Movies> =
            MoviesReminderDatabase.getInstance(this)?.moviesDAO()?.getListMovies() as MutableList<Movies>

        for (i in list.indices) {
            createNotificationChannel()
            val intent = Intent(this@MainActivity, ReminderBroadcast::class.java)
            intent.putExtra("KEY_PASS_MOVIES", list[i])
            sendBroadcast(intent)
            val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, 0)
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager[AlarmManager.RTC_WAKEUP, list[i].timeReminder + 10000] = pendingIntent
        }
        if (list.size == 1) {
            mTvReminderList1!!.text =
                """${list[0].movieTitle} ${list[0].movieReleaseDate} ${list[0].movieRating}/10 +
${list[0].timeReminderDisplay}"""
        } else if (list.size > 1) {
            mTvReminderList1!!.text =
                """${list[0].movieTitle} ${list[0].movieReleaseDate} ${list[0].movieRating}/10 +
${list[0].timeReminderDisplay}"""
            mTvReminderList2!!.text =
                """${list[1].movieTitle} ${list[1].movieReleaseDate} ${list[1].movieRating}/10 +
${list[1].timeReminderDisplay}"""
        }
    }

    //create menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // support NavigateUp
    override fun onSupportNavigateUp(): Boolean {
        val navController =
            Navigation.findNavController(this, R.id.content_main_nav_host_fragment_container)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    //onclick view main activity
    override fun onClick(v: View) {
        when (v.id) {
            R.id.nav_header_main_btn_show_all -> {
                val intent = Intent(this@MainActivity, ShowAllReminder::class.java)
                startActivity(intent)
            }
            R.id.nav_header_main_btn_edit -> {
                val intent2 = Intent(this@MainActivity, EditUser::class.java)
                startActivity(intent2)
            }
            else -> {
            }
        }
    }

    //Change tab layout when fragment change
    private class TabChangeListener(private val mActivity: MainActivity) :
        OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {}
        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "name"
            val des = "des"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = des
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "notification"
        const val KEY_GET_LIST_OBJECT_REMINDER = "key get list reminder"
        const val KEY_SHARE_SAVE_DATA = "MyObjectUser"
    }
}