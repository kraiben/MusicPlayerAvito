package com.gab.musicplayeravito.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.gab.musicplayeravito.di.ApplicationComponent
import com.gab.musicplayeravito.di.DaggerApplicationComponent
import com.gab.musicplayeravito.ui.service.CHANNEL_ID
import com.gab.musicplayeravito.ui.service.CHANNEL_NAME

class MusicApplication: Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}