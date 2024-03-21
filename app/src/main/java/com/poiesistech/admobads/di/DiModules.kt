/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 1:28 PM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */


package com.poiesistech.admobads.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.poiesistech.admobads.internet_connection_manager.InternetManager
import com.poiesistech.admobads.remote_config.RemoteConfiguration
import com.poiesistech.admobads.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val internetManagerModule = module {
    single { InternetManager(androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) }
}

private val firebaseRemoteCfgModule = module {
    single {
        RemoteConfiguration(
            get(),
            androidContext().getSharedPreferences(
                "firebase_cfg_preferences",
                Application.MODE_PRIVATE
            )
        )
    }
}

private val prefsModule = module {
    single {
        SharedPreferenceUtils(
            androidContext().getSharedPreferences(
                "app_preferences",
                Application.MODE_PRIVATE
            )
        )
    }
}


val modulesList = listOf(internetManagerModule, firebaseRemoteCfgModule, prefsModule)