package com.example.androidfinal.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.androidfinal.R
import com.example.androidfinal.model.Movies

class ReminderBroadcast : BroadcastReceiver() {
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        val movies = intent.getSerializableExtra("KEY_PASS_MOVIES") as Movies?
        if (movies != null) {
            val builder = NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.drawable.test_img)
                .setContentTitle(movies.movieTitle)
                .setContentText(movies.movieReleaseDate)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(200, builder.build())
            //MoviesReminderDatabase.getInstance(context).moviesDAO().deleteMovies(movies);
        }
    }
}