package com.bizmiz.umidjonmarket111

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bizmiz.umidjonmarket111.di.dataModule
import com.bizmiz.umidjonmarket111.di.sharedPreferencesModule
import com.bizmiz.umidjonmarket111.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val modules = listOf(dataModule, viewModelModule, sharedPreferencesModule)
        startKoin { // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            koin.loadModules(modules)
        }
    }
}